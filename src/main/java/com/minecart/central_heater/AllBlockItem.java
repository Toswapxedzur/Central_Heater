package com.minecart.central_heater;

import com.minecart.central_heater.block.*;
import com.minecart.central_heater.item.BrickItem;
import com.minecart.central_heater.recipe.SeethingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

public class AllBlockItem {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Central_heater.MODID);
    public static DeferredRegister.Items VITEMS = DeferredRegister.createItems("minecraft");
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Central_heater.MODID);


    public static DeferredBlock<Block> stone_stove = registerBlockWithSimpleItem("stone_stove", ()->new StoneStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

    public static DeferredBlock<Block> deepslate_stove = registerBlockWithSimpleItem("deepslate_stove", ()->new StoneStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(8.0F, 8.0F).sound(SoundType.DEEPSLATE_BRICKS)));

    public static DeferredBlock<Block> red_nether_brick_stove = registerBlockWithSimpleItem("red_nether_brick_stove", ()->new GoldenStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static DeferredBlock<Block> nether_brick_stove = registerBlockWithSimpleItem("nether_brick_stove", ()->new GoldenStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static DeferredBlock<Block> blackstone_stove = registerBlockWithSimpleItem("blackstone_stove", ()->new GoldenStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static DeferredBlock<Block> brick_stove = registerBlockWithSimpleItem("brick_stove", ()->new BrickStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

    public static DeferredBlock<Block> mud_brick_stove = registerBlockWithSimpleItem("mud_brick_stove", ()->new BrickStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.MUD_BRICKS)));


    public static DeferredBlock<LidBlock> iron_lid = registerBlockWithSimpleItem("iron_lid", ()->new LidBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));

    public static DeferredBlock<LidBlock> gold_lid = registerBlockWithSimpleItem("gold_lid", ()->new LidBlock(BlockBehaviour.Properties.of()));

    public static DeferredBlock<PotBlock> brick_pot = registerBlockWithSimpleItem("brick_pot", ()->new BrickPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)));

    public static DeferredBlock<PotBlock> mud_brick_pot = registerBlockWithSimpleItem("mud_brick_pot", ()->new BrickPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUD_BRICKS)));

    public static DeferredBlock<PotBlock> stone_pot = registerBlockWithSimpleItem("stone_pot", ()->new StonePotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));

    public static DeferredBlock<PotBlock> deepslate_pot = registerBlockWithSimpleItem("deepslate_pot", ()->new StonePotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS)));

    public static DeferredBlock<PotBlock> red_nether_brick_pot = registerBlockWithSimpleItem("red_nether_brick_pot", ()->new NetherPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));

    public static DeferredBlock<PotBlock> nether_brick_pot = registerBlockWithSimpleItem("nether_brick_pot", ()->new NetherPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS)));

    public static DeferredBlock<PotBlock> blackstone_pot = registerBlockWithSimpleItem("blackstone_pot", ()->new NetherPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS)));


    public static DeferredBlock<Block> stone_brick_tile = registerBlockWithSimpleItem("stone_brick_tile", ()->new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 8F)));

    public static DeferredBlock<StairBlock> stone_brick_tile_stair = registerBlockWithSimpleItem("stone_brick_tile_stair", ()->new StairBlock(stone_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));
    public static DeferredBlock<SlabBlock> stone_brick_tile_slab = registerBlockWithSimpleItem("stone_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));
    public static DeferredBlock<WallBlock> stone_brick_tile_wall = registerBlockWithSimpleItem("stone_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));

    public static DeferredBlock<Block> deepslate_brick_tile = registerBlockWithSimpleItem("deepslate_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4F, 8F).sound(SoundType.DEEPSLATE_BRICKS)));

    public static DeferredBlock<StairBlock> deepslate_brick_tile_stair = registerBlockWithSimpleItem("deepslate_brick_tile_stair", ()->new StairBlock(deepslate_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));
    public static DeferredBlock<SlabBlock> deepslate_brick_tile_slab = registerBlockWithSimpleItem("deepslate_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));
    public static DeferredBlock<WallBlock> deepslate_brick_tile_wall = registerBlockWithSimpleItem("deepslate_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));

    public static DeferredBlock<Block> mud_brick_tile = registerBlockWithSimpleItem("mud_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));

    public static DeferredBlock<StairBlock> mud_brick_tile_stair = registerBlockWithSimpleItem("mud_brick_tile_stair", ()->new StairBlock(mud_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(mud_brick_tile.get())));
    public static DeferredBlock<SlabBlock> mud_brick_tile_slab = registerBlockWithSimpleItem("mud_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(mud_brick_tile.get())));
    public static DeferredBlock<WallBlock> mud_brick_tile_wall = registerBlockWithSimpleItem("mud_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(mud_brick_tile.get())));

    public static DeferredBlock<Block> blackstone_brick_tile = registerBlockWithSimpleItem("blackstone_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 4F).sound(SoundType.MUD_BRICKS)));

    public static DeferredBlock<StairBlock> blackstone_brick_tile_stair = registerBlockWithSimpleItem("blackstone_brick_tile_stair", ()->new StairBlock(blackstone_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(blackstone_brick_tile.get())));
    public static DeferredBlock<SlabBlock> blackstone_brick_tile_slab = registerBlockWithSimpleItem("blackstone_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(blackstone_brick_tile.get())));
    public static DeferredBlock<WallBlock> blackstone_brick_tile_wall = registerBlockWithSimpleItem("blackstone_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(blackstone_brick_tile.get())));

    public static DeferredBlock<Block> gold_bars = registerBlockWithSimpleItem("gold_bars", ()->new IronBarsBlock(
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(10.0F, 5.0F).sound(SoundType.METAL).noOcclusion()));


    public static DeferredItem<Item> stone_brick = ITEMS.register("stone_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> deepslate_brick = ITEMS.register("deepslate_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> mud_brick = ITEMS.register("mud_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> red_nether_brick = ITEMS.register("red_nether_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> cobble = ITEMS.registerSimpleItem("cobble");

    public static DeferredItem<Item> deepslate_cobble = ITEMS.registerSimpleItem("deepslate_cobble");

    public static DeferredItem<Item> diamond_shard = ITEMS.registerSimpleItem("diamond_shard");

    public static DeferredItem<Item> blackstone_brick = ITEMS.registerSimpleItem("blackstone_brick");

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> supplier){
        return BLOCKS.register(name, supplier);
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
    }
}
