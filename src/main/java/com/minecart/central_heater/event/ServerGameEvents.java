package com.minecart.central_heater.event;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.misc.Alltags;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CentralHeater.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerGameEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player) {

            if (player.tickCount % 20 == 0) {
                if (player.getVehicle() instanceof AbstractMinecart minecart) {
                    double speed = minecart.getDeltaMovement().length();

                    AllTrigger.MINECART_SPEED.trigger(player, speed);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerFuelBurnTimes(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.is(Alltags.Items.OVERBURNT)) {
            event.setBurnTime(100);
            return;
        }

        if (stack.is(AllBlockItem.WOOD_CHIPS.get())) {
            event.setBurnTime(200);
        } else if (stack.is(AllBlockItem.FIRE_ASH.get())) {
            event.setBurnTime(50);
        } else if (stack.is(AllBlockItem.BURNT_LOG.get().asItem())) {
            event.setBurnTime(800);
        } else if (stack.is(AllBlockItem.BURNT_WOOD.get().asItem())) {
            event.setBurnTime(800);
        }
    }

    @SubscribeEvent
    public static void onItemDestroyed(ItemDestroyedBySourceEvent event) {
        if (event.getItemEntity().level().isClientSide) return;

        if (event.getDamageSource().is(DamageTypeTags.IS_FIRE)) {
            spawnAsh(event.getItemEntity());
        }
    }

    private static void spawnAsh(ItemEntity originalItem) {
        ItemStack stack = originalItem.getItem();

        if (originalItem.getOwner() instanceof ServerPlayer player) {
            AllTrigger.BURNT_OBJECT.trigger(player, stack);
        }

        int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);

        if (burnTime > 0) {
            int ashCount = 0;
            int stackSize = stack.getCount();
            for (int i = 0; i < stackSize; i++) {
                if (originalItem.level().random.nextFloat() < DataMapHook.getFireAshDropChance(stack)) {
                    ashCount++;
                }
            }
            if (ashCount > 0) {
                ItemStack ashStack = new ItemStack(AllBlockItem.FIRE_ASH.get(), ashCount);
                ItemEntity ashEntity = new ItemEntity(
                        originalItem.level(),
                        originalItem.getX(),
                        originalItem.getY(),
                        originalItem.getZ(),
                        ashStack
                );

                ashEntity.setDeltaMovement(originalItem.getDeltaMovement());
                ashEntity.setNoGravity(originalItem.isNoGravity());
                ashEntity.setInvulnerable(true);
                ashEntity.setDefaultPickUpDelay();

                if (originalItem.getOwner() != null)
                    ashEntity.setThrower(originalItem.getOwner().getUUID());

                originalItem.level().addFreshEntity(ashEntity);
            }
        }
    }
}