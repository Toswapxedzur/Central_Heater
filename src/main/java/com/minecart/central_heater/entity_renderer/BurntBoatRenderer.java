package com.minecart.central_heater.entity_renderer;

import com.google.common.collect.ImmutableMap;
import com.minecart.central_heater.CentralHeater;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

/**
 * Renderer for {@link com.minecart.central_heater.entity.BurntBoat} and
 * {@link com.minecart.central_heater.entity.BurntChestBoat}.
 *
 * <p>Vanilla {@link BoatRenderer} ignores {@link #getTextureLocation(Boat)}
 * because it indexes textures+models by {@link Boat#getVariant()} via its
 * internal {@code boatResources} map. Our burnt boats report OAK as their
 * variant (to satisfy the vanilla enum) which would otherwise display as a
 * plain oak boat. To make the burnt texture stick we replace the OAK entry of
 * {@code boatResources} (exposed via Access Transformer) with our own
 * (texture, model) pair.</p>
 */
public class BurntBoatRenderer extends BoatRenderer {
    private static final ResourceLocation BOAT_TEXTURE = new ResourceLocation(CentralHeater.MODID, "textures/entity/boat/burnt.png");
    private static final ResourceLocation CHEST_BOAT_TEXTURE = new ResourceLocation(CentralHeater.MODID, "textures/entity/chest_boat/burnt.png");

    public BurntBoatRenderer(EntityRendererProvider.Context context, boolean isChestBoat) {
        super(context, isChestBoat);

        ResourceLocation texture = isChestBoat ? CHEST_BOAT_TEXTURE : BOAT_TEXTURE;
        ModelLayerLocation layer = isChestBoat
                ? ModelLayers.createChestBoatModelName(Boat.Type.OAK)
                : ModelLayers.createBoatModelName(Boat.Type.OAK);

        ListModel<Boat> model = isChestBoat
                ? new ChestBoatModel(context.bakeLayer(layer))
                : new BoatModel(context.bakeLayer(layer));

        this.boatResources = ImmutableMap.of(Boat.Type.OAK, Pair.of(texture, model));
    }

    @Override
    public ResourceLocation getTextureLocation(Boat boat) {
        return BOAT_TEXTURE;
    }
}
