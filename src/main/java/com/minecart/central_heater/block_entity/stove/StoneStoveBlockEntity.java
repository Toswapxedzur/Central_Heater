package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.block.stove.StoneStoveBlock;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.enumeration.FireState;
import com.minecart.central_heater.misc.RecipeUtil;
import net.minecraft.core.*;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StoneStoveBlockEntity extends AbstractStoveBlockEntity {

    public int litTime;
    public FireState litState;
    public FireState prevLitState;

    public int[] cookingProgress;
    public int[] cookingTotalTime;
    public NonNullList<ItemStack> prevItems;

    public static final int fuelConsumptionRate = 2;
    public static final int coolRate = 2;
    public static final float processMultiplier = 1f;

    public StoneStoveBlockEntity(BlockPos pos, BlockState blockState){
        super(AllBlockEntity.stone_stove.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0, 4, 2);
        litState = FireState.NONE;
        litTime = 0;
        prevLitState = FireState.NONE;
        cookingProgress = new int[getItemSlots()];
        cookingTotalTime = new int[getItemSlots()];
        prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        // 1. Load Primitives with Safe Defaults
        this.litState = FireState.getFireState(tag.getBoolean("litState"));
        this.litTime = tag.getInt("LitTime");
        this.prevLitState = FireState.getFireState(tag.getBoolean("prevLitState"));

        // 2. Array Safety
        // Ensure arrays are initialized to the correct size even if tags are missing
        int[] progress = tag.getIntArray("cookingProgress");
        this.cookingProgress = (progress.length == getItemSlots()) ? progress : new int[getItemSlots()];

        int[] totalTime = tag.getIntArray("cookingTotalTime");
        this.cookingTotalTime = (totalTime.length == getItemSlots()) ? totalTime : new int[getItemSlots()];

        // 3. Item List Safety
        // Initialize list first to guarantee size
        this.prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);

        // Only load if tag exists
        if (tag.contains("prevItems")) {
            ContainerHelper.loadAllItems(tag.getCompound("prevItems"), this.prevItems, registries);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("litState", this.litState.getBooleanState());
        tag.putInt("LitTime", this.litTime);
        tag.putBoolean("prevLitState", this.prevLitState.getBooleanState());
        tag.putIntArray("cookingProgress", this.cookingProgress);
        tag.putIntArray("cookingTotalTime", this.cookingTotalTime);
        CompoundTag prevItemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(prevItemsTag, prevItems, registries);
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
            if(!isLit())
                cookingProgress[i] = Math.max(0, cookingProgress[i] - coolRate);
            if(ItemStack.matches(items.getStackInSlot(i), prevItems.get(i))) {
                if(isLit())
                    cookingProgress[i] += 1;
            }else if(ItemStack.isSameItemSameComponents(items.getStackInSlot(i), prevItems.get(i))){
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, items.getStackInSlot(i), processMultiplier);
            }else{
                cookingProgress[i] = 0;
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, items.getStackInSlot(i), processMultiplier);
            }
            prevItems.set(i, items.getStackInSlot(i).copy());
        }
    }

    @Override
    public void smeltItem() {
        for (int i = 0; i < getItemSlots(); i++) {
            if (cookingTotalTime[i] != 0 && cookingProgress[i] >= cookingTotalTime[i]) {
                ItemStack ingredient = items.getStackInSlot(i);

                Optional<RecipeHolder<CampfireCookingRecipe>> recipeHolder = RecipeUtil.getCookRecipe(level, RecipeType.CAMPFIRE_COOKING, ingredient);
                if (recipeHolder.isPresent()) {
                    ItemStack result = recipeHolder.get().value().assemble(new SingleRecipeInput(ingredient), level.registryAccess());

                    if (!result.isEmpty()) {
                        result.setCount(ingredient.getCount());
                        items.setStackInSlot(i, result);
                        if (this.getPlacer() != null && level instanceof ServerLevel serverLevel) {
                            if (serverLevel.getPlayerByUUID(this.getPlacer()) instanceof ServerPlayer serverPlayer) {
                                net.minecraft.advancements.CriteriaTriggers.RECIPE_CRAFTED.trigger(serverPlayer, recipeHolder.get().id(), java.util.Collections.singletonList(result));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StoneStoveBlockEntity entity) {
        if(level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN))
            entity.dropItem();

        entity.litTick();
        entity.updateCookingTime();
        entity.smeltItem();
        entity.smolderBlock();

        if(state.getValue(StoneStoveBlock.LIT) != entity.isLit()){
            entity.updateBlockState(entity.getBlockState().setValue(StoneStoveBlock.LIT, entity.isLit()));
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, StoneStoveBlockEntity entity){
        RandomSource randomsource = level.random;
        if(state.getValue(StoneStoveBlock.LIT).booleanValue()) {
            if (randomsource.nextFloat() < 0.11F) {
                for (int i = 0; i < randomsource.nextInt(2) + 2; i++) {
                    level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,true,
                            (double)pos.getX() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            (double)pos.getY() + randomsource.nextDouble() + randomsource.nextDouble(),
                            (double)pos.getZ() + 0.5 + randomsource.nextDouble() / 3.0 * (double)(randomsource.nextBoolean() ? 1 : -1),
                            0.0, 0.07, 0.0);
                }
            }
        }

        if(state.getValue(StoneStoveBlock.LIT).booleanValue()) {
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
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0)
            return;
        stack = fuels.extractItem(false);
        this.litState = FireState.LIT;
        this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        if (stack.hasCraftingRemainingItem()) {
            ItemStack toInsert = new ItemStack(stack.getCraftingRemainingItem().getItem());
            if(fuels.insertItem(toInsert, true).isEmpty())
                fuels.insertItem(toInsert, false);
            else
                Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
        float chance = DataMapHook.getFireAshDropChance(stack);
        if(getLevel().getRandom().nextFloat() < chance) {
            ItemStack toInsert = new ItemStack(AllBlockItem.FIRE_ASH.asItem());
            if(fuels.insertItem(toInsert, true).isEmpty())
                fuels.insertItem(toInsert, false);
            else
                Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
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

    public void updateBlockEntity(){
        setChanged(getLevel(), this.getBlockPos(), getBlockState());
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public void updateBlockState(BlockState newState){
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
    }
}
