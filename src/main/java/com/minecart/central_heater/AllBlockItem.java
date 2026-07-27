package com.minecart.central_heater;

import com.minecart.central_heater.block.cauldron.ModCauldronBlock;
import com.minecart.central_heater.block.misc.*;
import com.minecart.central_heater.block.stove.*;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.item.complex_items.*;
import com.minecart.central_heater.misc.enumeration.EnumProxyParam;
import net.minecraft.Util;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.*;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class AllBlockItem {
    public static final Float DEFAULT_FIRE_ASH_DROP_CHANCE = 0.2f;
    public static final Float DEFAULT_SCORCHED_DUST_DROP_CHANCE = 0.1f;

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CentralHeater.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CentralHeater.MODID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, CentralHeater.MODID);
    public static final DeferredRegister.Items DEFAULT_ITEMS = DeferredRegister.createItems("minecraft");
    public static final DeferredRegister.Blocks DEFAULT_BLOCKS = DeferredRegister.createBlocks("minecraft");

    public static final Tier STURDY;
    public static final Holder<ArmorMaterial> STURDY_ARMOR;

    public static final FoodProperties BURNT_FOOD;

    public static final DeferredBlock<AshtrayBlock> ASHTRAY;

    public static final DeferredItem<Item> BLACKSTONE_BRICK;
    public static final DeferredItem<BlazingFurnaceMinecartItem> BLAZING_FURNACE_MINECART;
    public static final DeferredItem<Item> BURNT_BEEF;
    public static final DeferredItem<Item> BURNT_CHICKEN;
    public static final DeferredItem<Item> BURNT_COD;
    public static final DeferredItem<Item> BURNT_MUTTON;
    public static final DeferredItem<Item> BURNT_PORKCHOP;
    public static final DeferredItem<Item> BURNT_RABBIT;
    public static final DeferredItem<Item> BURNT_SALMON;
    public static final DeferredItem<Item> CLAY_BIT;
    public static final DeferredItem<Item> CLAY_BRICK;
    public static final DeferredItem<Item> COBBLE;
    public static final DeferredItem<Item> DEEPSLATE_BRICK;
    public static final DeferredItem<Item> DEEPSLATE_COBBLE;
    public static final DeferredItem<Item> DIAMOND_SHARD;
    public static final DeferredItem<Item> FIRE_ASH;
    public static final DeferredItem<Item> MUD_BRICK;
    public static final DeferredItem<Item> RED_NETHER_BRICK;
    public static final DeferredItem<Item> SCORCHED_COAL;
    public static final DeferredItem<Item> SCORCHED_DUST;
    public static final DeferredItem<Item> SOUL_MIXTURE;
    public static final DeferredItem<Item> STONE_BRICK;
    public static final DeferredItem<AxeItem> STURDY_AXE;
    public static final DeferredItem<ArmorItem> STURDY_BOOTS;
    public static final DeferredItem<Item> STURDY_BRICK;
    public static final DeferredItem<ArmorItem> STURDY_CHESTPLATE;
    public static final DeferredItem<ArmorItem> STURDY_HELMET;
    public static final DeferredItem<HoeItem> STURDY_HOE;
    public static final DeferredItem<ArmorItem> STURDY_LEGGINGS;
    public static final DeferredItem<Item> STURDY_NUGGET;
    public static final DeferredItem<PickaxeItem> STURDY_PICKAXE;
    public static final DeferredItem<ShovelItem> STURDY_SHOVEL;
    public static final DeferredItem<SwordItem> STURDY_SWORD;
    public static final DeferredItem<SturdyTankItem> STURDY_TANK_ITEM;
    public static final DeferredItem<Item> WHEAT_DOUGH;
    public static final DeferredItem<Item> WHEAT_FLOUR;
    public static final DeferredItem<Item> WOOD_CHIPS;

    public static final DeferredBlock<Block> BLACKSTONE_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> BLACKSTONE_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> BLACKSTONE_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> BLACKSTONE_BRICK_TILE_WALL;
    public static final DeferredBlock<Block> BLACKSTONE_STOVE;
    public static final DeferredBlock<BlazingFurnaceBlock> BLAZING_FURNACE;
    public static final DeferredBlock<ModCauldronBlock> BRICK_CAULDRON;
    public static final DeferredBlock<Block> BRICK_STOVE;
    public static final DeferredBlock<BurntLogBlock> BURNT_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_WOOD;
    public static final DeferredBlock<BurntLogBlock> BURNT_SPRUCE_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_SPRUCE_WOOD;
    public static final DeferredBlock<BurntLogBlock> BURNT_BIRCH_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_BIRCH_WOOD;
    public static final DeferredBlock<BurntLogBlock> BURNT_JUNGLE_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_JUNGLE_WOOD;
    public static final DeferredBlock<BurntLogBlock> BURNT_ACACIA_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_ACACIA_WOOD;
    public static final DeferredBlock<BurntLogBlock> BURNT_DARK_OAK_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_DARK_OAK_WOOD;
    public static final DeferredBlock<BurntLogBlock> BURNT_CHERRY_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_CHERRY_WOOD;
    public static final DeferredBlock<BurntLogBlock> BURNT_MANGROVE_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_MANGROVE_WOOD;

    public static final DeferredBlock<Block> BURNT_PLANKS;
    public static final DeferredBlock<StairBlock> BURNT_STAIRS;
    public static final DeferredBlock<SlabBlock> BURNT_SLAB;
    public static final DeferredBlock<FenceBlock> BURNT_FENCE;
    public static final DeferredBlock<FenceGateBlock> BURNT_FENCE_GATE;
    public static final DeferredBlock<TrapDoorBlock> BURNT_TRAPDOOR;
    public static final DeferredBlock<ButtonBlock> BURNT_BUTTON;
    public static final DeferredBlock<PressurePlateBlock> BURNT_PRESSURE_PLATE;
    public static final DeferredBlock<Block> CLAY_CAULDRON;
    public static final DeferredBlock<Block> DEEPSLATE_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> DEEPSLATE_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> DEEPSLATE_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> DEEPSLATE_BRICK_TILE_WALL;
    public static final DeferredBlock<Block> DEEPSLATE_STOVE;
    public static final DeferredBlock<Block> GOLD_BARS;
    public static final DeferredBlock<ModCauldronBlock> GOLDEN_CAULDRON;
    public static final DeferredBlock<ModCauldronBlock> IRON_CAULDRON;
    public static final DeferredBlock<ModCauldronBlock> MUD_BRICK_POT;
    public static final DeferredBlock<Block> MUD_BRICK_STOVE;
    public static final DeferredBlock<Block> MUD_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> MUD_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> MUD_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> MUD_BRICK_TILE_WALL;
    public static final DeferredBlock<Block> NETHER_BRICK_STOVE;
    public static final DeferredBlock<Block> RED_NETHER_BRICK_STOVE;
    public static final DeferredBlock<Block> STONE_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> STONE_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> STONE_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> STONE_BRICK_TILE_WALL;
    public static final DeferredBlock<Block> STONE_STOVE;
    public static final DeferredBlock<Block> STURDY_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> STURDY_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> STURDY_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> STURDY_BRICK_TILE_WALL;
    public static final DeferredBlock<SturdyTankBlock> STURDY_TANK;

    public static final DeferredBlock<BurnableCampfireBlock> BURNABLE_CAMPFIRE;
    public static final DeferredBlock<BurnableCampfireBlock> BURNABLE_SOUL_CAMPFIRE;

    public static final DeferredItem<Item> BRIQUETTES;
    public static final DeferredItem<Item> SOAP;
    public static final DeferredItem<ShearsItem> STURDY_SHEARS;

    public static final DeferredBlock<Block> COAL_BRICKS;
    public static final DeferredBlock<Block> COAL_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> COAL_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> COAL_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> COAL_BRICK_TILE_WALL;

    public static final DeferredBlock<Block> GOLDEN_BRICKS;
    public static final DeferredBlock<Block> GOLDEN_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> GOLDEN_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> GOLDEN_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> GOLDEN_BRICK_TILE_WALL;

