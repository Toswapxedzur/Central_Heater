package com.minecart.central_heater;

import com.minecart.central_heater.block.cauldron.*;
import com.minecart.central_heater.block.cauldron.CauldronBlock;
import com.minecart.central_heater.block.misc.BlazingFurnaceBlock;
import com.minecart.central_heater.block.misc.BurntLogBlock;
import com.minecart.central_heater.block.misc.SturdyTankBlock;
import com.minecart.central_heater.block.stove.BrickStoveBlock;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.block.stove.StoneStoveBlock;
import com.minecart.central_heater.item.complex_items.BlazingFurnaceMinecartItem;
import com.minecart.central_heater.item.complex_items.BrickItem;
import com.minecart.central_heater.item.complex_items.PebbleItem;
import com.minecart.central_heater.item.complex_items.SturdyTankItem;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.*;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

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
    public static final DeferredBlock<CauldronBlock> BRICK_CAULDRON;
    public static final DeferredBlock<Block> BRICK_STOVE;
    public static final DeferredBlock<BurntLogBlock> BURNT_LOG;
    public static final DeferredBlock<RotatedPillarBlock> BURNT_WOOD;
    public static final DeferredBlock<Block> CLAY_CAULDRON;
    public static final DeferredBlock<Block> DEEPSLATE_BRICK_TILE;
    public static final DeferredBlock<SlabBlock> DEEPSLATE_BRICK_TILE_SLAB;
    public static final DeferredBlock<StairBlock> DEEPSLATE_BRICK_TILE_STAIR;
    public static final DeferredBlock<WallBlock> DEEPSLATE_BRICK_TILE_WALL;
    public static final DeferredBlock<Block> DEEPSLATE_STOVE;
    public static final DeferredBlock<Block> GOLD_BARS;
    public static final DeferredBlock<CauldronBlock> GOLDEN_CAULDRON;
    public static final DeferredBlock<CauldronBlock> IRON_CAULDRON;
    public static final DeferredBlock<CauldronBlock> MUD_BRICK_POT;
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
        STURDY_AXE = ITEMS.register("sturdy_axe", () -> new AxeItem(STURDY, new Item.Properties().fireResistant().attributes(AxeItem.createAttributes(STURDY, 7f, -3.4f))));
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

        MUD_BRICK_POT = registerBlockWithSimpleItem("mud_brick_pot", () -> new BrickPotBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).strength(0.8F).sound(SoundType.MUD_BRICKS).noOcclusion()));
        CLAY_CAULDRON = registerBlockWithSimpleItem("clay_cauldron", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).instrument(NoteBlockInstrument.BASEDRUM).strength(1.2F).sound(SoundType.STONE).noOcclusion()));
        BRICK_CAULDRON = registerBlockWithSimpleItem("brick_cauldron", () -> new BrickCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).sound(SoundType.STONE).noOcclusion()));
        IRON_CAULDRON = registerBlockWithSimpleItem("iron_cauldron", () -> new IronCauldronBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)));
        GOLDEN_CAULDRON = registerBlockWithSimpleItem("golden_cauldron", () -> new GoldenCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BELL).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.METAL).noOcclusion()));

        BLAZING_FURNACE = registerBlockWithSimpleItem("blazing_furnace", ()->new BlazingFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

        GOLD_BARS = registerBlockWithSimpleItem("gold_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(10.0F, 5.0F).sound(SoundType.METAL).noOcclusion()));

        BURNT_LOG = registerBlockWithSimpleItem("burnt_log", () -> new BurntLogBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS).instabreak().sound(SoundType.WOOD)));
        BURNT_WOOD = registerBlockWithSimpleItem("burnt_wood", ()->new RotatedPillarBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS).instabreak().sound(SoundType.WOOD)));

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

        STURDY_BRICK_TILE = registerBlockWithSimpleItem("sturdy_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 10F).sound(SoundType.DEEPSLATE_BRICKS)));
        STURDY_BRICK_TILE_STAIR = registerBlockWithSimpleItem("sturdy_brick_tile_stair", () -> new StairBlock(STURDY_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(STURDY_BRICK_TILE.get())));
        STURDY_BRICK_TILE_SLAB = registerBlockWithSimpleItem("sturdy_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(STURDY_BRICK_TILE.get())));
        STURDY_BRICK_TILE_WALL = registerBlockWithSimpleItem("sturdy_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(STURDY_BRICK_TILE.get())));

        BLACKSTONE_BRICK_TILE = registerBlockWithSimpleItem("blackstone_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));
        BLACKSTONE_BRICK_TILE_STAIR = registerBlockWithSimpleItem("blackstone_brick_tile_stair", () -> new StairBlock(BLACKSTONE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BLACKSTONE_BRICK_TILE.get())));
        BLACKSTONE_BRICK_TILE_SLAB = registerBlockWithSimpleItem("blackstone_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSTONE_BRICK_TILE.get())));
        BLACKSTONE_BRICK_TILE_WALL = registerBlockWithSimpleItem("blackstone_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(BLACKSTONE_BRICK_TILE.get())));
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

    public static void register(IEventBus modEventbus){
        ITEMS.register(modEventbus);
        BLOCKS.register(modEventbus);
        ARMOR_MATERIALS.register(modEventbus);
        DEFAULT_ITEMS.register(modEventbus);
        DEFAULT_BLOCKS.register(modEventbus);
    }
}
