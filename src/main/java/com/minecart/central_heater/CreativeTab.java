package com.minecart.central_heater;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Central_heater.MODID);

    public static final Supplier<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("central_heater",
            ()->CreativeModeTab.builder().icon(()->new ItemStack(AllBlockItem.brick_stove.asItem())).title(Component.translatable("central_heater.creativeTabs.central_heater"))
                    .displayItems(AllBlockItem.ITEMS.getEntries()).withSearchBar().build());

    public static void register(IEventBus modEventBus){
        CREATIVE_TABS.register(modEventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(AllBlockItem.stone_stove.asItem());
            event.accept(AllBlockItem.red_nether_brick_stove.asItem());
            event.accept(AllBlockItem.brick_stove.asItem());
            event.accept(AllBlockItem.mud_brick_stove.asItem());
            event.accept(AllBlockItem.deepslate_stove.asItem());
            event.accept(AllBlockItem.nether_brick_stove.asItem());
            event.accept(AllBlockItem.iron_lid.asItem());
            event.accept(AllBlockItem.gold_lid.asItem());

            event.accept(AllBlockItem.brick_pot.asItem());
            event.accept(AllBlockItem.mud_brick_pot.asItem());
            event.accept(AllBlockItem.stone_pot.asItem());
            event.accept(AllBlockItem.deepslate_pot.asItem());
            event.accept(AllBlockItem.red_nether_brick_pot.asItem());
            event.accept(AllBlockItem.nether_brick_pot.asItem());
        }else if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(AllBlockItem.stone_brick_tile.asItem());
            event.accept(AllBlockItem.deepslate_brick_tile.asItem());
            event.accept(AllBlockItem.mud_brick_tile.asItem());
            event.accept(AllBlockItem.blackstone_brick_tile.asItem());

            event.accept(AllBlockItem.stone_brick_tile_stair.asItem());
            event.accept(AllBlockItem.stone_brick_tile_slab.asItem());
            event.accept(AllBlockItem.stone_brick_tile_wall.asItem());

            event.accept(AllBlockItem.deepslate_brick_tile_stair.asItem());
            event.accept(AllBlockItem.deepslate_brick_tile_slab.asItem());
            event.accept(AllBlockItem.deepslate_brick_tile_wall.asItem());

            event.accept(AllBlockItem.mud_brick_tile_stair.asItem());
            event.accept(AllBlockItem.mud_brick_tile_slab.asItem());
            event.accept(AllBlockItem.mud_brick_tile_wall.asItem());

            event.accept(AllBlockItem.blackstone_brick_tile_stair.asItem());
            event.accept(AllBlockItem.blackstone_brick_tile_slab.asItem());
            event.accept(AllBlockItem.blackstone_brick_tile_wall.asItem());

            event.accept(AllBlockItem.gold_bars.asItem());
        }else if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(AllBlockItem.cobble.asItem());
            event.accept(AllBlockItem.deepslate_cobble.asItem());
            event.accept(AllBlockItem.stone_brick.asItem());
            event.accept(AllBlockItem.mud_brick.asItem());
            event.accept(AllBlockItem.red_nether_brick.asItem());
            event.accept(AllBlockItem.deepslate_brick.asItem());
            event.accept(AllBlockItem.diamond_shard.asItem());
            event.accept(AllBlockItem.blackstone_brick.asItem());
        }
    }
}
