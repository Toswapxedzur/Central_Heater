package com.minecart.central_heater;

import com.minecart.central_heater.block_entity.BurnableCampfireBlockEntity;
import com.minecart.central_heater.block_entity.SturdyTankBlockEntity;
import com.minecart.central_heater.block_entity.pot.BrickPotBlockEntity;
import com.minecart.central_heater.block_entity.pot.CauldronBlockEntity;
import com.minecart.central_heater.block_entity.pot.GoldenCauldronBlockEntity;
import com.minecart.central_heater.block_entity.pot.StonePotBlockEntity;
import com.minecart.central_heater.block_entity.stove.BrickStoveBlockEntity;
import com.minecart.central_heater.block_entity.stove.GoldenStoveBlockEntity;
import com.minecart.central_heater.block_entity.stove.StoneStoveBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllBlockEntity {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Central_heater.MODID);

    public static Supplier<BlockEntityType<StoneStoveBlockEntity>> stone_stove = BLOCK_ENTITIES.register("stone_stove",
            ()->BlockEntityType.Builder.of(StoneStoveBlockEntity::new, AllBlockItem.stone_stove.get(), AllBlockItem.deepslate_stove.get()).build(null));

    public static Supplier<BlockEntityType<GoldenStoveBlockEntity>> red_nether_brick_stove = BLOCK_ENTITIES.register("red_nether_brick_stove",
            ()->BlockEntityType.Builder.of(GoldenStoveBlockEntity::new, AllBlockItem.red_nether_brick_stove.get(), AllBlockItem.nether_brick_stove.get(), AllBlockItem.blackstone_stove.get()).build(null));

    public static Supplier<BlockEntityType<BrickStoveBlockEntity>> brick_stove = BLOCK_ENTITIES.register("brick_stove",
            ()->BlockEntityType.Builder.of(BrickStoveBlockEntity::new, AllBlockItem.brick_stove.get(), AllBlockItem.mud_brick_stove.get()).build(null));

    public static Supplier<BlockEntityType<BrickPotBlockEntity>> pot = BLOCK_ENTITIES.register("brick_pot",
            ()->BlockEntityType.Builder.of(BrickPotBlockEntity::new, AllBlockItem.mud_brick_pot.get()).build(null));

    public static Supplier<BlockEntityType<StonePotBlockEntity>> stone_pot = BLOCK_ENTITIES.register("stone_pot",
            ()->BlockEntityType.Builder.of(StonePotBlockEntity::new, AllBlockItem.stone_pot.get(), AllBlockItem.deepslate_pot.get()).build(null));

    public static Supplier<BlockEntityType<CauldronBlockEntity>> iron_cauldron = BLOCK_ENTITIES.register("iron_cauldron",
            ()->BlockEntityType.Builder.of(CauldronBlockEntity::new, AllBlockItem.iron_cauldron.get()).build(null));

    public static Supplier<BlockEntityType<GoldenCauldronBlockEntity>> golden_cauldron = BLOCK_ENTITIES.register("golden_cauldron",
            ()->BlockEntityType.Builder.of(GoldenCauldronBlockEntity::new, AllBlockItem.golden_cauldron.get()).build(null));

    public static Supplier<BlockEntityType<SturdyTankBlockEntity>> sturdy_tank = BLOCK_ENTITIES.register("sturdy_tank",
            ()->BlockEntityType.Builder.of(SturdyTankBlockEntity::new, AllBlockItem.sturdy_tank.get()).build(null));

    public static Supplier<BlockEntityType<BurnableCampfireBlockEntity>> burnable_campfire = BLOCK_ENTITIES.register("burnable_campfire_be",
            ()->BlockEntityType.Builder.of(BurnableCampfireBlockEntity::new, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE).build(null));

    public static void register(IEventBus modEventbus){
        BLOCK_ENTITIES.register(modEventbus);
    }
}
