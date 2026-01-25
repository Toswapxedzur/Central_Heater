package com.minecart.central_heater.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.mixin_interface.IAshProducer;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.RecipeHolder; // Note: In 1.20.1 this is likely not needed here
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, IAshProducer {

    private AbstractFurnaceBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Shadow protected NonNullList<ItemStack> items;

    @Unique private int central_heater$ashCount = 0;

    @Override
    public int getAshCount() {
        return this.central_heater$ashCount;
    }

    @Override
    public void setAshCount(int count) {
        this.central_heater$ashCount = count;
    }

    // 1.20.1 NBT Methods (No registries provider)
    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void central_heater$saveAsh(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("central_heater.ash_count", this.central_heater$ashCount);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void central_heater$loadAsh(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("central_heater.ash_count")) {
            this.central_heater$ashCount = tag.getInt("central_heater.ash_count");
        } else {
            this.central_heater$ashCount = 0;
        }
    }

    /**
     * Ash Generation Logic
     */
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;getBurnDuration(Lnet/minecraft/world/item/ItemStack;)I", shift = At.Shift.BY, by = 2))
    private static void central_heater$tickAshGeneration(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        IAshProducer ashFurnace = (IAshProducer) blockEntity;

        if (blockEntity.getItem(1).getCount() > 0) {
            if (((AbstractFurnaceBlockEntityAccessor)blockEntity).central_heater$isLit()) {
                ItemStack fuelStack = blockEntity.getItem(1);
                float chance = DataMapHook.getFireAshDropChance(fuelStack);

                if (chance > 0 && level.random.nextFloat() < chance) {
                    ashFurnace.setAshCount(ashFurnace.getAshCount() + 1);
                    blockEntity.setChanged();
                }
            }
        }
    }

    /**
     * Ash Dispensing Logic (Moving internal ash to the fuel slot)
     */
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;isLit()Z", ordinal = 7))
    private static void central_heater$dispenseAsh(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, @Local(ordinal = 1) LocalBooleanRef flag1) {
        IAshProducer ashFurnace = (IAshProducer) blockEntity;
        int storedAsh = ashFurnace.getAshCount();

        if (storedAsh > 0) {
            ItemStack fuelSlot = blockEntity.getItem(1);
            ItemStack ashItemSample = new ItemStack(AllBlockItem.FIRE_ASH.get());
            int maxStack = ashItemSample.getMaxStackSize();

            int amountToMove = 0;

            if (fuelSlot.isEmpty()) {
                amountToMove = Math.min(storedAsh, maxStack);
                ItemStack newStack = ashItemSample.copy();
                newStack.setCount(amountToMove);
                blockEntity.setItem(1, newStack);
            } else if (ItemStack.isSameItemSameTags(fuelSlot, ashItemSample)) {
                int spaceRemaining = maxStack - fuelSlot.getCount();
                if (spaceRemaining > 0) {
                    amountToMove = Math.min(storedAsh, spaceRemaining);
                    fuelSlot.grow(amountToMove);
                }
            }

            if (amountToMove > 0) {
                ashFurnace.setAshCount(ashFurnace.getAshCount() - amountToMove);
                flag1.set(true); // Triggers blockEntity.setChanged() via vanilla logic
            }
        }
    }
}