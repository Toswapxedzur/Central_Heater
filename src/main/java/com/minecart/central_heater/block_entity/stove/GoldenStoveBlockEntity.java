package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.minecart.central_heater.misc.RecipeUtil;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Optional;

public class GoldenStoveBlockEntity extends AbstractStoveBlockEntity {

    public NetherFireState litState;
    public NetherFireState prevLitState;

    public int[] hauntingProgress;
    public int[] hauntingTotalTime;

    public static final int fuelConsumptionRate = 2;
    public static final int netherFuelConsumptionRate = 4;
    public static final int coolRate = 2;
    public static final float processMultiplier = 1.4f;

    public GoldenStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.RED_NETHER_BRICK_STOVE.get(), pos, blockState, 4,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0 || DataMapHook.getNetherFuelBurnTime(stack) != 0, 4, 3);
        litState = NetherFireState.NONE;
        prevLitState = NetherFireState.NONE;
        hauntingProgress = new int[getItemSlots()];
        hauntingTotalTime = new int[getItemSlots()];
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        String litStateStr = !tag.contains("litState") ? "" : tag.getString("litState");
        this.litState = litStateStr.isEmpty() ? NetherFireState.NONE : NetherFireState.func.apply(litStateStr);

        String prevLitStateStr = tag.getString("litStateValidator");
        this.prevLitState = prevLitStateStr.isEmpty() ? NetherFireState.NONE : NetherFireState.func.apply(prevLitStateStr);

        int[] hProgress = !tag.contains("hauntingProgress") ? new int[0] : tag.getIntArray("hauntingProgress");
        this.hauntingProgress = (hProgress.length == getItemSlots()) ? hProgress : new int[getItemSlots()];
        int[] hauntTime = !tag.contains("seethingTotalTime") ? new int[0] : tag.getIntArray("seethingTotalTime");
        this.hauntingTotalTime = (hauntTime.length == getItemSlots()) ? hauntTime : new int[getItemSlots()];
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("litState", litState.getSerializedName());
        tag.putString("litStateValidator", prevLitState.getSerializedName());
        tag.putIntArray("hauntingProgress", hauntingProgress);
        tag.putIntArray("seethingTotalTime", hauntingTotalTime);
    }

    @Override
    public boolean isLit() { return this.litState.equals(NetherFireState.BURN); }

    @Override
    public boolean isHaunt() { return this.litState.equals(NetherFireState.SOUL); }

    @Override
    public void syncLitState(BlockState state) {
        if(!state.getValue(GoldenStoveBlock.LIT_SOUL).equals(this.litState)){
            this.updateBlockState(state.setValue(GoldenStoveBlock.LIT_SOUL, this.litState));
        }
    }

    @Override
    public void litTick() {
        if(litState.equals(NetherFireState.SOUL)) litTime -= netherFuelConsumptionRate;
        else litTime -= fuelConsumptionRate;

        if(litTime <= 0) {
            litState = NetherFireState.NONE;
            litTime = 0;
            if(!prevLitState.equals(NetherFireState.NONE)) burnOneFuel();
        }
        prevLitState = litState;
    }

    @Override
    public void burnOneFuel() {
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0 && DataMapHook.getNetherFuelBurnTime(stack) == 0) return;
        stack = fuels.extractItem(false);

        if (stack.hasCraftingRemainingItem()) {
            ItemStack toInsert = new ItemStack(stack.getCraftingRemainingItem().getItem());
            if(fuels.insertItem(toInsert, true).isEmpty()) fuels.insertItem(toInsert, false);
            else Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }

        float chance;
        ItemStack toInsert;
        if(DataMapHook.getNetherFuelBurnTime(stack) != 0) {
            this.litState = NetherFireState.SOUL;
            this.litTime += DataMapHook.getNetherFuelBurnTime(stack);
            chance = DataMapHook.getScorchedDustDropChance(stack);
            toInsert = new ItemStack(AllBlockItem.SCORCHED_DUST.asItem());
        } else {
            this.litState = NetherFireState.BURN;
            this.litTime += stack.getBurnTime(RecipeType.SMELTING);
            chance = DataMapHook.getFireAshDropChance(stack);
            toInsert = new ItemStack(AllBlockItem.FIRE_ASH.asItem());
        }

        if(getLevel().getRandom().nextFloat() < chance) {
            if(fuels.insertItem(toInsert, true).isEmpty()) fuels.insertItem(toInsert, false);
            else Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
    }

    @Override
    public void updateCookingTime() {
        boolean isLit = isLit();
        boolean isHaunt = isHaunt();

        for (int i = 0; i < getItemSlots(); i++) {
            ItemStack currentStack = items.getStackInSlot(i);
            ItemStack prevStack = prevItems.get(i);

            if (!isLit) cookingProgress[i] = Math.max(0, cookingProgress[i] - coolRate);
            if (!isHaunt) hauntingProgress[i] = Math.max(0, hauntingProgress[i] - coolRate);

            if (ItemStack.matches(currentStack, prevStack)) {
                if (isLit) cookingProgress[i]++;
                if (isHaunt) hauntingProgress[i]++;
            } else if (ItemStack.isSameItemSameComponents(currentStack, prevStack)) {
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, currentStack, processMultiplier);
                hauntingTotalTime[i] = RecipeUtil.getCookTime(level, AllRecipe.HAUNTING.get(), currentStack, processMultiplier);
            } else {
                cookingProgress[i] = 0;
                hauntingProgress[i] = 0;
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.SMELTING, currentStack, processMultiplier);
                hauntingTotalTime[i] = RecipeUtil.getCookTime(level, AllRecipe.HAUNTING.get(), currentStack, processMultiplier);
            }
            prevItems.set(i, currentStack.copy());
        }
    }

    @Override
    public void smeltItem() {
        for (int i = 0; i < getItemSlots(); i++) {
            ItemStack ingredient = items.getStackInSlot(i);
            ItemStack result = ItemStack.EMPTY;
            ResourceLocation recipeId = null;

            if (cookingTotalTime[i] > 0 && cookingProgress[i] >= cookingTotalTime[i]) {
                Optional<RecipeHolder<SmeltingRecipe>> holder = RecipeUtil.getCookRecipe(level, RecipeType.SMELTING, ingredient);
                if (holder.isPresent()) {
                    result = holder.get().value().assemble(new SingleRecipeInput(ingredient), level.registryAccess());
                    recipeId = holder.get().id();
                }
            }
            else if (hauntingTotalTime[i] > 0 && hauntingProgress[i] >= hauntingTotalTime[i]) {
                Optional<RecipeHolder<HauntingRecipe>> holder = RecipeUtil.getCookRecipe(level, AllRecipe.HAUNTING.get(), ingredient);
                if (holder.isPresent()) {
                    result = holder.get().value().assemble(new SingleRecipeInput(ingredient), level.registryAccess());
                    recipeId = holder.get().id();
                }
            }

            if (!result.isEmpty()) {
                cookingProgress[i] = 0;
                hauntingProgress[i] = 0;

                result.setCount(ingredient.getCount());
                items.setStackInSlot(i, result);

                if (this.getPlacer() != null && recipeId != null && level instanceof ServerLevel serverLevel) {
                    if (serverLevel.getPlayerByUUID(this.getPlacer()) instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.RECIPE_CRAFTED.trigger(serverPlayer, recipeId, Collections.singletonList(result));
                    }
                }
            }
        }
    }
}