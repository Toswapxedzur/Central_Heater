package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.block.stove.BrickStoveBlock;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.RecipeUtil;
import com.minecart.central_heater.misc.enumeration.FireState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;

public class BrickStoveBlockEntity extends AbstractStoveBlockEntity {

    public int litTime;
    public FireState litState;
    public FireState prevLitState;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public NonNullList<ItemStack> prevItems;

    public static final int fuelConsumptionRate = 2;
    public static final int coolRate = 2;
    public static final float processMultiplier = 0.4f;

    public BrickStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.brick_stove.get(), pos, blockState, 2,
                stack -> ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0, 4, 1);
        litState = FireState.NONE;
        litTime = 0;
        prevLitState = FireState.NONE;
        cookingProgress = new int[getItemSlots()];
        cookingTotalTime = new int[getItemSlots()];
        prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.litState = FireState.getFireState(tag.getBoolean("litState"));
        this.litTime = tag.getInt("LitTime");
        this.prevLitState = FireState.getFireState(tag.getBoolean("prevLitState"));

        int[] progress = tag.getIntArray("cookingProgress");
        this.cookingProgress = (progress.length == getItemSlots()) ? progress : new int[getItemSlots()];

        int[] totalTime = tag.getIntArray("cookingTotalTime");
        this.cookingTotalTime = (totalTime.length == getItemSlots()) ? totalTime : new int[getItemSlots()];

        this.prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
        if (tag.contains("prevItems")) {
            ContainerHelper.loadAllItems(tag.getCompound("prevItems"), this.prevItems);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("litState", this.litState.getBooleanState());
        tag.putInt("LitTime", this.litTime);
        tag.putBoolean("prevLitState", this.prevLitState.getBooleanState());
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
        CompoundTag prevItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(prevItemsTag, prevItems);
        tag.put("prevItems", prevItemsTag);
    }

    @Override
    public void litTick() {
        litTime -= fuelConsumptionRate;
        if(litTime <= 0){
            litState = FireState.NONE;
            litTime = 0;
            if(prevLitState.equals(FireState.LIT))
                burnOneFuel();
        }
        prevLitState = litState;
    }

    @Override
    public void updateCookingTime() {
        for (int i = 0; i < getItemSlots(); i++) {
            ItemStack currentStack = items.getStackInSlot(i);
            ItemStack prevStack = prevItems.get(i);

            if(!isLit())
                cookingProgress[i] = Math.max(0, cookingProgress[i] - coolRate);

            // In 1.20.1: matches() checks Item, Count, and Tag
            if(ItemStack.matches(currentStack, prevStack)) {
                if(isLit())
                    cookingProgress[i] += 1;
            }
            // Check if same item type + tag (ignoring count difference if any, though logic implies single slots)
            else if(ItemStack.isSameItemSameTags(currentStack, prevStack)){
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.CAMPFIRE_COOKING, currentStack, processMultiplier);
            }
            else {
                cookingProgress[i] = 0;
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.CAMPFIRE_COOKING, currentStack, processMultiplier);
            }
            prevItems.set(i, currentStack.copy());
        }
    }

    @Override
    public void smeltItem() {
        for (int i = 0; i < getItemSlots(); i++) {
            if (cookingTotalTime[i] != 0 && cookingProgress[i] >= cookingTotalTime[i]) {
                ItemStack ingredient = items.getStackInSlot(i);

                // RecipeUtil should return Optional<CampfireCookingRecipe> in 1.20.1 context
                Optional<CampfireCookingRecipe> recipeOptional = RecipeUtil.getCookRecipe(level, RecipeType.CAMPFIRE_COOKING, ingredient);

                if (recipeOptional.isPresent()) {
                    CampfireCookingRecipe recipe = recipeOptional.get();
                    // Assemble using SimpleContainer
                    ItemStack result = recipe.assemble(new SimpleContainer(ingredient), level.registryAccess());

                    if (!result.isEmpty()) {
                        result.setCount(ingredient.getCount());
                        items.setStackInSlot(i, result);
                        if (this.getPlacer() != null && level instanceof ServerLevel serverLevel) {
                            if (serverLevel.getPlayerByUUID(this.getPlacer()) instanceof ServerPlayer serverPlayer) {
                                net.minecraft.advancements.CriteriaTriggers.RECIPE_CRAFTED.trigger(serverPlayer, recipe.getId(), Collections.singletonList(result));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BrickStoveBlockEntity entity){
        if(level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN))
            entity.dropItem();

        entity.litTick();
        entity.updateCookingTime();
        entity.smeltItem();
        entity.smolderBlock();

        if(state.getValue(BrickStoveBlock.LIT) != entity.isLit()){
            entity.updateBlockState(entity.getBlockState().setValue(BrickStoveBlock.LIT, entity.isLit()));
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BrickStoveBlockEntity entity){
        RandomSource randomsource = level.random;

        if(state.getValue(BrickStoveBlock.LIT)) {
            if (randomsource.nextFloat() < 0.11F) {
                for (int i = 0; i < randomsource.nextInt(2) + 2; i++) {
                    level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                            (double)pos.getX() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            (double)pos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
                            (double)pos.getZ() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            0.0, 0.07, 0.0);
                }
            }
        }

        if(state.getValue(BrickStoveBlock.LIT)) {
            int facingValue = state.getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue();

            for (int j = 0; j < entity.items.getSlots(); ++j) {
                if (!entity.items.getStackInSlot(j).isEmpty() && randomsource.nextFloat() < 0.2F) {
                    Direction direction = Direction.from2DDataValue(Math.floorMod(j + facingValue, 4));
                    float offset = 0.3125F;

                    double x = (double) pos.getX() + 0.5D
                            - (double) ((float) direction.getStepX() * offset)
                            + (double) ((float) direction.getClockWise().getStepX() * offset);

                    double y = (double) pos.getY() + 1.0D;

                    double z = (double) pos.getZ() + 0.5D
                            - (double) ((float) direction.getStepZ() * offset)
                            + (double) ((float) direction.getClockWise().getStepZ() * offset);

                    for (int k = 0; k < 4; ++k) {
                        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }
        }
    }

    public void kindle() {
        if(this.isLit())
            return;
        burnOneFuel();
    }

    public void burnOneFuel(){
        ItemStack stack = fuels.extractItem(true);
        int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        if(stack.isEmpty() || burnTime <= 0)
            return;

        stack = fuels.extractItem(false);
        this.litState = FireState.LIT;
        this.litTime += burnTime;

        if (stack.hasCraftingRemainingItem()) {
            ItemStack toInsert = stack.getCraftingRemainingItem();
            if(fuels.insertItem(toInsert, true).isEmpty())
                fuels.insertItem(toInsert, false);
            else
                Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }

        // DataMapHook replacement for 1.20.1 static map lookup
        float chance = DataMapHook.getFireAshDropChance(stack);
        if(getLevel().getRandom().nextFloat() < chance) {
            ItemStack toInsert = new ItemStack(AllBlockItem.FIRE_ASH.get());
            if(fuels.insertItem(toInsert, true).isEmpty())
                fuels.insertItem(toInsert, false);
            else
                Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean isLit() { return this.litState.equals(FireState.LIT); }

    @Override
    public boolean isHaunt() {
        return false;
    }

    @Override
    public void updateBlockEntity() {
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void updateBlockState(BlockState newState){
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
    }
}