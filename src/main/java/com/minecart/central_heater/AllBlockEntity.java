package com.minecart.central_heater;

import com.minecart.central_heater.block_entity.BurnableCampfireBlockEntity;
import com.minecart.central_heater.block_entity.pot.BrickPotBlockEntity;
import com.minecart.central_heater.block_entity.pot.NetherPotBlockEntity;
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

    public static Supplier<BlockEntityType<StoneStoveBlockEntity>> stone_stove_be = BLOCK_ENTITIES.register("stone_stove",
            ()->BlockEntityType.Builder.of(StoneStoveBlockEntity::new, AllBlockItem.stone_stove.get(), AllBlockItem.deepslate_stove.get()).build(null));

    public static Supplier<BlockEntityType<GoldenStoveBlockEntity>> red_nether_brick_stove_be = BLOCK_ENTITIES.register("red_nether_brick_stove",
            ()->BlockEntityType.Builder.of(GoldenStoveBlockEntity::new, AllBlockItem.red_nether_brick_stove.get(), AllBlockItem.nether_brick_stove.get(), AllBlockItem.blackstone_stove.get()).build(null));

    public static Supplier<BlockEntityType<BrickStoveBlockEntity>> brick_stove_be = BLOCK_ENTITIES.register("brick_stove",
            ()->BlockEntityType.Builder.of(BrickStoveBlockEntity::new, AllBlockItem.brick_stove.get(), AllBlockItem.mud_brick_stove.get()).build(null));

    public static Supplier<BlockEntityType<BrickPotBlockEntity>> brick_pot_be = BLOCK_ENTITIES.register("brick_pot",
            ()->BlockEntityType.Builder.of(BrickPotBlockEntity::new, AllBlockItem.brick_pot.get(), AllBlockItem.mud_brick_pot.get()).build(null));

    public static Supplier<BlockEntityType<StonePotBlockEntity>> stone_pot_be = BLOCK_ENTITIES.register("stone_pot",
            ()->BlockEntityType.Builder.of(StonePotBlockEntity::new, AllBlockItem.stone_pot.get(), AllBlockItem.deepslate_pot.get()).build(null));

    public static Supplier<BlockEntityType<NetherPotBlockEntity>> nether_pot_be = BLOCK_ENTITIES.register("nether_pot",
            ()->BlockEntityType.Builder.of(NetherPotBlockEntity::new, AllBlockItem.red_nether_brick_pot.get(), AllBlockItem.nether_brick_pot.get(), AllBlockItem.blackstone_pot.get()).build(null));

    public static Supplier<BlockEntityType<BurnableCampfireBlockEntity>> burnable_campfire = BLOCK_ENTITIES.register("burnable_campfire_be",
            ()->BlockEntityType.Builder.of(BurnableCampfireBlockEntity::new, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE).build(null));

    public static void register(IEventBus modEventbus){
        BLOCK_ENTITIES.register(modEventbus);
    }
}
