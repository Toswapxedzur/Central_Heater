package com.minecart.central_heater.misc.enumeration;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
}
