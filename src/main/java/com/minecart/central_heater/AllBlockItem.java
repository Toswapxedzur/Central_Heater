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
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class AllBlockItem {
    public static final Float DEFAULT_FIRE_ASH_DROP_CHANCE = 0.2f;
    public static final Float DEFAULT_SCORCHED_DUST_DROP_CHANCE = 0.1f;

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CentralHeater.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CentralHeater.MODID);

    // Armor Material Implementation for 1.20.1
    public static final ArmorMaterial STURDY_ARMOR_MAT = new SturdyArmorMaterial();
    public static final Tier STURDY;

    public static final FoodProperties BURNT_FOOD;

    // Items
    public static final RegistryObject<Item> BLACKSTONE_BRICK;
    public static final RegistryObject<BlazingFurnaceMinecartItem> BLAZING_FURNACE_MINECART;
    public static final RegistryObject<Item> BURNT_BEEF;
    public static final RegistryObject<Item> BURNT_CHICKEN;
    public static final RegistryObject<Item> BURNT_COD;
    public static final RegistryObject<Item> BURNT_MUTTON;
    public static final RegistryObject<Item> BURNT_PORKCHOP;
    public static final RegistryObject<Item> BURNT_RABBIT;
    public static final RegistryObject<Item> BURNT_SALMON;
    public static final RegistryObject<Item> CLAY_BIT;
    public static final RegistryObject<Item> CLAY_BRICK;
    public static final RegistryObject<Item> COBBLE;
    public static final RegistryObject<Item> DEEPSLATE_BRICK;
    public static final RegistryObject<Item> DEEPSLATE_COBBLE;
    public static final RegistryObject<Item> DIAMOND_SHARD;
    public static final RegistryObject<Item> FIRE_ASH;
    public static final RegistryObject<Item> MUD_BRICK;
    public static final RegistryObject<Item> RED_NETHER_BRICK;
    public static final RegistryObject<Item> SCORCHED_COAL;
    public static final RegistryObject<Item> SCORCHED_DUST;
    public static final RegistryObject<Item> SOUL_MIXTURE;
    public static final RegistryObject<Item> STONE_BRICK;
    public static final RegistryObject<AxeItem> STURDY_AXE;
    public static final RegistryObject<ArmorItem> STURDY_BOOTS;
    public static final RegistryObject<Item> STURDY_BRICK;
    public static final RegistryObject<ArmorItem> STURDY_CHESTPLATE;
    public static final RegistryObject<ArmorItem> STURDY_HELMET;
    public static final RegistryObject<HoeItem> STURDY_HOE;
    public static final RegistryObject<ArmorItem> STURDY_LEGGINGS;
    public static final RegistryObject<Item> STURDY_NUGGET;
    public static final RegistryObject<PickaxeItem> STURDY_PICKAXE;
    public static final RegistryObject<ShovelItem> STURDY_SHOVEL;
    public static final RegistryObject<SwordItem> STURDY_SWORD;
    public static final RegistryObject<SturdyTankItem> STURDY_TANK_ITEM;
    public static final RegistryObject<Item> WHEAT_DOUGH;
    public static final RegistryObject<Item> WHEAT_FLOUR;
    public static final RegistryObject<Item> WOOD_CHIPS;

    // Blocks
    public static final RegistryObject<Block> BLACKSTONE_BRICK_TILE;
    public static final RegistryObject<SlabBlock> BLACKSTONE_BRICK_TILE_SLAB;
    public static final RegistryObject<StairBlock> BLACKSTONE_BRICK_TILE_STAIR;
    public static final RegistryObject<WallBlock> BLACKSTONE_BRICK_TILE_WALL;
    public static final RegistryObject<Block> BLACKSTONE_STOVE;
    public static final RegistryObject<BlazingFurnaceBlock> BLAZING_FURNACE;
    public static final RegistryObject<CauldronBlock> BRICK_CAULDRON;
    public static final RegistryObject<Block> BRICK_STOVE;
    public static final RegistryObject<BurntLogBlock> BURNT_LOG;
    public static final RegistryObject<RotatedPillarBlock> BURNT_WOOD;
    public static final RegistryObject<Block> CLAY_CAULDRON;
    public static final RegistryObject<Block> DEEPSLATE_BRICK_TILE;
    public static final RegistryObject<SlabBlock> DEEPSLATE_BRICK_TILE_SLAB;
    public static final RegistryObject<StairBlock> DEEPSLATE_BRICK_TILE_STAIR;
    public static final RegistryObject<WallBlock> DEEPSLATE_BRICK_TILE_WALL;
    public static final RegistryObject<Block> DEEPSLATE_STOVE;
    public static final RegistryObject<Block> GOLD_BARS;
    public static final RegistryObject<CauldronBlock> GOLDEN_CAULDRON;
    public static final RegistryObject<CauldronBlock> IRON_CAULDRON;
    public static final RegistryObject<CauldronBlock> MUD_BRICK_POT;
    public static final RegistryObject<Block> MUD_BRICK_STOVE;
    public static final RegistryObject<Block> MUD_BRICK_TILE;
    public static final RegistryObject<SlabBlock> MUD_BRICK_TILE_SLAB;
    public static final RegistryObject<StairBlock> MUD_BRICK_TILE_STAIR;
    public static final RegistryObject<WallBlock> MUD_BRICK_TILE_WALL;
    public static final RegistryObject<Block> NETHER_BRICK_STOVE;
    public static final RegistryObject<Block> RED_NETHER_BRICK_STOVE;
    public static final RegistryObject<Block> STONE_BRICK_TILE;
    public static final RegistryObject<SlabBlock> STONE_BRICK_TILE_SLAB;
    public static final RegistryObject<StairBlock> STONE_BRICK_TILE_STAIR;
    public static final RegistryObject<WallBlock> STONE_BRICK_TILE_WALL;
    public static final RegistryObject<Block> STONE_STOVE;
    public static final RegistryObject<Block> STURDY_BRICK_TILE;
    public static final RegistryObject<SlabBlock> STURDY_BRICK_TILE_SLAB;
    public static final RegistryObject<StairBlock> STURDY_BRICK_TILE_STAIR;
    public static final RegistryObject<WallBlock> STURDY_BRICK_TILE_WALL;
    public static final RegistryObject<SturdyTankBlock> STURDY_TANK;

    static {
        // Items
        STONE_BRICK = ITEMS.register("stone_brick", () -> new BrickItem(new Item.Properties()));
        DEEPSLATE_BRICK = ITEMS.register("deepslate_brick", () -> new BrickItem(new Item.Properties()));
        MUD_BRICK = ITEMS.register("mud_brick", () -> new BrickItem(new Item.Properties()));
        RED_NETHER_BRICK = ITEMS.register("red_nether_brick", () -> new BrickItem(new Item.Properties()));
        COBBLE = ITEMS.register("cobble", () -> new PebbleItem(new Item.Properties()));
        DEEPSLATE_COBBLE = ITEMS.register("deepslate_cobble", () -> new PebbleItem(new Item.Properties()));

        DIAMOND_SHARD = registerSimpleItem("diamond_shard");
        BLACKSTONE_BRICK = registerSimpleItem("blackstone_brick");
        STURDY_BRICK = registerSimpleItem("sturdy_brick", new Item.Properties().fireResistant());
        STURDY_NUGGET = registerSimpleItem("sturdy_nugget", new Item.Properties().fireResistant());
        SCORCHED_COAL = registerSimpleItem("scorched_coal");
        SCORCHED_DUST = registerSimpleItem("scorched_dust");
        FIRE_ASH = registerSimpleItem("fire_ash");
        CLAY_BIT = registerSimpleItem("clay_bit");
        CLAY_BRICK = registerSimpleItem("clay_brick");
        SOUL_MIXTURE = registerSimpleItem("soul_mixture");
        WHEAT_DOUGH = registerSimpleItem("wheat_dough");
        WHEAT_FLOUR = registerSimpleItem("wheat_flour");
        WOOD_CHIPS = registerSimpleItem("wood_chips");

        BURNT_FOOD = new FoodProperties.Builder().nutrition(2).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300), 0.6f).effect(() -> new MobEffectInstance(MobEffects.POISON, 120), 0.4f).build();
        BURNT_BEEF = ITEMS.register("burnt_beef", () -> new Item(new Item.Properties().food(BURNT_FOOD)));
        BURNT_CHICKEN = ITEMS.register("burnt_chicken", () -> new Item(new Item.Properties().food(BURNT_FOOD)));
        BURNT_COD = ITEMS.register("burnt_cod", () -> new Item(new Item.Properties().food(BURNT_FOOD)));
        BURNT_MUTTON = ITEMS.register("burnt_mutton", () -> new Item(new Item.Properties().food(BURNT_FOOD)));
        BURNT_PORKCHOP = ITEMS.register("burnt_porkchop", () -> new Item(new Item.Properties().food(BURNT_FOOD)));
        BURNT_RABBIT = ITEMS.register("burnt_rabbit", () -> new Item(new Item.Properties().food(BURNT_FOOD)));
        BURNT_SALMON = ITEMS.register("burnt_salmon", () -> new Item(new Item.Properties().food(BURNT_FOOD)));

        // Tier & Tools
        STURDY = new ForgeTier(2, 713, 8f, 3f, 11, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(STURDY_BRICK.get()));

        STURDY_PICKAXE = ITEMS.register("sturdy_pickaxe", () -> new PickaxeItem(STURDY, 1, -2.8f, new Item.Properties().fireResistant()));
        STURDY_AXE = ITEMS.register("sturdy_axe", () -> new AxeItem(STURDY, 7f, -3.4f, new Item.Properties().fireResistant()));
        STURDY_SHOVEL = ITEMS.register("sturdy_shovel", () -> new ShovelItem(STURDY, 1.5f, -3f, new Item.Properties().fireResistant()));
        STURDY_HOE = ITEMS.register("sturdy_hoe", () -> new HoeItem(STURDY, -2, -1f, new Item.Properties().fireResistant()));
        STURDY_SWORD = ITEMS.register("sturdy_sword", () -> new SwordItem(STURDY, 3, -2.4f, new Item.Properties().fireResistant()));

        // Armor
        STURDY_CHESTPLATE = ITEMS.register("sturdy_chestplate", () -> new ArmorItem(STURDY_ARMOR_MAT, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));
        STURDY_HELMET = ITEMS.register("sturdy_helmet", () -> new ArmorItem(STURDY_ARMOR_MAT, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));
        STURDY_LEGGINGS = ITEMS.register("sturdy_leggings", () -> new ArmorItem(STURDY_ARMOR_MAT, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));
        STURDY_BOOTS = ITEMS.register("sturdy_boots", () -> new ArmorItem(STURDY_ARMOR_MAT, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));

        BLAZING_FURNACE_MINECART = ITEMS.register("blazing_furnace_minecart",
                () -> new BlazingFurnaceMinecartItem(new Item.Properties().stacksTo(1)));

        // Blocks
        STURDY_TANK = registerBlock("sturdy_tank", () -> new SturdyTankBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).instrument(NoteBlockInstrument.BASEDRUM).instabreak().requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).noOcclusion()));
        STURDY_TANK_ITEM = ITEMS.register("sturdy_tank", () -> new SturdyTankItem(new Item.Properties().fireResistant()));

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
        IRON_CAULDRON = registerBlockWithSimpleItem("iron_cauldron", () -> new IronCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));
        GOLDEN_CAULDRON = registerBlockWithSimpleItem("golden_cauldron", () -> new GoldenCauldronBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).instrument(NoteBlockInstrument.BELL).requiresCorrectToolForDrops().strength(3.0F).sound(SoundType.METAL).noOcclusion()));

        BLAZING_FURNACE = registerBlockWithSimpleItem("blazing_furnace", () -> new BlazingFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

        GOLD_BARS = registerBlockWithSimpleItem("gold_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(10.0F, 5.0F).sound(SoundType.METAL).noOcclusion()));

        // Added .ignitedByLava() to replace Material.WOOD logic
        BURNT_LOG = registerBlockWithSimpleItem("burnt_log", () -> new BurntLogBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS).ignitedByLava().instabreak().sound(SoundType.WOOD)));
        BURNT_WOOD = registerBlockWithSimpleItem("burnt_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS).ignitedByLava().instabreak().sound(SoundType.WOOD)));

        STONE_BRICK_TILE = registerBlockWithSimpleItem("stone_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 8F)));
        STONE_BRICK_TILE_STAIR = registerBlockWithSimpleItem("stone_brick_tile_stair", () -> new StairBlock(() -> STONE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.copy(STONE_BRICK_TILE.get())));
        STONE_BRICK_TILE_SLAB = registerBlockWithSimpleItem("stone_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(STONE_BRICK_TILE.get())));
        STONE_BRICK_TILE_WALL = registerBlockWithSimpleItem("stone_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(STONE_BRICK_TILE.get())));

        DEEPSLATE_BRICK_TILE = registerBlockWithSimpleItem("deepslate_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4F, 8F).sound(SoundType.DEEPSLATE_BRICKS)));
        DEEPSLATE_BRICK_TILE_STAIR = registerBlockWithSimpleItem("deepslate_brick_tile_stair", () -> new StairBlock(() -> DEEPSLATE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.copy(DEEPSLATE_BRICK_TILE.get())));
        DEEPSLATE_BRICK_TILE_SLAB = registerBlockWithSimpleItem("deepslate_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(DEEPSLATE_BRICK_TILE.get())));
        DEEPSLATE_BRICK_TILE_WALL = registerBlockWithSimpleItem("deepslate_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(DEEPSLATE_BRICK_TILE.get())));

        MUD_BRICK_TILE = registerBlockWithSimpleItem("mud_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));
        MUD_BRICK_TILE_STAIR = registerBlockWithSimpleItem("mud_brick_tile_stair", () -> new StairBlock(() -> MUD_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.copy(MUD_BRICK_TILE.get())));
        MUD_BRICK_TILE_SLAB = registerBlockWithSimpleItem("mud_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MUD_BRICK_TILE.get())));
        MUD_BRICK_TILE_WALL = registerBlockWithSimpleItem("mud_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(MUD_BRICK_TILE.get())));

        STURDY_BRICK_TILE = registerBlockWithSimpleItem("sturdy_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 10F).sound(SoundType.DEEPSLATE_BRICKS)));
        STURDY_BRICK_TILE_STAIR = registerBlockWithSimpleItem("sturdy_brick_tile_stair", () -> new StairBlock(() -> STURDY_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.copy(STURDY_BRICK_TILE.get())));
        STURDY_BRICK_TILE_SLAB = registerBlockWithSimpleItem("sturdy_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(STURDY_BRICK_TILE.get())));
        STURDY_BRICK_TILE_WALL = registerBlockWithSimpleItem("sturdy_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(STURDY_BRICK_TILE.get())));

        BLACKSTONE_BRICK_TILE = registerBlockWithSimpleItem("blackstone_brick_tile", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));
        BLACKSTONE_BRICK_TILE_STAIR = registerBlockWithSimpleItem("blackstone_brick_tile_stair", () -> new StairBlock(() -> BLACKSTONE_BRICK_TILE.get().defaultBlockState(), BlockBehaviour.Properties.copy(BLACKSTONE_BRICK_TILE.get())));
        BLACKSTONE_BRICK_TILE_SLAB = registerBlockWithSimpleItem("blackstone_brick_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(BLACKSTONE_BRICK_TILE.get())));
        BLACKSTONE_BRICK_TILE_WALL = registerBlockWithSimpleItem("blackstone_brick_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(BLACKSTONE_BRICK_TILE.get())));
    }

    // Helper Methods for 1.20.1

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> supplier) {
        return BLOCKS.register(name, supplier);
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier) {
        return ITEMS.register(name, supplier);
    }

    public static RegistryObject<Item> registerSimpleItem(String name) {
        return registerSimpleItem(name, new Item.Properties());
    }

    public static RegistryObject<Item> registerSimpleItem(String name, Item.Properties properties) {
        return ITEMS.register(name, () -> new Item(properties));
    }

    public static <T extends Block> RegistryObject<T> registerBlockWithSimpleItem(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static void register(IEventBus modEventbus) {
        ITEMS.register(modEventbus);
        BLOCKS.register(modEventbus);
        // ARMOR_MATERIALS.register(modEventbus); // Skipped, using custom class
    }

    // Custom Armor Material Implementation
    private static class SturdyArmorMaterial implements ArmorMaterial {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return new int[]{13, 15, 16, 11}[type.ordinal()] * 23;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return new int[]{3, 6, 8, 3}[type.ordinal()];
        }

        @Override
        public int getEnchantmentValue() {
            return 11;
        }

        @Override
        public net.minecraft.sounds.SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(STURDY_BRICK.get());
        }

        @Override
        public String getName() {
            return CentralHeater.MODID + ":sturdy";
        }

        @Override
        public float getToughness() {
            return 1f;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.05f;
        }
    }
}