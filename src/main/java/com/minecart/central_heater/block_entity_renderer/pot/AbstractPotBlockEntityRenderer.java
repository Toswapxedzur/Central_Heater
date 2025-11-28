package com.minecart.central_heater.block_entity_renderer.pot;

import com.minecart.central_heater.block_entity.pot.AbstractPotBlockEntity;
import com.minecart.central_heater.util.AllConstants;
import com.minecart.central_heater.util.ItemUtil;
import com.minecart.central_heater.util.VirtualLevel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class AbstractPotBlockEntityRenderer {
    public final BlockEntityRendererProvider.Context context;
    private final int invLocSize;
    private final Vec3[] invLoc;

    public AbstractPotBlockEntityRenderer(BlockEntityRendererProvider.Context context, int invLocSize){
        this.context = context;
        this.invLocSize = invLocSize;

        switch (invLocSize){
            default -> this.invLoc = AllConstants.potInvLoc8;
        }
    }

    public void render(AbstractPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        renderInv(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        renderFluid(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    public void renderFluid(AbstractPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay){
        FluidStack fluidStack = blockEntity.getFluidTank().getFluidInTank(0);
        float percentage = (float) (fluidStack.getAmount() * 1.0 / blockEntity.getFluidTank().getTankCapacity(0));
        float renderHeight = 0.125f + percentage * 0.75f;
        IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = attributes.getStillTexture();
        if(stillTexture == null)
            return;
        int tintColor = attributes.getTintColor();
        TextureAtlasSprite sprite = VirtualLevel.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        VertexConsumer buffer = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidStack.getFluid().defaultFluidState()));
        poseStack.pushPose();
        renderQuad(poseStack.last(), buffer, tintColor, packedLight, renderHeight, renderHeight, 0f, 0f, 1f, 1f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1());
        poseStack.popPose();
    }

    public void renderInv(AbstractPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        NonNullList<ItemStack> stacks = blockEntity.getContainer().get();
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        for(int j=0;j<stacks.size();j++){
            ItemStack stack = stacks.get(j);
            if(stack.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.translate(0.5f, 0, 0.5f);

            poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
            poseStack.translate(invLoc[j].x, invLoc[j].y, invLoc[j].z);
            poseStack.scale(0.5f, 0.5f, 0.5f);

            if(ItemUtil.isFlatItem(stack)) {
                poseStack.scale(0.8f, 0.8f, 0.8f);
                poseStack.translate(0, -0.2f, 0);
                if(direction.getAxis().equals(Direction.Axis.X))
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                else
                    poseStack.mulPose(Axis.XN.rotationDegrees(90));
            }

            getRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), j);
            poseStack.popPose();
        }
    }

    private static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int color,
            int packed,
            float minY,
            float maxY,
            float minX,
            float minZ,
            float maxX,
            float maxZ,
            float u0,
            float v0,
            float u1,
            float v1
    ) {
        consumer.addVertex(pose, minX, minY, minZ).setColor(color).setUv(u0, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packed).setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(pose, minX, maxY, maxZ).setColor(color).setUv(u0, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packed).setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(pose, maxX, maxY, maxZ).setColor(color).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packed).setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(pose, maxX, minY, minZ).setColor(color).setUv(u1, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packed).setNormal(pose, 0f, 1f, 0f);
    }

    public ItemRenderer getRenderer(){
        return context.getItemRenderer();
    }
}
