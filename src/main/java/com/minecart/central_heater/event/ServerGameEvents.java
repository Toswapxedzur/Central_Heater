package com.minecart.central_heater.event;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.block.misc.SturdyAnvilBlock;
import com.minecart.central_heater.heat.HeatDebugCommands;
import com.minecart.central_heater.heat.HeatManager;
import com.minecart.central_heater.heat.HeatTicker;
import com.minecart.central_heater.heat.debug.HeatDebugTempDisplay;
import com.minecart.central_heater.misc.DataMapHook;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = CentralHeater.MODID)
public class ServerGameEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        HeatDebugCommands.register(event);
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) {
            HeatTicker.tick(level);
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level && event.getChunk() instanceof LevelChunk chunk) {
            HeatTicker.enqueue(level, chunk.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof ServerLevel level) {
            HeatManager.markDirty(level, event.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!event.isCanceled() && event.getLevel() instanceof ServerLevel level) {
            HeatManager.markDirty(level, event.getPos());
        }
    }

    @SubscribeEvent
    public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        if (!event.isCanceled() && event.getLevel() instanceof ServerLevel level) {
            HeatManager.markDirty(level, event.getPos());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0) {
            if (player.getVehicle() instanceof AbstractMinecart minecart) {
                double speed = minecart.getDeltaMovement().length();
                AllTrigger.MINECART_SPEED.get().trigger(player, speed);
            }
        }
        if (event.getEntity() instanceof ServerPlayer player) {
            HeatDebugTempDisplay.tickPlayer(player);
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
            AllTrigger.BURNT_OBJECT.get().trigger(player, stack);
        }

        int burnTime = stack.getBurnTime(RecipeType.SMELTING);
        if (burnTime > 0) {
            int ashCount = 0;
            int stackSize = stack.getCount();
            for (int i = 0; i < stackSize; i++) {
                if (originalItem.level().random.nextFloat() < DataMapHook.getFireAshDropChance(stack)) {
                    ashCount++;
                }
            }
            if (ashCount > 0) {
                ItemStack ashStack = new ItemStack(AllBlockItem.FIRE_ASH.asItem(), ashCount);
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
                    ashEntity.setThrower(originalItem.getOwner());

                originalItem.level().addFreshEntity(ashEntity);
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilRepair(AnvilRepairEvent event) {
        // Check if the player is actually interacting with an Anvil Menu
        if (event.getEntity().containerMenu instanceof AnvilMenu menu) {

            // Access the physical block in the world safely
            menu.access.execute((level, pos) -> {
                BlockState state = level.getBlockState(pos);

                // If they are using our custom anvil, slash the break chance!
                if (state.getBlock() instanceof SturdyAnvilBlock) {
                    event.setBreakChance(0.04f); // 3x durability (4% instead of 12%)
                }
            });
        }
    }
}
