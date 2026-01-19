package com.minecart.central_heater.misc;

import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Alltags {
    public static class Blocks{
        public static TagKey<Block> STOVE = create("stove");
        public static TagKey<Block> POT = create("pot");

        private static TagKey<Block> create(String name){
            return TagKey.create(Registries.BLOCK, CentralHeater.modLoc(name));
        }

        private static TagKey<Block> createCommon(String name){
            return TagKey.create(Registries.BLOCK, new ResourceLocation("c", name));
        }
    }

    public static class Items{
        public static TagKey<Item> DOUGH = createCommon("foods/dough");
        public static TagKey<Item> FLOUR = createCommon("foods/flour");
        public static TagKey<Item> OVERBURNT = create("overburnt");
        public static TagKey<Item> SOULISTIC = create("soulistic");
        public static TagKey<Item> SHOULD_DISPLAY_ITEM = create("should_display_item");

        private static TagKey<Item> create(String name){
            return TagKey.create(Registries.ITEM, CentralHeater.modLoc(name));
        }

        private static TagKey<Item> createCommon(String name){
            return TagKey.create(Registries.ITEM, new ResourceLocation("c", name));
        }
    }
}