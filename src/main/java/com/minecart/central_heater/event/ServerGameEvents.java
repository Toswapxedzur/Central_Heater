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
        } else if (stack.is(AllBlockItem.BRIQUETTES.get())) {
            event.setBurnTime(2400);
        } else if (stack.is(AllBlockItem.COAL_BRICKS.get().asItem())
                || stack.is(AllBlockItem.COAL_BRICK_TILE.get().asItem())
                || stack.is(AllBlockItem.COAL_BRICK_TILE_WALL.get().asItem())) {
            event.setBurnTime(6400);
        } else if (stack.is(AllBlockItem.COAL_BRICK_STAIR.get().asItem())
                || stack.is(AllBlockItem.COAL_BRICK_TILE_STAIR.get().asItem())) {
            event.setBurnTime(4800);
        } else if (stack.is(AllBlockItem.COAL_BRICK_SLAB.get().asItem())
                || stack.is(AllBlockItem.COAL_BRICK_TILE_SLAB.get().asItem())) {
            event.setBurnTime(3200);
        } else if (stack.is(AllBlockItem.COAL_BIT.get()) || stack.is(AllBlockItem.CHARCOAL_BIT.get())) {
            event.setBurnTime(400);
        } else if (stack.is(AllBlockItem.BURNT_BIRCH_LOG.get().asItem())
                || stack.is(AllBlockItem.BURNT_BIRCH_WOOD.get().asItem())
                || stack.is(AllBlockItem.BURNT_JUNGLE_LOG.get().asItem())
                || stack.is(AllBlockItem.BURNT_JUNGLE_WOOD.get().asItem())
                || stack.is(AllBlockItem.BURNT_CHERRY_LOG.get().asItem())
                || stack.is(AllBlockItem.BURNT_CHERRY_WOOD.get().asItem())
                || stack.is(AllBlockItem.BURNT_MANGROVE_LOG.get().asItem())
                || stack.is(AllBlockItem.BURNT_MANGROVE_WOOD.get().asItem())) {
            event.setBurnTime(1200);
        } else if (stack.is(AllBlockItem.BURNT_PLANKS.get().asItem())
                || stack.is(AllBlockItem.BURNT_BUTTON.get().asItem())) {
            event.setBurnTime(300);
        } else if (stack.is(AllBlockItem.BURNT_STAIRS.get().asItem())) {
            event.setBurnTime(450);
        } else if (stack.is(AllBlockItem.BURNT_SLAB.get().asItem())) {
            event.setBurnTime(150);
        } else if (stack.is(AllBlockItem.BURNT_FENCE.get().asItem())) {
            event.setBurnTime(466);
        } else if (stack.is(AllBlockItem.BURNT_FENCE_GATE.get().asItem())) {
            event.setBurnTime(1000);
        } else if (stack.is(AllBlockItem.BURNT_TRAPDOOR.get().asItem())) {
            event.setBurnTime(900);
        } else if (stack.is(AllBlockItem.BURNT_PRESSURE_PLATE.get().asItem())
                || stack.is(AllBlockItem.BURNT_DOOR.get().asItem())) {
            event.setBurnTime(600);
        } else if (stack.is(AllBlockItem.BURNT_BOAT.get())) {
            event.setBurnTime(1500);
        } else if (stack.is(AllBlockItem.BURNT_CHEST_BOAT.get())) {
            event.setBurnTime(1800);
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