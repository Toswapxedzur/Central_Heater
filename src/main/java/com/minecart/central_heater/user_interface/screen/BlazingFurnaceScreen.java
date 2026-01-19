package com.minecart.central_heater.user_interface.screen;

import com.minecart.central_heater.recipe.recipe_book.HauntingRecipeBookComponent;
import com.minecart.central_heater.user_interface.menu.BlazingFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlazingFurnaceScreen extends AbstractFurnaceScreen<BlazingFurnaceMenu> {
    // Note: Use your mod ID here if the texture is in your assets
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
    // If using custom mod texture:
    // private static final ResourceLocation TEXTURE = new ResourceLocation("central_heater", "textures/gui/container/blazing_furnace.png");

    public BlazingFurnaceScreen(BlazingFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new HauntingRecipeBookComponent(), playerInventory, title, TEXTURE);
    }
}