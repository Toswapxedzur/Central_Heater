package com.minecart.central_heater.block_entity.stove;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.block.stove.BrickStoveBlock;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.enumeration.FireState;
import com.minecart.central_heater.misc.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class BrickStoveBlockEntity extends AbstractStoveBlockEntity {

    public FireState litState;
    public FireState prevLitState;

    public static final int fuelConsumptionRate = 2;
    public static final int coolRate = 2;
    public static final float processMultiplier = 0.4f;

    public BrickStoveBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntity.BRICK_STOVE.get(), pos, blockState, 2,
                stack -> stack.getBurnTime(RecipeType.SMELTING) != 0, 4, 1);
        litState = FireState.NONE;
        prevLitState = FireState.NONE;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.litState = FireState.getFireState(!tag.contains("litState") ? false : tag.getBoolean("litState"));
        this.prevLitState = FireState.getFireState(!tag.contains("prevLitState") ? false : tag.getBoolean("prevLitState"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("litState", this.litState.getBooleanState());
        tag.putBoolean("prevLitState", this.prevLitState.getBooleanState());
    }

    @Override
    public boolean isLit() { return this.litState.equals(FireState.LIT); }

    @Override
    public boolean isHaunt() { return false; }

    @Override
    public void syncLitState(BlockState state) {
        if(state.getValue(BrickStoveBlock.LIT) != this.isLit()) {
            this.updateBlockState(state.setValue(BrickStoveBlock.LIT, this.isLit()));
        }
    }

    @Override
    public void litTick() {
        litTime -= fuelConsumptionRate;
        if(litTime <= 0) {
            litState = FireState.NONE;
            litTime = 0;
            if(prevLitState.equals(FireState.LIT)) burnOneFuel();
        }
        prevLitState = litState;
    }

    @Override
    public void burnOneFuel() {
        ItemStack stack = fuels.extractItem(true);
        if(stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING) == 0) return;
        stack = fuels.extractItem(false);
        this.litState = FireState.LIT;
        this.litTime += stack.getBurnTime(RecipeType.SMELTING);
        if (stack.hasCraftingRemainingItem()) {
            ItemStack toInsert = new ItemStack(stack.getCraftingRemainingItem().getItem());
            if(fuels.insertItem(toInsert, true).isEmpty()) fuels.insertItem(toInsert, false);
            else Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
        float chance = DataMapHook.getFireAshDropChance(stack);
        if(getLevel().getRandom().nextFloat() < chance) {
            ItemStack toInsert = new ItemStack(AllBlockItem.FIRE_ASH.asItem());
            if(fuels.insertItem(toInsert, true).isEmpty()) fuels.insertItem(toInsert, false);
            else Containers.dropItemStack(getLevel(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), toInsert);
        }
    }

    @Override
    public void updateCookingTime() {
        for (int i = 0; i < getItemSlots(); i++) {
            if(!isLit()) cookingProgress[i] = Math.max(0, cookingProgress[i] - coolRate);
            if(ItemStack.matches(items.getStackInSlot(i), prevItems.get(i))) {
                if(isLit()) cookingProgress[i] += 1;
            } else if(ItemStack.isSameItemSameComponents(items.getStackInSlot(i), prevItems.get(i))){
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.CAMPFIRE_COOKING, items.getStackInSlot(i), processMultiplier);
            } else {
                cookingProgress[i] = 0;
                cookingTotalTime[i] = RecipeUtil.getCookTime(level, RecipeType.CAMPFIRE_COOKING, items.getStackInSlot(i), processMultiplier);
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
}