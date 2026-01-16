package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.minecart.central_heater.misc.RecipeUtil;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GoldenStoveBlockEntity extends AbstractStoveBlockEntity {

    public int litTime;
    public NetherFireState litState;
    public NetherFireState prevLitState;

    public int[] cookingProgress;
    public int[] smeltingTotalTime;
    public int[] seethingTotalTime;
    public NonNullList<ItemStack> prevItems;

    public static final int fuelConsumptionRate = 2;
    public static final int netherFuelConsumptionRate = 4;
    public static final int coolRate = 2;
    public static final float processMultiplier = 1.4f;

    public GoldenStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.red_nether_brick_stove.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0 || DataMapHook.getNetherFuelBurnTime(stack) != 0, 4, 3);
        litState = NetherFireState.NONE;
        litTime = 0;
        prevLitState = NetherFireState.NONE;
        cookingProgress = new int[getItemSlots()];
        smeltingTotalTime = new int[getItemSlots()];
        seethingTotalTime = new int[getItemSlots()];
        prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        // 1. Load Primitives
        this.litTime = tag.getInt("litTime");

        // 2. Load Enums (Safe Fallback)
        // If the tag is missing, getString returns "", so ensure your func handles that
        // or we default to NONE manually.
        String litStateStr = tag.getString("litState");
        this.litState = litStateStr.isEmpty() ? NetherFireState.NONE : NetherFireState.func.apply(litStateStr);

        String prevLitStateStr = tag.getString("litStateValidator");
        this.prevLitState = prevLitStateStr.isEmpty() ? NetherFireState.NONE : NetherFireState.func.apply(prevLitStateStr);

        // 3. Array Safety
        // Ensure arrays are the correct size (getItemSlots()) to prevent index out of bounds crashes
        int[] progress = tag.getIntArray("cookingProgress");
        this.cookingProgress = (progress.length == getItemSlots()) ? progress : new int[getItemSlots()];

        int[] smeltTime = tag.getIntArray("cookingTotalTime"); // Note: Key matches saveAdditional
        this.smeltingTotalTime = (smeltTime.length == getItemSlots()) ? smeltTime : new int[getItemSlots()];

        int[] seethTime = tag.getIntArray("seethingTotalTime");
        this.seethingTotalTime = (seethTime.length == getItemSlots()) ? seethTime : new int[getItemSlots()];

        // 4. Item List Safety
        // Initialize list with Empty stacks first
        this.prevItems = NonNullList.withSize(getItemSlots(), ItemStack.EMPTY);

        // Only attempt to load if the specific tag exists
        if (tag.contains("validator")) {
            ContainerHelper.loadAllItems(tag.getCompound("validator"), prevItems, registries);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("litTime", litTime);
        tag.putString("litState", litState.getSerializedName());
        tag.putString("litStateValidator", prevLitState.getSerializedName());
        tag.putIntArray("cookingProgress", cookingProgress);
        tag.putIntArray("cookingTotalTime", smeltingTotalTime);
        tag.putIntArray("seethingTotalTime", seethingTotalTime);
        CompoundTag validatorTag = new CompoundTag();
        ContainerHelper.saveAllItems(validatorTag, prevItems, registries);
        tag.put("validator", validatorTag);
    }

    @Override
    public void litTick() {
        if(litState.equals(NetherFireState.SOUL))
            litTime -= netherFuelConsumptionRate;
        else
            litTime -= fuelConsumptionRate;
        if(litTime <= 0){
            litState = NetherFireState.NONE;
            litTime = 0;
            if(!prevLitState.equals(NetherFireState.NONE))
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
                smeltingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, items.getStackInSlot(i), processMultiplier);
                seethingTotalTime[i] = RecipeUtil.getCookTime(level, AllRecipe.HAUNTING.get(), items.getStackInSlot(i), processMultiplier);
            }else{
                cookingProgress[i] = 0;
                smeltingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, items.getStackInSlot(i), processMultiplier);
                seethingTotalTime[i] = RecipeUtil.getCookTime(level, AllRecipe.HAUNTING.get(), items.getStackInSlot(i), processMultiplier);
            }
            if(!litState.equals(prevLitState) && !prevLitState.equals(NetherFireState.SOUL)) {
                cookingProgress[i] = 0;
            }
            prevItems.set(i, items.getStackInSlot(i).copy());
        }
    }

    @Override
    public void smeltItem() {
        for (int i = 0; i < getItemSlots(); i++) {
            ItemStack ingredient = items.getStackInSlot(i);
            ItemStack result = ItemStack.EMPTY;
            ResourceLocation recipeId = null;

            if (smeltingTotalTime[i] != 0 && cookingProgress[i] >= smeltingTotalTime[i]) {
                Optional<RecipeHolder<SmeltingRecipe>> holder = RecipeUtil.getCookRecipe(level, RecipeType.SMELTING, ingredient);
                if (holder.isPresent()) {
                    result = holder.get().value().assemble(new SingleRecipeInput(ingredient), level.registryAccess());
                    recipeId = holder.get().id();
                }
            }
            else if (seethingTotalTime[i] != 0 && cookingProgress[i] >= seethingTotalTime[i]) {
                Optional<RecipeHolder<HauntingRecipe>> holder = RecipeUtil.getCookRecipe(level, AllRecipe.HAUNTING.get(), ingredient);
                if (holder.isPresent()) {
                    result = holder.get().value().assemble(new SingleRecipeInput(ingredient), level.registryAccess());
                    recipeId = holder.get().id();
                }
            }

            if (!result.isEmpty()) {
                result.setCount(ingredient.getCount());
                items.setStackInSlot(i, result);

                if (this.getPlacer() != null && recipeId != null && level instanceof ServerLevel serverLevel) {
                    if (serverLevel.getPlayerByUUID(this.getPlacer()) instanceof ServerPlayer serverPlayer) {
                        net.minecraft.advancements.CriteriaTriggers.RECIPE_CRAFTED.trigger(serverPlayer, recipeId, java.util.Collections.singletonList(result));
                    }
                }
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GoldenStoveBlockEntity entity) {
        if(level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN))
            entity.dropItem();

        entity.litTick();
        entity.updateCookingTime();
        entity.smeltItem();
        entity.smolderBlock();

        if(!state.getValue(GoldenStoveBlock.LIT_SOUL).equals(entity.litState)){
            entity.updateBlockState(entity.getBlockState().setValue(GoldenStoveBlock.LIT_SOUL, entity.litState));
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, GoldenStoveBlockEntity entity){
        RandomSource randomsource = level.random;
        if(!state.getValue(GoldenStoveBlock.LIT_SOUL).equals(NetherFireState.NONE)) {
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

        if(!state.getValue(GoldenStoveBlock.LIT_SOUL).equals(NetherFireState.NONE)) {
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

    public void kindle() {
        if(this.isLit() || this.isHaunt())
            return;
        burnOneFuel();
    }

    public boolean isLit() { return this.litState.equals(NetherFireState.BURN); }

    @Override
    public boolean isHaunt() {
        return this.litState.equals(NetherFireState.SOUL);
    }

    public void burnOneFuel(){
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0 && DataMapHook.getNetherFuelBurnTime(stack) == 0 )
            return;
        stack = fuels.extractItem(false);
        if (stack.hasCraftingRemainingItem()) {
            ItemStack toInsert = new ItemStack(stack.getCraftingRemainingItem().getItem());
            if(fuels.insertItem(toInsert, true).isEmpty())
                fuels.insertItem(toInsert, false);
            else
                Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
        float chance;
        ItemStack toInsert;
        if(DataMapHook.getNetherFuelBurnTime(stack) != 0){
            this.litState = NetherFireState.SOUL;
            this.litTime += DataMapHook.getNetherFuelBurnTime(stack);
            chance = DataMapHook.getScorchedDustDropChance(stack);
            toInsert = new ItemStack(AllBlockItem.SCORCHED_DUST.asItem());
        }else{
            this.litState = NetherFireState.BURN;
            this.litTime += stack.getBurnTime(RecipeType.SMELTING);
            chance = DataMapHook.getFireAshDropChance(stack);
            toInsert = new ItemStack(AllBlockItem.FIRE_ASH.asItem());
        }
        if(getLevel().getRandom().nextFloat() < chance) {
            if(fuels.insertItem(toInsert, true).isEmpty())
                fuels.insertItem(toInsert, false);
            else
                Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
    }

    public void updateBlockState(BlockState newState){
        getLevel().setBlock(getBlockPos(), newState, Block.UPDATE_ALL);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), newState, Block.UPDATE_ALL);
    }

    public void updateBlockEntity(){
        setChanged(getLevel(), this.getBlockPos(), getBlockState());
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

}
