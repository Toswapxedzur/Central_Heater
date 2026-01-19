package com.minecart.central_heater.recipe.recipe_book;

import com.minecart.central_heater.block_entity.misc.BlazingFurnaceBlockEntity;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class HauntingRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.hauntable");

    @Override
    protected Component getRecipeFilterName() {
        return FILTER_NAME;
    }

    @Override
    protected Set<Item> getFuelItems() {
        return BlazingFurnaceBlockEntity.getHauntFuel().keySet();
    }
}