//    public static final DeferredBlock<Block> IRON_BRICKS;
//    public static final DeferredBlock<Block> IRON_BRICK_TILE;
//    public static final DeferredBlock<SlabBlock> IRON_BRICK_TILE_SLAB;
//    public static final DeferredBlock<StairBlock> IRON_BRICK_TILE_STAIR;
//    public static final DeferredBlock<WallBlock> IRON_BRICK_TILE_WALL;

    public static final DeferredBlock<Block> NETHERITE_BRICKS;
    public static final DeferredBlock<Block> NETHERITE_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> NETHERITE_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> NETHERITE_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> NETHERITE_BRICK_TILE_WALL;

    public static final DeferredBlock<Block> SCORCHED_BRICKS;
    public static final DeferredBlock<Block> SCORCHED_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> SCORCHED_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> SCORCHED_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> SCORCHED_BRICK_TILE_WALL;

    // --- Base Brick Stairs & Slabs ---
    public static final DeferredBlock<SlabBlock> COAL_BRICK_SLAB;
    public static final DeferredBlock<StairBlock> COAL_BRICK_STAIR;

    public static final DeferredBlock<SlabBlock> GOLDEN_BRICK_SLAB;
    public static final DeferredBlock<StairBlock> GOLDEN_BRICK_STAIR;

