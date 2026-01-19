package com.minecart.central_heater.jei_compat.misc;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;

public abstract class VariantCategory<T> implements IRecipeCategory<T> {
    public static final String TEXTURE_GUI_PATH = "textures/jei/gui/";
    public static final String TEXTURE_GUI_VANILLA = TEXTURE_GUI_PATH + "gui_vanilla.png";
    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(ModIds.JEI_ID, TEXTURE_GUI_VANILLA);

    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;
    protected final IDrawableStatic staticArrow;
    protected final IDrawableAnimated animatedArrow;
    protected final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public VariantCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(RECIPE_GUI_VANILLA, 82, 114, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        staticArrow = guiHelper.createDrawable(RECIPE_GUI_VANILLA, 61, 79, 24, 17);
        animatedArrow = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(RECIPE_GUI_VANILLA, 82, 128, 24, 17), 300, IDrawableAnimated.StartDirection.LEFT, false);
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer time) {
                        return guiHelper.createAnimatedDrawable(guiHelper.createDrawable(RECIPE_GUI_VANILLA, 82, 128, 24, 17), time, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    protected IDrawableAnimated getArrow(Integer time) {
        if (time <= 0) {
            time = 0;
        }
        return this.cachedArrows.getUnchecked(time);
    }
}