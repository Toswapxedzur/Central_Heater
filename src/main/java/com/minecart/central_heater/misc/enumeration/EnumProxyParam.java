package com.minecart.central_heater.misc.enumeration;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.Supplier;

public class EnumProxyParam {
    public static final EnumProxy<RecipeBookCategories> BLAZING_SEARCH_PROXY = new EnumProxy<>(
            RecipeBookCategories.class,
            (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.COMPASS))
    );

    public static final EnumProxy<RecipeBookCategories> BLAZING_FURNACE_MISC_PROXY = new EnumProxy<>(
            RecipeBookCategories.class,
            (Supplier<List<ItemStack>>) () -> List.of(new ItemStack(Items.SOUL_CAMPFIRE))
    );

    public static final EnumProxy<Boat.Type> BURNT_BOAT_PROXY = new EnumProxy<>(
            Boat.Type.class,
            (Supplier<Block>) AllBlockItem.BURNT_PLANKS::get,
            "central_heater:burnt",
            (Supplier<Item>) AllBlockItem.BURNT_BOAT::get,
            (Supplier<Item>) AllBlockItem.BURNT_CHEST_BOAT::get,
            (Supplier<Item>) () -> Items.STICK,
            false // Set to true if you want it to look/act like a Bamboo raft!
    );
}