//    public static final DeferredBlock<SlabBlock> IRON_BRICK_SLAB;
//    public static final DeferredBlock<StairBlock> IRON_BRICK_STAIR;

    public static final DeferredItem<Item> COAL_BIT;
    public static final DeferredItem<Item> CHARCOAL_BIT;

    public static final DeferredBlock<SlabBlock> NETHERITE_BRICK_SLAB;
    public static final DeferredBlock<StairBlock> NETHERITE_BRICK_STAIR;

    public static final DeferredBlock<SlabBlock> SCORCHED_BRICK_SLAB;
    public static final DeferredBlock<StairBlock> SCORCHED_BRICK_STAIR;

    public static final DeferredBlock<Block> STURDY_ANVIL;
    public static final DeferredBlock<Block> CHIPPED_STURDY_ANVIL;
    public static final DeferredBlock<Block> DAMAGED_STURDY_ANVIL;

    public static final DeferredBlock<Block> STURDY_BRICKS;
    public static final DeferredBlock<StairBlock> STURDY_BRICK_STAIR;
    public static final DeferredBlock<SlabBlock> STURDY_BRICK_SLAB;

    public static final DeferredItem<Item> SCORCHED_BRIQUETTES;

    public static final DeferredBlock<Block> COPPER_STOVE;
    public static final DeferredBlock<Block> EXPOSED_COPPER_STOVE;
    public static final DeferredBlock<Block> WEATHERED_COPPER_STOVE;
    public static final DeferredBlock<Block> OXIDIZED_COPPER_STOVE;

    public static final DeferredBlock<Block> WAXED_COPPER_STOVE;
    public static final DeferredBlock<Block> WAXED_EXPOSED_COPPER_STOVE;
    public static final DeferredBlock<Block> WAXED_WEATHERED_COPPER_STOVE;
    public static final DeferredBlock<Block> WAXED_OXIDIZED_COPPER_STOVE;

    public static final DeferredBlock<DoorBlock> BURNT_DOOR;

    public static final DeferredItem<Item> BURNT_BOAT;
    public static final DeferredItem<Item> BURNT_CHEST_BOAT;

    public static final List<BurntWoodSet> BURNT_WOOD_SETS;

    public record BurntWoodSet(String material, String prefix, DeferredBlock<BurntLogBlock> log,
                               DeferredBlock<RotatedPillarBlock> wood, DeferredBlock<Block> planks,
                               DeferredBlock<StairBlock> stairs, DeferredBlock<SlabBlock> slab,
                               DeferredBlock<FenceBlock> fence, DeferredBlock<FenceGateBlock> fenceGate,
                               DeferredBlock<TrapDoorBlock> trapdoor, DeferredBlock<ButtonBlock> button,
                               DeferredBlock<PressurePlateBlock> pressurePlate, DeferredBlock<DoorBlock> door) {
        public List<DeferredBlock<? extends Block>> blocks() {
            return List.of(log, wood, planks, stairs, slab, fence, fenceGate, trapdoor, button, pressurePlate, door);
        }
    }

    static {
        STONE_BRICK = ITEMS.register("stone_brick", () -> new BrickItem(new Item.Properties()));
        DEEPSLATE_BRICK = ITEMS.register("deepslate_brick", () -> new BrickItem(new Item.Properties()));
        MUD_BRICK = ITEMS.register("mud_brick", () -> new BrickItem(new Item.Properties()));
        RED_NETHER_BRICK = ITEMS.register("red_nether_brick", () -> new BrickItem(new Item.Properties()));
        COBBLE = ITEMS.register("cobble", ()->new PebbleItem(new Item.Properties()));
        DEEPSLATE_COBBLE = ITEMS.register("deepslate_cobble", ()->new PebbleItem(new Item.Properties()));
        DIAMOND_SHARD = ITEMS.registerSimpleItem("diamond_shard");
        BLACKSTONE_BRICK = ITEMS.registerSimpleItem("blackstone_brick");
        STURDY_BRICK = ITEMS.registerSimpleItem("sturdy_brick", new Item.Properties().fireResistant());
        STURDY_NUGGET = ITEMS.registerSimpleItem("sturdy_nugget", new Item.Properties().fireResistant());
        SCORCHED_COAL = ITEMS.registerSimpleItem("scorched_coal");
        SCORCHED_DUST = ITEMS.registerSimpleItem("scorched_dust");
        FIRE_ASH = ITEMS.registerSimpleItem("fire_ash");
        CLAY_BIT = ITEMS.registerSimpleItem("clay_bit");
        CLAY_BRICK = ITEMS.registerSimpleItem("clay_brick");
        SOUL_MIXTURE = ITEMS.registerSimpleItem("soul_mixture");
        WHEAT_DOUGH = ITEMS.registerSimpleItem("wheat_dough");
        WHEAT_FLOUR = ITEMS.registerSimpleItem("wheat_flour");
        WOOD_CHIPS = ITEMS.registerSimpleItem("wood_chips");
        BURNT_FOOD = new FoodProperties.Builder().nutrition(2).effect(new MobEffectInstance(MobEffects.HUNGER, 300), 0.6f).effect(new MobEffectInstance(MobEffects.POISON, 120), 0.4f).build();
        BURNT_BEEF = ITEMS.registerSimpleItem("burnt_beef", new Item.Properties().food(BURNT_FOOD));
        BURNT_CHICKEN = ITEMS.registerSimpleItem("burnt_chicken", new Item.Properties().food(BURNT_FOOD));
        BURNT_COD = ITEMS.registerSimpleItem("burnt_cod", new Item.Properties().food(BURNT_FOOD));
        BURNT_MUTTON = ITEMS.registerSimpleItem("burnt_mutton", new Item.Properties().food(BURNT_FOOD));
        BURNT_PORKCHOP = ITEMS.registerSimpleItem("burnt_porkchop", new Item.Properties().food(BURNT_FOOD));
        BURNT_RABBIT = ITEMS.registerSimpleItem("burnt_rabbit", new Item.Properties().food(BURNT_FOOD));
        BURNT_SALMON = ITEMS.registerSimpleItem("burnt_salmon", new Item.Properties().food(BURNT_FOOD));

        STURDY = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 713, 8f, 3f, 2, () -> Ingredient.of(STURDY_BRICK.asItem()));
        STURDY_ARMOR = ARMOR_MATERIALS.register("sturdy", () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), p_323380_ -> {
                    p_323380_.put(ArmorItem.Type.BOOTS, 3);
                    p_323380_.put(ArmorItem.Type.LEGGINGS, 6);
                    p_323380_.put(ArmorItem.Type.CHESTPLATE, 8);
                    p_323380_.put(ArmorItem.Type.HELMET, 3);
                    p_323380_.put(ArmorItem.Type.BODY, 11);
                }), 2, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> Ingredient.of(STURDY_BRICK.asItem()),
                List.of(new ArmorMaterial.Layer(CentralHeater.modLoc("sturdy"))), 1f, 0.05f
        ));

        STURDY_PICKAXE = ITEMS.register("sturdy_pickaxe", () -> new PickaxeItem(STURDY, new Item.Properties().fireResistant().attributes(PickaxeItem.createAttributes(STURDY, 1, -2.8f))));
        STURDY_AXE = ITEMS.register("sturdy_axe", () -> new AxeItem(STURDY, new Item.Properties().fireResistant().attributes(AxeItem.createAttributes(STURDY, 9f, -3.4f))));
        STURDY_SHOVEL = ITEMS.register("sturdy_shovel", () -> new ShovelItem(STURDY, new Item.Properties().fireResistant().attributes(ShovelItem.createAttributes(STURDY, 1.5f, -3f))));
        STURDY_HOE = ITEMS.register("sturdy_hoe", () -> new HoeItem(STURDY, new Item.Properties().fireResistant().attributes(HoeItem.createAttributes(STURDY, -2f, -1f))));
        STURDY_SWORD = ITEMS.register("sturdy_sword", () -> new SwordItem(STURDY, new Item.Properties().fireResistant().attributes(SwordItem.createAttributes(STURDY, 3, -2.4f))));

        STURDY_CHESTPLATE = ITEMS.register("sturdy_chestplate", () -> new ArmorItem(STURDY_ARMOR, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(23))));
        STURDY_HELMET = ITEMS.register("sturdy_helmet", () -> new ArmorItem(STURDY_ARMOR, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(23))));
        STURDY_LEGGINGS = ITEMS.register("sturdy_leggings", () -> new ArmorItem(STURDY_ARMOR, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(23))));
        STURDY_BOOTS = ITEMS.register("sturdy_boots", () -> new ArmorItem(STURDY_ARMOR, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(23))));

        BLAZING_FURNACE_MINECART = ITEMS.register("blazing_furnace_minecart",
                () -> new BlazingFurnaceMinecartItem(
                        new Item.Properties().stacksTo(1)
                ));

        STURDY_TANK = registerBlock("sturdy_tank", () -> new SturdyTankBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).instrument(NoteBlockInstrument.BASEDRUM).instabreak().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY)));
        STURDY_TANK_ITEM = registerItem("sturdy_tank", () -> new SturdyTankItem(new Item.Properties().fireResistant()));

        STONE_STOVE = registerBlockWithSimpleItem("stone_stove", () -> new StoneStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));
        DEEPSLATE_STOVE = registerBlockWithSimpleItem("deepslate_stove", () -> new StoneStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(8.0F, 8.0F).sound(SoundType.DEEPSLATE_BRICKS)));
        RED_NETHER_BRICK_STOVE = registerBlockWithSimpleItem("red_nether_brick_stove", () -> new GoldenStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
        NETHER_BRICK_STOVE = registerBlockWithSimpleItem("nether_brick_stove", () -> new GoldenStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
        BLACKSTONE_STOVE = registerBlockWithSimpleItem("blackstone_stove", () -> new GoldenStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
        BRICK_STOVE = registerBlockWithSimpleItem("brick_stove", () -> new BrickStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));
        MUD_BRICK_STOVE = registerBlockWithSimpleItem("mud_brick_stove", () -> new BrickStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.MUD_BRICKS)));

        BURNABLE_CAMPFIRE = registerBlockWithSimpleItem("burnable_campfire",
                () -> new BurnableCampfireBlock(true, 1, BlockBehaviour.Properties.ofFullCopy(Blocks.CAMPFIRE)));

        BURNABLE_SOUL_CAMPFIRE = registerBlockWithSimpleItem("burnable_soul_campfire",
                () -> new BurnableCampfireBlock(true, 2, BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_CAMPFIRE)));

        COPPER_STOVE = registerBlockWithSimpleItem("copper_stove",
                () -> new WeatheringCopperStoveBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        EXPOSED_COPPER_STOVE = registerBlockWithSimpleItem("exposed_copper_stove",
                () -> new WeatheringCopperStoveBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        WEATHERED_COPPER_STOVE = registerBlockWithSimpleItem("weathered_copper_stove",
                () -> new WeatheringCopperStoveBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        OXIDIZED_COPPER_STOVE = registerBlockWithSimpleItem("oxidized_copper_stove",
                () -> new WeatheringCopperStoveBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        WAXED_COPPER_STOVE = registerBlockWithSimpleItem("waxed_copper_stove",
                () -> new CopperStoveBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        WAXED_EXPOSED_COPPER_STOVE = registerBlockWithSimpleItem("waxed_exposed_copper_stove",
                () -> new CopperStoveBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        WAXED_WEATHERED_COPPER_STOVE = registerBlockWithSimpleItem("waxed_weathered_copper_stove",
                () -> new CopperStoveBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_STEM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        WAXED_OXIDIZED_COPPER_STOVE = registerBlockWithSimpleItem("waxed_oxidized_copper_stove",
                () -> new CopperStoveBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_NYLIUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

        CLAY_CAULDRON = registerBlockWithSimpleItem("clay_cauldron", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).instrument(NoteBlockInstrument.BASEDRUM).strength(1.2F).sound(SoundType.STONE).noOcclusion()));

        MUD_BRICK_POT = registerBlockWithSimpleItem("mud_brick_pot", () -> new ModCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).strength(0.8F).sound(SoundType.MUD_BRICKS).noOcclusion(), AllBlockEntity.MUD_BRICK_POT, 1));
        BRICK_CAULDRON = registerBlockWithSimpleItem("brick_cauldron", () -> new ModCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion(), AllBlockEntity.BRICK_CAULDRON, 2));
        IRON_CAULDRON = registerBlockWithSimpleItem("iron_cauldron", () -> new ModCauldronBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON), AllBlockEntity.IRON_CAULDRON, 3));
        GOLDEN_CAULDRON = registerBlockWithSimpleItem("golden_cauldron", () -> new ModCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BELL).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.METAL).noOcclusion(), AllBlockEntity.GOLDEN_CAULDRON, 4));

        BLAZING_FURNACE = registerBlockWithSimpleItem("blazing_furnace", ()->new BlazingFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

        GOLD_BARS = registerBlockWithSimpleItem("gold_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(10.0F, 5.0F).sound(SoundType.METAL).noOcclusion()));

        COAL_BIT = registerSimpleItem("coal_bit");
        CHARCOAL_BIT = registerSimpleItem("charcoal_bit");

        BurntWoodSet burntOak = registerBurntWoodSet("oak", "burnt");
        BurntWoodSet burntSpruce = registerBurntWoodSet("spruce", "burnt_spruce");
        BurntWoodSet burntBirch = registerBurntWoodSet("birch", "burnt_birch");
        BurntWoodSet burntJungle = registerBurntWoodSet("jungle", "burnt_jungle");
        BurntWoodSet burntAcacia = registerBurntWoodSet("acacia", "burnt_acacia");
        BurntWoodSet burntDarkOak = registerBurntWoodSet("dark_oak", "burnt_dark_oak");
        BurntWoodSet burntMangrove = registerBurntWoodSet("mangrove", "burnt_mangrove");
        BurntWoodSet burntCherry = registerBurntWoodSet("cherry", "burnt_cherry");

        BURNT_LOG = burntOak.log();
        BURNT_WOOD = burntOak.wood();
        BURNT_SPRUCE_LOG = burntSpruce.log();
        BURNT_SPRUCE_WOOD = burntSpruce.wood();
        BURNT_BIRCH_LOG = burntBirch.log();
        BURNT_BIRCH_WOOD = burntBirch.wood();
        BURNT_JUNGLE_LOG = burntJungle.log();
        BURNT_JUNGLE_WOOD = burntJungle.wood();
        BURNT_ACACIA_LOG = burntAcacia.log();
        BURNT_ACACIA_WOOD = burntAcacia.wood();
        BURNT_DARK_OAK_LOG = burntDarkOak.log();
        BURNT_DARK_OAK_WOOD = burntDarkOak.wood();
        BURNT_CHERRY_LOG = burntCherry.log();
        BURNT_CHERRY_WOOD = burntCherry.wood();
        BURNT_MANGROVE_LOG = burntMangrove.log();
        BURNT_MANGROVE_WOOD = burntMangrove.wood();

        BURNT_PLANKS = burntOak.planks();
        BURNT_STAIRS = burntOak.stairs();
        BURNT_SLAB = burntOak.slab();
        BURNT_FENCE = burntOak.fence();
        BURNT_FENCE_GATE = burntOak.fenceGate();
        BURNT_TRAPDOOR = burntOak.trapdoor();
        BURNT_BUTTON = burntOak.button();
        BURNT_PRESSURE_PLATE = burntOak.pressurePlate();
        BURNT_DOOR = burntOak.door();
        BURNT_WOOD_SETS = List.of(burntOak, burntSpruce, burntBirch, burntJungle, burntAcacia, burntDarkOak, burntMangrove, burntCherry);

        BURNT_BOAT = ITEMS.register("burnt_boat", () ->
                new BoatItem(false, Boat.Type.valueOf("CENTRAL_HEATER_BURNT"), new Item.Properties().stacksTo(1)));

        BURNT_CHEST_BOAT = ITEMS.register("burnt_chest_boat", () ->
                new BoatItem(true, Boat.Type.valueOf("CENTRAL_HEATER_BURNT"), new Item.Properties().stacksTo(1)));

        STONE_BRICK_TILE = registerBlockWithSimpleItem("stone_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 8F)));
        STONE_BRICK_TILE_STAIR = registerBlockWithSimpleItem("stone_brick_tile_stair", () -> new StairBlock(STONE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(STONE_BRICK_TILE.get())));
        STONE_BRICK_TILE_SLAB = registerBlockWithSimpleItem("stone_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(STONE_BRICK_TILE.get())));
        STONE_BRICK_TILE_WALL = registerBlockWithSimpleItem("stone_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(STONE_BRICK_TILE.get())));

        DEEPSLATE_BRICK_TILE = registerBlockWithSimpleItem("deepslate_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4F, 8F).sound(SoundType.DEEPSLATE_BRICKS)));
        DEEPSLATE_BRICK_TILE_STAIR = registerBlockWithSimpleItem("deepslate_brick_tile_stair", () -> new StairBlock(DEEPSLATE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DEEPSLATE_BRICK_TILE.get())));
        DEEPSLATE_BRICK_TILE_SLAB = registerBlockWithSimpleItem("deepslate_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DEEPSLATE_BRICK_TILE.get())));
        DEEPSLATE_BRICK_TILE_WALL = registerBlockWithSimpleItem("deepslate_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(DEEPSLATE_BRICK_TILE.get())));

        MUD_BRICK_TILE = registerBlockWithSimpleItem("mud_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));
        MUD_BRICK_TILE_STAIR = registerBlockWithSimpleItem("mud_brick_tile_stair", () -> new StairBlock(MUD_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MUD_BRICK_TILE.get())));
        MUD_BRICK_TILE_SLAB = registerBlockWithSimpleItem("mud_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MUD_BRICK_TILE.get())));
        MUD_BRICK_TILE_WALL = registerBlockWithSimpleItem("mud_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(MUD_BRICK_TILE.get())));

        STURDY_BRICK_TILE = registerBlockWithItem("sturdy_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 10F).sound(SoundType.DEEPSLATE_BRICKS)), fireResistant());
        STURDY_BRICK_TILE_STAIR = registerBlockWithItem("sturdy_brick_tile_stair", () -> new StairBlock(STURDY_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(STURDY_BRICK_TILE.get())), fireResistant());
        STURDY_BRICK_TILE_SLAB = registerBlockWithItem("sturdy_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(STURDY_BRICK_TILE.get())), fireResistant());
        STURDY_BRICK_TILE_WALL = registerBlockWithItem("sturdy_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(STURDY_BRICK_TILE.get())), fireResistant());

        BLACKSTONE_BRICK_TILE = registerBlockWithSimpleItem("blackstone_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));
        BLACKSTONE_BRICK_TILE_STAIR = registerBlockWithSimpleItem("blackstone_brick_tile_stair", () -> new StairBlock(BLACKSTONE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BLACKSTONE_BRICK_TILE.get())));
        BLACKSTONE_BRICK_TILE_SLAB = registerBlockWithSimpleItem("blackstone_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSTONE_BRICK_TILE.get())));
        BLACKSTONE_BRICK_TILE_WALL = registerBlockWithSimpleItem("blackstone_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSTONE_BRICK_TILE.get())));

        STURDY_BRICKS = registerBlockWithItem("sturdy_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 10F).sound(SoundType.DEEPSLATE_BRICKS)), fireResistant());
        STURDY_BRICK_STAIR = registerBlockWithItem("sturdy_brick_stair", () -> new StairBlock(STURDY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(STURDY_BRICKS.get())), fireResistant());
        STURDY_BRICK_SLAB = registerBlockWithItem("sturdy_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(STURDY_BRICKS.get())), fireResistant());

        BRIQUETTES = ITEMS.registerSimpleItem("briquettes");
        SCORCHED_BRIQUETTES = ITEMS.registerSimpleItem("scorched_briquettes", new Item.Properties().fireResistant());
        SOAP = ITEMS.register("soap", () -> new SoapItem(new Item.Properties().durability(96).stacksTo(1)));
        STURDY_SHEARS = ITEMS.register("sturdy_shears", () -> new ShearsItem(new Item.Properties().fireResistant().durability(724).component(DataComponents.TOOL, ShearsItem.createToolProperties())){
            @Override
            public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
                return repairCandidate.is(AllBlockItem.STURDY_BRICK.get()) || super.isValidRepairItem(stack, repairCandidate);
            }
        });

        COAL_BRICKS = registerBlockWithSimpleItem("coal_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.STONE)));
        COAL_BRICK_TILE = registerBlockWithSimpleItem("coal_brick_tile", () -> new Block(BlockBehaviour.Properties.ofFullCopy(COAL_BRICKS.get())));
        COAL_BRICK_TILE_STAIR = registerBlockWithSimpleItem("coal_brick_tile_stair", () -> new StairBlock(COAL_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(COAL_BRICK_TILE.get())));
        COAL_BRICK_TILE_SLAB = registerBlockWithSimpleItem("coal_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(COAL_BRICK_TILE.get())));
        COAL_BRICK_TILE_WALL = registerBlockWithSimpleItem("coal_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(COAL_BRICK_TILE.get())));

        GOLDEN_BRICKS = registerBlockWithSimpleItem("golden_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BELL).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
        GOLDEN_BRICK_TILE = registerBlockWithSimpleItem("golden_brick_tile", () -> new Block(BlockBehaviour.Properties.ofFullCopy(GOLDEN_BRICKS.get())));
        GOLDEN_BRICK_TILE_STAIR = registerBlockWithSimpleItem("golden_brick_tile_stair", () -> new StairBlock(GOLDEN_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GOLDEN_BRICK_TILE.get())));
        GOLDEN_BRICK_TILE_SLAB = registerBlockWithSimpleItem("golden_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GOLDEN_BRICK_TILE.get())));
        GOLDEN_BRICK_TILE_WALL = registerBlockWithSimpleItem("golden_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(GOLDEN_BRICK_TILE.get())));

//        IRON_BRICKS = registerBlockWithSimpleItem("iron_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
//        IRON_BRICK_TILE = registerBlockWithSimpleItem("iron_brick_tile", () -> new Block(BlockBehaviour.Properties.ofFullCopy(IRON_BRICKS.get())));
//        IRON_BRICK_TILE_STAIR = registerBlockWithSimpleItem("iron_brick_tile_stair", () -> new StairBlock(IRON_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(IRON_BRICK_TILE.get())));
//        IRON_BRICK_TILE_SLAB = registerBlockWithSimpleItem("iron_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(IRON_BRICK_TILE.get())));
//        IRON_BRICK_TILE_WALL = registerBlockWithSimpleItem("iron_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(IRON_BRICK_TILE.get())));

        // --- Netherite Bricks & Tiles ---
        NETHERITE_BRICKS = registerBlockWithItem("netherite_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK)), fireResistant());
        NETHERITE_BRICK_TILE = registerBlockWithItem("netherite_brick_tile", () -> new Block(BlockBehaviour.Properties.ofFullCopy(NETHERITE_BRICKS.get())), fireResistant());
        NETHERITE_BRICK_TILE_STAIR = registerBlockWithItem("netherite_brick_tile_stair", () -> new StairBlock(NETHERITE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(NETHERITE_BRICK_TILE.get())), fireResistant());
        NETHERITE_BRICK_TILE_SLAB = registerBlockWithItem("netherite_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(NETHERITE_BRICK_TILE.get())), fireResistant());
        NETHERITE_BRICK_TILE_WALL = registerBlockWithItem("netherite_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(NETHERITE_BRICK_TILE.get())), fireResistant());

        SCORCHED_BRICKS = registerBlockWithSimpleItem("scorched_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
        SCORCHED_BRICK_TILE = registerBlockWithSimpleItem("scorched_brick_tile", () -> new Block(BlockBehaviour.Properties.ofFullCopy(SCORCHED_BRICKS.get())));
        SCORCHED_BRICK_TILE_STAIR = registerBlockWithSimpleItem("scorched_brick_tile_stair", () -> new StairBlock(SCORCHED_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SCORCHED_BRICK_TILE.get())));
        SCORCHED_BRICK_TILE_SLAB = registerBlockWithSimpleItem("scorched_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SCORCHED_BRICK_TILE.get())));
        SCORCHED_BRICK_TILE_WALL = registerBlockWithSimpleItem("scorched_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SCORCHED_BRICK_TILE.get())));

        COAL_BRICK_STAIR = registerBlockWithSimpleItem("coal_brick_stair", () -> new StairBlock(COAL_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(COAL_BRICKS.get())));
        COAL_BRICK_SLAB = registerBlockWithSimpleItem("coal_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(COAL_BRICKS.get())));

        GOLDEN_BRICK_STAIR = registerBlockWithSimpleItem("golden_brick_stair", () -> new StairBlock(GOLDEN_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GOLDEN_BRICKS.get())));
        GOLDEN_BRICK_SLAB = registerBlockWithSimpleItem("golden_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GOLDEN_BRICKS.get())));

//        IRON_BRICK_STAIR = registerBlockWithSimpleItem("iron_brick_stair", () -> new StairBlock(IRON_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(IRON_BRICKS.get())));
//        IRON_BRICK_SLAB = registerBlockWithSimpleItem("iron_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(IRON_BRICKS.get())));

        NETHERITE_BRICK_STAIR = registerBlockWithItem("netherite_brick_stair", () -> new StairBlock(NETHERITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(NETHERITE_BRICKS.get())), fireResistant());
        NETHERITE_BRICK_SLAB = registerBlockWithItem("netherite_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(NETHERITE_BRICKS.get())), fireResistant());

        SCORCHED_BRICK_STAIR = registerBlockWithSimpleItem("scorched_brick_stair", () -> new StairBlock(SCORCHED_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SCORCHED_BRICKS.get())));
        SCORCHED_BRICK_SLAB = registerBlockWithSimpleItem("scorched_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SCORCHED_BRICKS.get())));

        STURDY_ANVIL = registerBlockWithItem("sturdy_anvil", () -> new SturdyAnvilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.BLOCK)), fireResistant());
        CHIPPED_STURDY_ANVIL = registerBlockWithItem("chipped_sturdy_anvil", () -> new SturdyAnvilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.BLOCK)), fireResistant());
        DAMAGED_STURDY_ANVIL = registerBlockWithItem("damaged_sturdy_anvil", () -> new SturdyAnvilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.BLOCK)), fireResistant());

        ASHTRAY = registerBlockWithItem("ashtray", () -> new AshtrayBlock(BlockBehaviour.Properties.of().noOcclusion()
                .mapColor(MapColor.TERRACOTTA_GREEN)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .instabreak()
                .requiresCorrectToolForDrops()
                .pushReaction(PushReaction.DESTROY)), fireResistant());
    }

    public static UnaryOperator<Item.Properties> fireResistant(){
        return p -> p.fireResistant();
    }

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> supplier){
        return BLOCKS.register(name, supplier);
    }

    public static <T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> supplier){
        return ITEMS.register(name, supplier);
    }

    public static DeferredItem<Item> registerSimpleItem(String key){
        return ITEMS.registerSimpleItem(key);
    }

    public static <T extends Block> DeferredBlock<T> registerBlockWithSimpleItem(String name, Supplier<T> supplier){
        DeferredBlock<T> ret = BLOCKS.register(name, supplier);
        ITEMS.registerSimpleBlockItem(ret);
        return ret;
    }

    private static BurntWoodSet registerBurntWoodSet(String material, String prefix) {
        DeferredBlock<BurntLogBlock> log = registerBlockWithSimpleItem(prefix + "_log", () -> new BurntLogBlock(burntLogProperties()));
        DeferredBlock<RotatedPillarBlock> wood = registerBlockWithSimpleItem(prefix + "_wood", () -> new RotatedPillarBlock(burntLogProperties()));
        DeferredBlock<Block> planks = registerBlockWithSimpleItem(prefix + "_planks", () -> new Block(burntPlankProperties()));
        DeferredBlock<StairBlock> stairs = registerBlockWithSimpleItem(prefix + "_stairs", () -> new StairBlock(planks.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(planks.get())));
        DeferredBlock<SlabBlock> slab = registerBlockWithSimpleItem(prefix + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(planks.get())));
        DeferredBlock<FenceBlock> fence = registerBlockWithSimpleItem(prefix + "_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(planks.get())));
        DeferredBlock<FenceGateBlock> fenceGate = registerBlockWithSimpleItem(prefix + "_fence_gate", () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.ofFullCopy(planks.get())));
        DeferredBlock<TrapDoorBlock> trapdoor = registerBlockWithSimpleItem(prefix + "_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(planks.get()).noOcclusion()));
        DeferredBlock<ButtonBlock> button = registerBlockWithSimpleItem(prefix + "_button", () -> new ButtonBlock(BlockSetType.OAK, 30, BlockBehaviour.Properties.ofFullCopy(planks.get()).noCollission()));
        DeferredBlock<PressurePlateBlock> pressurePlate = registerBlockWithSimpleItem(prefix + "_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(planks.get()).noCollission()));
        DeferredBlock<DoorBlock> door = registerBlockWithSimpleItem(prefix + "_door", () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(planks.get()).noOcclusion()));
        return new BurntWoodSet(material, prefix, log, wood, planks, stairs, slab, fence, fenceGate, trapdoor, button, pressurePlate, door);
    }

    private static BlockBehaviour.Properties burntLogProperties() {
        return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK)
                .instrument(NoteBlockInstrument.BASS).instabreak().sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties burntPlankProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F).sound(SoundType.WOOD);
    }

    public static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> supplierBlock, UnaryOperator<Item.Properties> operator){
        DeferredBlock<T> ret = BLOCKS.register(name, supplierBlock);
        ITEMS.register(name, () -> new BlockItem((Block) ret.get(), operator.apply(new Item.Properties())));
        return ret;
    }

    public static void register(IEventBus modEventbus){
        ITEMS.register(modEventbus);
        BLOCKS.register(modEventbus);
        ARMOR_MATERIALS.register(modEventbus);
        DEFAULT_ITEMS.register(modEventbus);
        DEFAULT_BLOCKS.register(modEventbus);
    }
}
