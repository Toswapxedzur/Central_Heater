package com.minecart.central_heater.block_entity_renderer.pot;

import com.minecart.central_heater.block_entity.pot.NetherPotBlockEntity;
import com.minecart.central_heater.block_entity.pot.StonePotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherPotBlockEntityRenderer extends AbstractPotBlockEntityRenderer implements BlockEntityRenderer<NetherPotBlockEntity> {
    public NetherPotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, 8);
    }

    @Override
    public void render(NetherPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
