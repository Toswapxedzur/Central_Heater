package com.minecart.central_heater;

import com.minecart.central_heater.block.*;
import com.minecart.central_heater.item.BrickItem;
import com.minecart.central_heater.item.SturdyTankItem;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.*;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class AllBlockItem {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Central_heater.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Central_heater.MODID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Central_heater.MODID);
    public static final DeferredRegister.Items DEFAULT_ITEMS = DeferredRegister.createItems("minecraft");
    public static final DeferredRegister.Blocks DEFAULT_BLOCKS = DeferredRegister.createBlocks("minecraft");

    public static final DeferredBlock<Block> stone_stove = registerBlockWithSimpleItem("stone_stove", ()->new StoneStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

    public static final DeferredBlock<Block> deepslate_stove = registerBlockWithSimpleItem("deepslate_stove", ()->new StoneStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(8.0F, 8.0F).sound(SoundType.DEEPSLATE_BRICKS)));

    public static final DeferredBlock<Block> red_nether_brick_stove = registerBlockWithSimpleItem("red_nether_brick_stove", ()->new GoldenStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static final DeferredBlock<Block> nether_brick_stove = registerBlockWithSimpleItem("nether_brick_stove", ()->new GoldenStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static final DeferredBlock<Block> blackstone_stove = registerBlockWithSimpleItem("blackstone_stove", ()->new GoldenStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static final DeferredBlock<Block> brick_stove = registerBlockWithSimpleItem("brick_stove", ()->new BrickStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

    public static final DeferredBlock<Block> mud_brick_stove = registerBlockWithSimpleItem("mud_brick_stove", ()->new BrickStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.MUD_BRICKS)));

    public static final DeferredBlock<PotBlock> mud_brick_pot = registerBlockWithSimpleItem("mud_brick_pot", ()->new BrickPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUD_BRICKS)));

    public static final DeferredBlock<PotBlock> stone_pot = registerBlockWithSimpleItem("stone_pot", ()->new StonePotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));

    public static final DeferredBlock<PotBlock> deepslate_pot = registerBlockWithSimpleItem("deepslate_pot", ()->new StonePotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS)));

    public static final DeferredBlock<PotBlock> cauldron = registerBlockWithSimpleItem("cauldron", ()->new IronCauldronBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)));

    public static final DeferredBlock<PotBlock> golden_cauldron = registerBlockWithSimpleItem("golden_cauldron", ()->new GoldenCauldronBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)));

    public static final DeferredBlock<SturdyTankBlock> sturdy_tank = registerBlock("sturdy_tank", ()->new SturdyTankBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO)));

    public static final DeferredItem<SturdyTankItem> sturdy_tank_item = registerItem("sturdy_tank", ()->new SturdyTankItem(new Item.Properties().fireResistant()));




    public static final DeferredBlock<Block> stone_brick_tile = registerBlockWithSimpleItem("stone_brick_tile", ()->new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 8F)));

    public static final DeferredBlock<StairBlock> stone_brick_tile_stair = registerBlockWithSimpleItem("stone_brick_tile_stair", ()->new StairBlock(stone_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));
    public static final DeferredBlock<SlabBlock> stone_brick_tile_slab = registerBlockWithSimpleItem("stone_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));
    public static final DeferredBlock<WallBlock> stone_brick_tile_wall = registerBlockWithSimpleItem("stone_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));

    public static final DeferredBlock<Block> deepslate_brick_tile = registerBlockWithSimpleItem("deepslate_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4F, 8F).sound(SoundType.DEEPSLATE_BRICKS)));

    public static final DeferredBlock<StairBlock> deepslate_brick_tile_stair = registerBlockWithSimpleItem("deepslate_brick_tile_stair", ()->new StairBlock(deepslate_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));
    public static final DeferredBlock<SlabBlock> deepslate_brick_tile_slab = registerBlockWithSimpleItem("deepslate_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));
    public static final DeferredBlock<WallBlock> deepslate_brick_tile_wall = registerBlockWithSimpleItem("deepslate_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));

    public static final DeferredBlock<Block> mud_brick_tile = registerBlockWithSimpleItem("mud_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));

    public static final DeferredBlock<StairBlock> mud_brick_tile_stair = registerBlockWithSimpleItem("mud_brick_tile_stair", ()->new StairBlock(mud_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(mud_brick_tile.get())));
    public static final DeferredBlock<SlabBlock> mud_brick_tile_slab = registerBlockWithSimpleItem("mud_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(mud_brick_tile.get())));
    public static final DeferredBlock<WallBlock> mud_brick_tile_wall = registerBlockWithSimpleItem("mud_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(mud_brick_tile.get())));

    public static final DeferredBlock<Block> sturdy_brick_tile = registerBlockWithSimpleItem("sturdy_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 10F).sound(SoundType.DEEPSLATE_BRICKS)));

    public static final DeferredBlock<StairBlock> sturdy_brick_tile_stair = registerBlockWithSimpleItem("sturdy_brick_tile_stair", ()->new StairBlock(sturdy_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(sturdy_brick_tile.get())));
    public static final DeferredBlock<SlabBlock> sturdy_brick_tile_slab = registerBlockWithSimpleItem("sturdy_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(sturdy_brick_tile.get())));
    public static final DeferredBlock<WallBlock> sturdy_brick_tile_wall = registerBlockWithSimpleItem("sturdy_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(sturdy_brick_tile.get())));

    public static final DeferredBlock<Block> blackstone_brick_tile = registerBlockWithSimpleItem("blackstone_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));

    public static final DeferredBlock<StairBlock> blackstone_brick_tile_stair = registerBlockWithSimpleItem("blackstone_brick_tile_stair", ()->new StairBlock(blackstone_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(blackstone_brick_tile.get())));
    public static final DeferredBlock<SlabBlock> blackstone_brick_tile_slab = registerBlockWithSimpleItem("blackstone_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(blackstone_brick_tile.get())));
    public static final DeferredBlock<WallBlock> blackstone_brick_tile_wall = registerBlockWithSimpleItem("blackstone_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(blackstone_brick_tile.get())));

    public static final DeferredBlock<Block> gold_bars = registerBlockWithSimpleItem("gold_bars", ()->new IronBarsBlock(
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(10.0F, 5.0F).sound(SoundType.METAL).noOcclusion()));


    public static final DeferredItem<Item> stone_brick = ITEMS.register("stone_brick", ()->new BrickItem(new Item.Properties()));

    public static final DeferredItem<Item> deepslate_brick = ITEMS.register("deepslate_brick", ()->new BrickItem(new Item.Properties()));

    public static final DeferredItem<Item> mud_brick = ITEMS.register("mud_brick", ()->new BrickItem(new Item.Properties()));

    public static final DeferredItem<Item> red_nether_brick = ITEMS.register("red_nether_brick", ()->new BrickItem(new Item.Properties()));

    public static final DeferredItem<Item> cobble = ITEMS.registerSimpleItem("cobble");

    public static final DeferredItem<Item> deepslate_cobble = ITEMS.registerSimpleItem("deepslate_cobble");

    public static final DeferredItem<Item> diamond_shard = ITEMS.registerSimpleItem("diamond_shard");

    public static final DeferredItem<Item> blackstone_brick = ITEMS.registerSimpleItem("blackstone_brick");

    public static final DeferredItem<Item> sturdy_brick = ITEMS.registerSimpleItem("sturdy_brick", new Item.Properties().fireResistant());

    public static final DeferredItem<Item> sturdy_nugget = ITEMS.registerSimpleItem("sturdy_nugget", new Item.Properties().fireResistant());

    public static final FoodProperties burnt_food = new FoodProperties.Builder().nutrition(2).effect(new MobEffectInstance(MobEffects.HUNGER, 300), 0.6f).effect(new MobEffectInstance(MobEffects.POISON, 120), 0.4f).build();

    public static final DeferredItem<Item> burnt_beef = ITEMS.registerSimpleItem("burnt_beef", new Item.Properties().food(burnt_food));

    public static final DeferredItem<Item> burnt_chicken = ITEMS.registerSimpleItem("burnt_chicken", new Item.Properties().food(burnt_food));

    public static final DeferredItem<Item> burnt_cod = ITEMS.registerSimpleItem("burnt_cod", new Item.Properties().food(burnt_food));

    public static final DeferredItem<Item> burnt_mutton = ITEMS.registerSimpleItem("burnt_mutton", new Item.Properties().food(burnt_food));

    public static final DeferredItem<Item> burnt_porkchop = ITEMS.registerSimpleItem("burnt_porkchop", new Item.Properties().food(burnt_food));

    public static final DeferredItem<Item> burnt_potato = ITEMS.registerSimpleItem("burnt_potato", new Item.Properties().food(burnt_food));

    public static final DeferredItem<Item> burnt_rabbit = ITEMS.registerSimpleItem("burnt_rabbit", new Item.Properties().food(burnt_food));

    public static final DeferredItem<Item> burnt_salmon = ITEMS.registerSimpleItem("burnt_salmon", new Item.Properties().food(burnt_food));

    public static final Tier STURDY = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 713, 8f, 3f, 2, ()-> Ingredient.of(sturdy_brick.asItem()));

    public static final Holder<ArmorMaterial> STURDY_ARMOR = ARMOR_MATERIALS.register("sturdy", ()->new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323380_ -> {
                p_323380_.put(ArmorItem.Type.BOOTS, 3);
                p_323380_.put(ArmorItem.Type.LEGGINGS, 6);
                p_323380_.put(ArmorItem.Type.CHESTPLATE, 8);
                p_323380_.put(ArmorItem.Type.HELMET, 3);
                p_323380_.put(ArmorItem.Type.BODY, 11);
            }), 2, SoundEvents.ARMOR_EQUIP_NETHERITE, ()->Ingredient.of(sturdy_brick.asItem()),
            List.of(new ArmorMaterial.Layer(Central_heater.modLoc("sturdy"))), 1f, 0.05f
    ));

    public static final DeferredItem<PickaxeItem> sturdy_pickaxe = ITEMS.register("sturdy_pickaxe", ()->new PickaxeItem(STURDY, new Item.Properties().fireResistant()
            .attributes(PickaxeItem.createAttributes(STURDY, 1, -2.8f))));

    public static final DeferredItem<AxeItem> sturdy_axe = ITEMS.register("sturdy_axe", ()->new AxeItem(STURDY, new Item.Properties().fireResistant()
            .attributes(AxeItem.createAttributes(STURDY, 7f, -3.4f))));

    public static final DeferredItem<ShovelItem> sturdy_shovel = ITEMS.register("sturdy_shovel", ()->new ShovelItem(STURDY, new Item.Properties().fireResistant()
            .attributes(ShovelItem.createAttributes(STURDY, 1.5f, -3f))));

    public static final DeferredItem<HoeItem> sturdy_hoe = ITEMS.register("sturdy_hoe", ()->new HoeItem(STURDY, new Item.Properties().fireResistant()
            .attributes(HoeItem.createAttributes(STURDY, -2f, -1f))));

    public static final DeferredItem<SwordItem> sturdy_sword = ITEMS.register("sturdy_sword", ()->new SwordItem(STURDY, new Item.Properties().fireResistant()
            .attributes(SwordItem.createAttributes(STURDY, 3, -2.4f))));

    public static final DeferredItem<ArmorItem> sturdy_chestplate = ITEMS.register("sturdy_chestplate", ()->new ArmorItem(STURDY_ARMOR, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(23))));

    public static final DeferredItem<ArmorItem> sturdy_helmet = ITEMS.register("sturdy_helmet", ()->new ArmorItem(STURDY_ARMOR, ArmorItem.Type.HELMET,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(23))));

    public static final DeferredItem<ArmorItem> sturdy_leggings = ITEMS.register("sturdy_leggings", ()->new ArmorItem(STURDY_ARMOR, ArmorItem.Type.LEGGINGS,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(23))));

    public static final DeferredItem<ArmorItem> sturdy_boots = ITEMS.register("sturdy_boots", ()->new ArmorItem(STURDY_ARMOR, ArmorItem.Type.BOOTS,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(23))));


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
