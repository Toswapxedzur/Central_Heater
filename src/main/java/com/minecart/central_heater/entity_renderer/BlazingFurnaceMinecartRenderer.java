package com.minecart.central_heater.entity_renderer;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.entity.MinecartBlazingFurnace;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.resources.ResourceLocation;

public class BlazingFurnaceMinecartRenderer extends MinecartRenderer<MinecartBlazingFurnace> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/minecart.png");

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            CentralHeater.modLoc("blazing_furnace_minecart"),
            "main"
    );

    public BlazingFurnaceMinecartRenderer(EntityRendererProvider.Context context) {
        super(context, LAYER);
    }

    @Override
    public ResourceLocation getTextureLocation(MinecartBlazingFurnace entity) {
        return TEXTURE;
    }
}
