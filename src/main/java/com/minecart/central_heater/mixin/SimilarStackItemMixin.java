package com.minecart.central_heater.mixin;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public class SimilarStackItemMixin {
    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void centralHeater$overrideSimilarStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (action != ClickAction.SECONDARY || !SimilarStacker.hasContents(stack)) {
            return;
        }

        ItemStack slotStack = slot.getItem();
        if (slotStack.isEmpty()) {
            ItemStack removed = SimilarStacker.removeSelected(stack, 1);
            if (!removed.isEmpty()) {
                slot.safeInsert(removed);
                playRemoveOneSound(player);
                cir.setReturnValue(true);
            }
            return;
        }

        if (slot.mayPickup(player) && SimilarStacker.canMerge(stack, slotStack)) {
            ItemStack taken = slot.safeTake(slotStack.getCount(), stack.getMaxStackSize() - stack.getCount(), player);
            ItemStack merged = SimilarStacker.mergeStacks(stack, taken, stack.getMaxStackSize());
            player.containerMenu.setCarried(merged);
            if (!taken.isEmpty()) {
                slot.safeInsert(taken);
            }
            playInsertSound(player);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void centralHeater$overrideOtherSimilarStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        if (action != ClickAction.SECONDARY || !SimilarStacker.hasContents(stack) || !slot.allowModification(player)) {
            return;
        }

        if (other.isEmpty()) {
            ItemStack removed = SimilarStacker.removeSelected(stack, 1);
            if (!removed.isEmpty()) {
                access.set(removed);
                playRemoveOneSound(player);
                cir.setReturnValue(true);
            }
            return;
        }

        if (SimilarStacker.canMerge(stack, other)) {
            ItemStack merged = SimilarStacker.mergeStacks(stack, other, stack.getMaxStackSize());
            slot.setByPlayer(merged);
            playInsertSound(player);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void centralHeater$cycleSimilarSelection(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.isSecondaryUseActive() && SimilarStacker.cycleSelected(stack)) {
            playInsertSound(player);
            player.awardStat(Stats.ITEM_USED.get((Item) (Object) this));
            cir.setReturnValue(InteractionResultHolder.sidedSuccess(stack, level.isClientSide()));
        }
    }

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    private void centralHeater$getSimilarContentsTooltipImage(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        Optional<TooltipComponent> tooltip = SimilarStacker.tooltipImage(stack);
        if (tooltip.isPresent()) {
            cir.setReturnValue(tooltip);
        }
    }

    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
