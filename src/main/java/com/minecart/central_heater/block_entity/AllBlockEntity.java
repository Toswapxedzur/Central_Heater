package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block_entity.cauldron.ModCauldronBlockEntity;
import com.minecart.central_heater.block_entity.misc.AshtrayBlockEntity;
import com.minecart.central_heater.block_entity.misc.BlazingFurnaceBlockEntity;
import com.minecart.central_heater.block_entity.misc.BurnableCampfireBlockEntity;
import com.minecart.central_heater.block_entity.misc.SturdyTankBlockEntity;
import com.minecart.central_heater.block_entity.stove.BrickStoveBlockEntity;
import com.minecart.central_heater.block_entity.stove.CopperStoveBlockEntity;
import com.minecart.central_heater.block_entity.stove.GoldenStoveBlockEntity;
import com.minecart.central_heater.block_entity.stove.StoneStoveBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllBlockEntity {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CentralHeater.MODID);

    public static Supplier<BlockEntityType<StoneStoveBlockEntity>> STONE_STOVE;
    public static Supplier<BlockEntityType<GoldenStoveBlockEntity>> RED_NETHER_BRICK_STOVE;
    public static Supplier<BlockEntityType<BrickStoveBlockEntity>> BRICK_STOVE;
    public static Supplier<BlockEntityType<ModCauldronBlockEntity>> MUD_BRICK_POT;
    public static Supplier<BlockEntityType<ModCauldronBlockEntity>> BRICK_CAULDRON;
    public static Supplier<BlockEntityType<ModCauldronBlockEntity>> IRON_CAULDRON;
    public static Supplier<BlockEntityType<ModCauldronBlockEntity>> GOLDEN_CAULDRON;
    public static Supplier<BlockEntityType<SturdyTankBlockEntity>> STURDY_TANK;
    public static Supplier<BlockEntityType<BurnableCampfireBlockEntity>> BURNABLE_CAMPFIRE;
    public static Supplier<BlockEntityType<BlazingFurnaceBlockEntity>> BLAZING_FURNACE;
    public static Supplier<BlockEntityType<AshtrayBlockEntity>> ASHTRAY;

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperStoveBlockEntity>> COPPER_STOVE;

    static {
        STONE_STOVE = BLOCK_ENTITIES.register("stone_stove",
                ()->BlockEntityType.Builder.of(StoneStoveBlockEntity::new, AllBlockItem.STONE_STOVE.get(), AllBlockItem.DEEPSLATE_STOVE.get()).build(null));

        RED_NETHER_BRICK_STOVE = BLOCK_ENTITIES.register("red_nether_brick_stove",
                ()->BlockEntityType.Builder.of(GoldenStoveBlockEntity::new, AllBlockItem.RED_NETHER_BRICK_STOVE.get(), AllBlockItem.NETHER_BRICK_STOVE.get(), AllBlockItem.BLACKSTONE_STOVE.get()).build(null));

        BRICK_STOVE = BLOCK_ENTITIES.register("brick_stove",
                ()->BlockEntityType.Builder.of(BrickStoveBlockEntity::new, AllBlockItem.BRICK_STOVE.get(), AllBlockItem.MUD_BRICK_STOVE.get()).build(null));

        MUD_BRICK_POT = BLOCK_ENTITIES.register("mud_brick_pot",
                ()->BlockEntityType.Builder.of((pos, state) -> new ModCauldronBlockEntity(MUD_BRICK_POT.get(), pos, state, 1), AllBlockItem.MUD_BRICK_POT.get()).build(null));

        BRICK_CAULDRON = BLOCK_ENTITIES.register("brick_cauldron",
                () -> BlockEntityType.Builder.of((pos, state) -> new ModCauldronBlockEntity(BRICK_CAULDRON.get(), pos, state, 2), AllBlockItem.BRICK_CAULDRON.get()).build(null));

        IRON_CAULDRON = BLOCK_ENTITIES.register("iron_cauldron",
                () -> BlockEntityType.Builder.of((pos, state) -> new ModCauldronBlockEntity(IRON_CAULDRON.get(), pos, state, 3), AllBlockItem.IRON_CAULDRON.get()).build(null));

        GOLDEN_CAULDRON = BLOCK_ENTITIES.register("golden_cauldron",
                () -> BlockEntityType.Builder.of((pos, state) -> new ModCauldronBlockEntity(GOLDEN_CAULDRON.get(), pos, state, 4), AllBlockItem.GOLDEN_CAULDRON.get()).build(null));

        STURDY_TANK = BLOCK_ENTITIES.register("sturdy_tank",
                ()->BlockEntityType.Builder.of(SturdyTankBlockEntity::new, AllBlockItem.STURDY_TANK.get()).build(null));

        BURNABLE_CAMPFIRE = BLOCK_ENTITIES.register("burnable_campfire",
                ()->BlockEntityType.Builder.of((pos, state) -> new BurnableCampfireBlockEntity(pos, state, 0), AllBlockItem.BURNABLE_CAMPFIRE.get(), AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get(), Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE).build(null));

        BLAZING_FURNACE = BLOCK_ENTITIES.register("blazing_furnace",
                ()->BlockEntityType.Builder.of(BlazingFurnaceBlockEntity::new, AllBlockItem.BLAZING_FURNACE.get()).build(null));

        ASHTRAY = BLOCK_ENTITIES.register("ashtray",
                ()->BlockEntityType.Builder.of(AshtrayBlockEntity::new, AllBlockItem.ASHTRAY.get()).build(null));

        COPPER_STOVE = BLOCK_ENTITIES.register("copper_stove", () -> BlockEntityType.Builder.of(
                        CopperStoveBlockEntity::new,
                        AllBlockItem.COPPER_STOVE.get(),
                        AllBlockItem.EXPOSED_COPPER_STOVE.get(),
                        AllBlockItem.WEATHERED_COPPER_STOVE.get(),
                        AllBlockItem.OXIDIZED_COPPER_STOVE.get(),
                        AllBlockItem.WAXED_COPPER_STOVE.get(),
                        AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get(),
                        AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get(),
                        AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get()
                ).build(null));
    }

    public static void register(IEventBus modEventbus){
        BLOCK_ENTITIES.register(modEventbus);
    }
}
