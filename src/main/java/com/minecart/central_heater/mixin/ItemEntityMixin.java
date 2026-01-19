package com.minecart.central_heater.mixin;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getItem();

    @Shadow @Nullable public abstract Entity getOwner();

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"))
    private void central_heater$onBurned(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // In 1.20.1, 'level' is a public field, not level()
        if (this.level().isClientSide) return;

        if (source.is(DamageTypeTags.IS_FIRE)) {
            ItemStack stack = this.getItem();

            // Trigger advancement
            if (this.getOwner() instanceof ServerPlayer player) {
                AllTrigger.BURNT_OBJECT.trigger(player, stack);
            }

            // In Forge 1.20.1, use ForgeHooks to get burn time
            int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);

            if (burnTime > 0) {
                int ashCount = 0;
                int stackSize = stack.getCount();

                for (int i = 0; i < stackSize; i++) {
                    // Logic for ash drop chance (ensure DataMapHook is adjusted for 1.20.1)
                    if (this.random.nextFloat() < DataMapHook.getFireAshDropChance(stack)) {
                        ashCount++;
                    }
                }

                if (ashCount > 0) {
                    ItemStack ashStack = new ItemStack(AllBlockItem.FIRE_ASH.get(), ashCount);
                    ItemEntity ashEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), ashStack);

                    // Copy movement and rotation
                    ashEntity.setDeltaMovement(this.getDeltaMovement());
                    ashEntity.setXRot(this.getXRot());
                    ashEntity.setYRot(this.getYRot());
                    ashEntity.xRotO = this.xRotO;
                    ashEntity.yRotO = this.yRotO;

                    ashEntity.setNoGravity(this.isNoGravity());
                    ashEntity.setInvulnerable(true); // Prevent ash from burning immediately
                    ashEntity.setDefaultPickUpDelay();

                    if (this.getOwner() != null) {
                        ashEntity.setThrower(this.getOwner().getUUID());
                    }

                    this.level().addFreshEntity(ashEntity);
                }
            }
        }
    }
}