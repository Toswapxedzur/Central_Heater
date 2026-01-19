package com.minecart.central_heater.block_entity_renderer.pot;

import com.minecart.central_heater.block_entity.cauldron.AbstractCauldronBlockEntity;
import com.minecart.central_heater.misc.ItemUtil;
import com.minecart.central_heater.misc.VirtualLevel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class QuadruplePotBlockEntityRenderer {
    public final BlockEntityRendererProvider.Context context;

    public QuadruplePotBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
    }

    public void render(AbstractCauldronBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int smoothAmount = (int) Mth.lerp(partialTick, blockEntity.prevClientFluid, blockEntity.clientFluid);
        float percentage = (float) (smoothAmount * 1.0 / blockEntity.getFluidTank().getTankCapacity(0));
        float renderHeight = 0.249375f + percentage * 0.688125f;
        renderInv(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay, renderHeight);
        renderFluid(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay, renderHeight);
    }

    public void renderFluid(AbstractCauldronBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, float fluidHeight){
        FluidStack fluidStack = blockEntity.clientFluidType.copy();
        if (fluidStack.isEmpty()) return;
        Potion potion = PotionUtils.getPotion(fluidStack.getTag());
        IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation stillTexture = attributes.getStillTexture(fluidStack);
        if(stillTexture == null)
            return;
        int tintColor = attributes.getTintColor(fluidStack);
        if(fluidStack.getFluid().getFluidType().getLightLevel() > 0)
            packedLight = LightTexture.pack(fluidStack.getFluid().getFluidType().getLightLevel(), LightTexture.sky(packedLight));
        if(fluidStack.getFluid().isSame(Fluids.WATER) && !potion.equals(Potions.EMPTY)) {
            tintColor = PotionUtils.getColor(potion);
        }
        TextureAtlasSprite sprite = VirtualLevel.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        VertexConsumer buffer = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidStack.getFluid().defaultFluidState()));
        renderQuad(poseStack.last(), buffer, tintColor, packedLight, fluidHeight, fluidHeight, 0f, 0f, 1f, 1f, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1());
    }

    public void renderInv(AbstractCauldronBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, float fluidHeight) {
        NonNullList<ItemStack> stacks = blockEntity.getContainer().get();
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        int i = (int) blockEntity.getBlockPos().asLong();

        float spin = Mth.lerp(partialTick, blockEntity.prevClientSpin, blockEntity.clientSpin);
        float setIn = Mth.lerp(partialTick, blockEntity.prevSpinVelocity, blockEntity.spinVelocity);

        for(int j=0;j<4;j++){
            ItemStack stack = stacks.get(j);
            if(stack.isEmpty())
                continue;
            Direction direction1 = Direction.from2DDataValue((j + direction.get2DDataValue()) % 4);
            float f = -direction1.toYRot() + spin;
            poseStack.pushPose();
            poseStack.translate(0.5f, 0f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(f));
            poseStack.translate(-0.1875f + setIn * 0.05f, 0f, -0.1875f + setIn * 0.05f);

            if(ItemUtil.isFlatItem(stack)) {
                poseStack.translate(0f, Math.max(fluidHeight, 0.2625f), 0f);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(0.4f, 0.4f, 0.4f);
            }else {
                poseStack.translate(0, Math.max(fluidHeight, 0.375f), 0f);
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(10));
            VirtualLevel.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), i + j);
            poseStack.popPose();

            if(bufferSource instanceof MultiBufferSource.BufferSource source){
                source.endBatch();
            }
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
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();

        consumer.vertex(matrix, minX, minY, minZ).color(color).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packed).normal(normal, 0f, 1f, 0f).endVertex();
        consumer.vertex(matrix, minX, maxY, maxZ).color(color).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packed).normal(normal, 0f, 1f, 0f).endVertex();
        consumer.vertex(matrix, maxX, maxY, maxZ).color(color).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packed).normal(normal, 0f, 1f, 0f).endVertex();
        consumer.vertex(matrix, maxX, minY, minZ).color(color).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packed).normal(normal, 0f, 1f, 0f).endVertex();
    }

    public ItemRenderer getRenderer(){
        return context.getItemRenderer();
    }
}