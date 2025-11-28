package com.minecart.central_heater.block_entity_renderer.stove;

import com.minecart.central_heater.block_entity.stove.AbstractStoveBlockEntity;
import com.minecart.central_heater.util.AllConstants;
import com.minecart.central_heater.util.ItemUtil;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractStoveBlockEntityRenderer{
    public final BlockEntityRendererProvider.Context context;
    private final int fuelLocSize;
    private final Vec3[][] fuelLoc;
    private final int invLocSize;
    private final Vec3[] invLoc;

    public AbstractStoveBlockEntityRenderer(BlockEntityRendererProvider.Context context, int fuelLocSize, int invLocSize){
        this.context = context;
        this.fuelLocSize = fuelLocSize;
        this.invLocSize = invLocSize;

        if(fuelLocSize<=4)
            this.fuelLoc = AllConstants.stoveFuelLoc4;
        else
            this.fuelLoc = AllConstants.stoveFuelLoc4;
        switch (invLocSize){
            case 1 -> this.invLoc = AllConstants.stoveInvLoc1;
            case 4 -> this.invLoc = AllConstants.stoveInvLoc4;
            case 9 -> this.invLoc = AllConstants.stoveInvLoc9;
            default -> this.invLoc = AllConstants.stoveInvLoc4;
        }
    }

    public void render(AbstractStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        renderFuel(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        renderInv(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    public void renderInv(AbstractStoveBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        NonNullList<ItemStack> stacks = blockEntity.items.get();
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
            poseStack.translate(fuelLoc[i][j].x, fuelLoc[i][j].y, fuelLoc[i][j].z);

            if(fuelLocSize == 2 && j == 3 && isFlat[0] && isFlat[1] && isFlat[2])
                poseStack.translate(0, -0.2f, 0);

            poseStack.scale(0.5f, 0.5f, 0.5f);

            if(isFlat[j]){
                poseStack.scale(0.8f, 0.8f, 0.8f);
                poseStack.translate(0, -0.28f, 0);
                if(direction.getAxis().equals(Direction.Axis.X))
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                else
                    poseStack.mulPose(Axis.XN.rotationDegrees(90));
            }

            getRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), i + j);
            poseStack.popPose();
        }
    }

    public ItemRenderer getRenderer(){
        return context.getItemRenderer();
    }
}
