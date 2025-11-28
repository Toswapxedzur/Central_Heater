package com.minecart.central_heater.block_entity_renderer.pot;

import com.minecart.central_heater.block_entity.pot.BrickPotBlockEntity;
import com.minecart.central_heater.block_entity.pot.StonePotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StonePotBlockEntityRenderer extends AbstractPotBlockEntityRenderer implements BlockEntityRenderer<StonePotBlockEntity> {
    public StonePotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 8);
    }

    @Override
    public void render(StonePotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
