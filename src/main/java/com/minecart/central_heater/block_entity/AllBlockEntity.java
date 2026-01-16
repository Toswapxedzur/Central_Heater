package com.minecart.central_heater.block_entity;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block_entity.misc.BlazingFurnaceBlockEntity;
import com.minecart.central_heater.block_entity.misc.BurnableCampfireBlockEntity;
import com.minecart.central_heater.block_entity.misc.SturdyTankBlockEntity;
import com.minecart.central_heater.block_entity.cauldron.MudBrickPotBlockEntity;
import com.minecart.central_heater.block_entity.cauldron.CauldronBlockEntity;
import com.minecart.central_heater.block_entity.cauldron.GoldenCauldronBlockEntity;
import com.minecart.central_heater.block_entity.cauldron.BrickCauldronBlockEntity;
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
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CentralHeater.MODID);

    public static Supplier<BlockEntityType<StoneStoveBlockEntity>> stone_stove = BLOCK_ENTITIES.register("stone_stove",
            ()->BlockEntityType.Builder.of(StoneStoveBlockEntity::new, AllBlockItem.STONE_STOVE.get(), AllBlockItem.DEEPSLATE_STOVE.get()).build(null));

    public static Supplier<BlockEntityType<GoldenStoveBlockEntity>> red_nether_brick_stove = BLOCK_ENTITIES.register("red_nether_brick_stove",
            ()->BlockEntityType.Builder.of(GoldenStoveBlockEntity::new, AllBlockItem.RED_NETHER_BRICK_STOVE.get(), AllBlockItem.NETHER_BRICK_STOVE.get(), AllBlockItem.BLACKSTONE_STOVE.get()).build(null));

    public static Supplier<BlockEntityType<BrickStoveBlockEntity>> brick_stove = BLOCK_ENTITIES.register("brick_stove",
            ()->BlockEntityType.Builder.of(BrickStoveBlockEntity::new, AllBlockItem.BRICK_STOVE.get(), AllBlockItem.MUD_BRICK_STOVE.get()).build(null));

    public static Supplier<BlockEntityType<MudBrickPotBlockEntity>> pot = BLOCK_ENTITIES.register("mud_brick_pot",
            ()->BlockEntityType.Builder.of(MudBrickPotBlockEntity::new, AllBlockItem.MUD_BRICK_POT.get()).build(null));

    public static Supplier<BlockEntityType<BrickCauldronBlockEntity>> brick_cauldron = BLOCK_ENTITIES.register("brick_cauldron",
            ()->BlockEntityType.Builder.of(BrickCauldronBlockEntity::new, AllBlockItem.BRICK_CAULDRON.get(), AllBlockItem.BRICK_CAULDRON.get()).build(null));

    public static Supplier<BlockEntityType<CauldronBlockEntity>> iron_cauldron = BLOCK_ENTITIES.register("iron_cauldron",
            ()->BlockEntityType.Builder.of(CauldronBlockEntity::new, AllBlockItem.IRON_CAULDRON.get()).build(null));

    public static Supplier<BlockEntityType<GoldenCauldronBlockEntity>> golden_cauldron = BLOCK_ENTITIES.register("golden_cauldron",
            ()->BlockEntityType.Builder.of(GoldenCauldronBlockEntity::new, AllBlockItem.GOLDEN_CAULDRON.get()).build(null));

    public static Supplier<BlockEntityType<SturdyTankBlockEntity>> sturdy_tank = BLOCK_ENTITIES.register("sturdy_tank",
            ()->BlockEntityType.Builder.of(SturdyTankBlockEntity::new, AllBlockItem.STURDY_TANK.get()).build(null));

    public static Supplier<BlockEntityType<BurnableCampfireBlockEntity>> burnable_campfire = BLOCK_ENTITIES.register("burnable_campfire_be",
            ()->BlockEntityType.Builder.of(BurnableCampfireBlockEntity::new, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE).build(null));

    public static Supplier<BlockEntityType<BlazingFurnaceBlockEntity>> blazing_furnace = BLOCK_ENTITIES.register("blazing_furnace",
            ()->BlockEntityType.Builder.of(BlazingFurnaceBlockEntity::new, AllBlockItem.BLAZING_FURNACE.get()).build(null));

    public static void register(IEventBus modEventbus){
        BLOCK_ENTITIES.register(modEventbus);
    }
}
