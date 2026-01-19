package com.minecart.central_heater.user_interface.menu;

import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.AllRecipeBooks;
import com.minecart.central_heater.user_interface.AllMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class BlazingFurnaceMenu extends AbstractFurnaceMenu {
    public BlazingFurnaceMenu(int containerId, Inventory playerInventory) {
        super(AllMenu.BLAZING_FURNACE.get(), AllRecipe.HAUNTING.get(), AllRecipeBooks.getBlazingType(), containerId, playerInventory);
    }

    public BlazingFurnaceMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData) {
        super(AllMenu.BLAZING_FURNACE.get(), AllRecipe.HAUNTING.get(), AllRecipeBooks.getBlazingType(), containerId, playerInventory, furnaceContainer, furnaceData);
    }

    @Override
    protected boolean isFuel(ItemStack stack) {
        return DataMapHook.getNetherFuelBurnTime(stack) > 0;
    }
}