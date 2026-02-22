package com.minecart.central_heater.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VirtualLevel {
    public static Minecraft getMinecraft(){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft == null)
            throw new NullPointerException("minecraft must not be null.");
        return minecraft;
    }

    public static Level getLevel(){
        Level ret = getMinecraft().level;
        if(ret == null)
            throw new NullPointerException("level must not be null.");
        return ret;
    }

    public static RegistryAccess getRegistryAccess(){
        return getLevel().registryAccess();
    }

    public static RecipeManager getRecipeManager(){
        return getLevel().getRecipeManager();
    }

    // NOTE: In Forge 1.20.1, PotionBrewing is fully static.
    // You cannot get an instance of it.
    // Callers of this method must be changed to use PotionBrewing static methods directly.
    /*
    public static PotionBrewing getPotionBrewing(){
        return getLevel().potionBrewing(); // This method does not exist in 1.20.1
    }
    */

    public static ItemRenderer getItemRenderer(){
        return getMinecraft().getItemRenderer();
    }

    public static BlockRenderDispatcher getBlockRenderer(){
        return getMinecraft().getBlockRenderer();
    }

    public static EntityRenderDispatcher getEntityRenderer(){
        return getMinecraft().getEntityRenderDispatcher();
    }
}