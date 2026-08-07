package com.minecart.central_heater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.mixin_interface.IAshProducer;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible, IAshProducer {
    private AbstractFurnaceBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Shadow protected NonNullList<ItemStack> items;

    @Unique private int ashCount = 0;

    @Override
    public int getAshCount() {
        return this.ashCount;
    }

    @Override
    public void setAshCount(int count) {
        this.ashCount = count;
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void central_heater$saveAsh(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        tag.putInt("central_heater.ash_count", this.ashCount);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void central_heater$loadAsh(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        if (tag.contains("central_heater.ash_count")) {
            this.setAshCount(tag.getInt("central_heater.ash_count"));
        }else{
            this.setAshCount(0);
        }
    }

    /**
     * Injects into serverTick to:
     * 1. Detect when fuel is consumed and generate ash.
     * 2. Move stored ash into the fuel slot if possible.
     */
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;getBurnDuration(Lnet/minecraft/world/item/ItemStack;)I", shift = At.Shift.BY, by = 2))
    private static void central_heater$tickAsh(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        IAshProducer ashFurnace = (IAshProducer) blockEntity;

        // 1. Ash Generation
        // We check if the furnace just successfully lit up (consumed fuel).
        // The injection point is right after 'litTime' is calculated but before the item is shrunk.
        if (blockEntity.getItem(1).getCount() > 0) { // Ensure there is actually fuel being processed
            // We can check isLit because the vanilla code sets litTime = duration immediately before this.
            // If litTime > 0, it means fuel was accepted.
            if (((AbstractFurnaceBlockEntityAccessor)blockEntity).central_heater$isLit()) {
                ItemStack fuelStack = blockEntity.getItem(1);

                // Get chance from your Data Map
                float chance = DataMapHook.getFireAshDropChance(fuelStack);

                // Roll for ash
                if (chance > 0 && level.random.nextFloat() < chance) {
                    ashFurnace.setAshCount(ashFurnace.getAshCount()+1);
                    blockEntity.setChanged();
                }
            }
        }
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;isLit()Z", ordinal = 7))
    private static void central_heater$dispenseAsh(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, @Local(ordinal = 1) LocalBooleanRef flag1) {
        IAshProducer ashFurnace = (IAshProducer) blockEntity;
        int storedAsh = ashFurnace.getAshCount();

        // Only proceed if we actually have ash to dispense
        if (storedAsh > 0) {
            ItemStack fuelSlot = blockEntity.getItem(1);
            ItemStack ashItem = new ItemStack(AllBlockItem.FIRE_ASH.get());
            int maxStack = ashItem.getMaxStackSize();

            int amountToMove = 0;

            if (fuelSlot.isEmpty()) {
                // Case 1: Slot is empty. Move as much as possible (up to 64).
                amountToMove = Math.min(storedAsh, maxStack);

                ItemStack newStack = ashItem.copy();
                newStack.setCount(amountToMove);
                blockEntity.setItem(1, newStack);

            } else if (ItemStack.isSameItemSameComponents(fuelSlot, ashItem)) {
                // Case 2: Slot has Ash. Fill the remaining space.
                int spaceRemaining = maxStack - fuelSlot.getCount();

                if (spaceRemaining > 0) {
                    amountToMove = Math.min(storedAsh, spaceRemaining);
                    fuelSlot.grow(amountToMove);
                }
            }

            if (amountToMove > 0) {
                ashFurnace.setAshCount(ashFurnace.getAshCount()-amountToMove);
                flag1.set(true);
            }
        }
    }
}
