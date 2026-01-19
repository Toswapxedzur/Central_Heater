package com.minecart.central_heater.block_entity_renderer.stove;

import com.minecart.central_heater.block_entity.stove.AbstractStoveBlockEntity;
import com.minecart.central_heater.misc.ItemUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class QuadrupleFuelInvStoveBlockEntityRenderer {
    public static final Vec3[][] fuelLocation = new Vec3[][]{
            {new Vec3(0, 0.3, 0)},
            {new Vec3(-0.15, 0.3, 0), new Vec3(0.15, 0.3, 0)},
            {new Vec3(-0.15, 0.3, -0.15), new Vec3(-0.15, 0.3, 0.15), new Vec3(0.15, 0.3, 0)},
            {new Vec3(-0.15, 0.3, -0.15), new Vec3(-0.15, 0.3, 0.15), new Vec3(0.15, 0.3, 0), new Vec3(0, 0.55, 0)}};

    public final BlockEntityRendererProvider.Context context;

    public QuadrupleFuelInvStoveBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.context = context;
    }

    public void render(AbstractStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        renderFuel(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        renderInv(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    public void renderInv(AbstractStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        int i = (int) blockEntity.getBlockPos().asLong();

        for(int j=0;j<Math.min(blockEntity.getItemSlots(), 4);j++){
            ItemStack stack = blockEntity.getStackInItems(j);
            if(stack.isEmpty())
                continue;
            Direction direction1 = Direction.from2DDataValue((j + direction.get2DDataValue()) % 4);
            float f = -direction1.toYRot();
            poseStack.pushPose();
            poseStack.translate(0.5f, 0f, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(f));
            poseStack.translate(-0.25f, 0f, -0.25f);
            if(ItemUtil.isFlatItem(stack)) {
                poseStack.translate(0, 1.0125f, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(0.4f, 0.4f, 0.4f);
            }else{
                poseStack.translate(0, 1.125f, 0);
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }

            getRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), j);
            poseStack.popPose();
        }
    }

    public void renderFuel(AbstractStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        NonNullList<ItemStack> stacks = blockEntity.fuels.get();
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        boolean[] isFlat = new boolean[stacks.size()];
        int i = stacks.size()-1;

        for(int j=0;j<stacks.size();j++){
            ItemStack stack = stacks.get(j);
            isFlat[j] = ItemUtil.isFlatItem(stack);
            if(stack.isEmpty())
                continue;

            poseStack.pushPose();
            poseStack.translate(0.5f, 0, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
            poseStack.translate(fuelLocation[i][j].x, fuelLocation[i][j].y, fuelLocation[i][j].z);

            if(j == 3 && isFlat[0] && isFlat[1] && isFlat[2])
                poseStack.translate(0, -0.2f, 0);

            poseStack.scale(0.5f, 0.5f, 0.5f);

            if(isFlat[j]){
                poseStack.scale(0.8f, 0.8f, 0.8f);
                poseStack.translate(0, -0.28f, 0);
                if(direction.getAxis().equals(Direction.Axis.X))
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                else
                    // 1.20.1 doesn't have Axis.XN, so use XP negative
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            }

            getRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), i + j);
            poseStack.popPose();
        }
    }

    public ItemRenderer getRenderer(){
        return context.getItemRenderer();
    }
}