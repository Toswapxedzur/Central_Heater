package com.minecart.central_heater.util;

import com.minecart.central_heater.Central_heater;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Alltags {
    public static class Blocks{
        public static TagKey<Block> STOVE = create("stove");
        public static TagKey<Block> POT = create("pot");

        private static TagKey<Block> create(String name){
            return TagKey.create(Registries.BLOCK, Central_heater.modLoc(name));
        }
    }

    public static class Items{
        public static TagKey<Item> OVERBURNT = create("overburnt");

        private static TagKey<Item> create(String name){
            return TagKey.create(Registries.ITEM, Central_heater.modLoc(name));
        }
    }
}
