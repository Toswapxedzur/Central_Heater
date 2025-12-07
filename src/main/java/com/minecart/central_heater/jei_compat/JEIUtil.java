package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.fuel.FuelMapHook;
import com.minecart.central_heater.recipe.SmolderingRecipe;
import com.minecart.central_heater.util.VirtualLevel;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.Comparator;
import java.util.List;

public class JEIUtil {
    private JEIUtil(){

    }

    public static List<IJeiFuelingRecipe> getNetherFuelRecipes(IIngredientManager manager){
        return manager.getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = FuelMapHook.getBurnTime(stack);
                    if(burnTime > 0)
                        consumer.accept(new NetherFuelingRecipe(List.of(stack), burnTime));
                }).sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }

    public static SmolderingRecipe fireBrewingSmolderingRecipe(){
        PotionBrewing brewing = VirtualLevel.getPotionBrewing();
    }

    public static void addAnimatedRecipeSoulFlame(IRecipeExtrasBuilder builder, int x, int y, int tick, IGuiHelper helper){
        ResourceLocation texture = Central_heater.modLoc("textures/gui/soul_flame.png");
        IDrawableStatic staticDraw = helper.drawableBuilder(texture, 0, 0, 14, 14).setTextureSize(14, 14).build();
        IDrawableAnimated animatedDraw = helper.createAnimatedDrawable(staticDraw, tick, IDrawableAnimated.StartDirection.TOP, true);
        builder.addDrawable(helper.getRecipeFlameEmpty(), x, y);
        builder.addDrawable(animatedDraw, x, y);
    }

}
