package com.minecart.central_heater.event;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.misc.Alltags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CentralHeater.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerGameEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // In Forge 1.20.1, we check the phase. END is equivalent to .Post in 1.21
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player) {

            // Optimization: Only run every 20 ticks (1 second)
            if (player.tickCount % 20 == 0) {
                if (player.getVehicle() instanceof AbstractMinecart minecart) {
                    // length() calculates the speed vector magnitude
                    double speed = minecart.getDeltaMovement().length();

                    // AllTrigger.MINECART_SPEED is a direct instance in our 1.20.1 port
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
}