package com.minecart.central_heater.client;

import com.minecart.central_heater.heat.debug.HeatDebugLabel;
import com.minecart.central_heater.heat.debug.HeatDebugOutline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public class ClientHeatDebugRenderer {
    private static final double MAX_LABEL_DISTANCE_SQUARED = 96.0D * 96.0D;
    private static final double MAX_OUTLINE_DISTANCE_SQUARED = 128.0D * 128.0D;
    private static final float LABEL_SCALE = 0.025F;
    private static final float CARD_PADDING_X = 4.0F;
    private static final float CARD_PADDING_Y = 3.0F;
    private static final int CARD_COLOR = 0xD0181818;

    private ClientHeatDebugRenderer() {
    }

    @SubscribeEvent
    public static void renderHeatLabels(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER || !ClientHeatDebugState.enabled()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null || (ClientHeatDebugState.labels().isEmpty() && ClientHeatDebugState.outlines().isEmpty()) || !event.getCamera().isInitialized()) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        Vec3 camera = event.getCamera().getPosition();
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        Font font = minecraft.font;
        RenderType cardRenderType = RenderType.textBackgroundSeeThrough();

        renderOutlines(poseStack, buffer, camera);

        for (HeatDebugLabel label : ClientHeatDebugState.labels()) {
            if (!isVisible(label, camera)) {
                continue;
            }

            String text = label.celsius() + "C";
            poseStack.pushPose();
            applyLabelPose(poseStack, camera, label);
            Matrix4f matrix = poseStack.last().pose();
            float textWidth = font.width(text);
            float halfWidth = textWidth / 2.0F;
            renderCard(matrix, buffer.getBuffer(cardRenderType),
                    -halfWidth - CARD_PADDING_X,
                    -CARD_PADDING_Y,
                    halfWidth + CARD_PADDING_X,
                    font.lineHeight + CARD_PADDING_Y,
                    CARD_COLOR);
            poseStack.popPose();
        }
        buffer.endBatch(cardRenderType);

        for (HeatDebugLabel label : ClientHeatDebugState.labels()) {
            if (!isVisible(label, camera)) {
                continue;
            }

            String text = label.celsius() + "C";
            DebugRenderer.renderFloatingText(poseStack, buffer, text, label.x(), label.y(), label.z(), color(label.celsius()), LABEL_SCALE, true, 0.0F, true);
        }

        buffer.endBatch();
    }

    private static boolean isVisible(HeatDebugLabel label, Vec3 camera) {
        double dx = label.x() - camera.x();
        double dy = label.y() - camera.y();
        double dz = label.z() - camera.z();
        return dx * dx + dy * dy + dz * dz <= MAX_LABEL_DISTANCE_SQUARED;
    }

    private static void renderOutlines(PoseStack poseStack, MultiBufferSource.BufferSource buffer, Vec3 camera) {
        if (ClientHeatDebugState.outlines().isEmpty()) {
            return;
        }
        RenderType lineRenderType = RenderType.lines();
        VertexConsumer consumer = buffer.getBuffer(lineRenderType);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.lineWidth(2.0F);
        poseStack.pushPose();
        poseStack.translate(-camera.x(), -camera.y(), -camera.z());
        for (HeatDebugOutline outline : ClientHeatDebugState.outlines()) {
            if (!outlineVisible(outline, camera)) {
                continue;
            }
            renderOutlineMask(poseStack, consumer, outline);
        }
        poseStack.popPose();
        buffer.endBatch(lineRenderType);
        RenderSystem.lineWidth(1.0F);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    private static boolean outlineVisible(HeatDebugOutline outline, Vec3 camera) {
        double centerX = outline.originX() + 2.0D;
        double centerY = outline.originY() + 2.0D;
        double centerZ = outline.originZ() + 2.0D;
        double dx = centerX - camera.x();
        double dy = centerY - camera.y();
        double dz = centerZ - camera.z();
        return dx * dx + dy * dy + dz * dz <= MAX_OUTLINE_DISTANCE_SQUARED;
    }

    private static void renderOutlineMask(PoseStack poseStack, VertexConsumer consumer, HeatDebugOutline outline) {
        float alpha = ((outline.color() >>> 24) & 0xFF) / 255.0F;
        float red = ((outline.color() >>> 16) & 0xFF) / 255.0F;
        float green = ((outline.color() >>> 8) & 0xFF) / 255.0F;
        float blue = (outline.color() & 0xFF) / 255.0F;
        long mask = outline.blockMask();
        while (mask != 0L) {
            int bit = Long.numberOfTrailingZeros(mask);
            int x = outline.originX() + (bit & 3);
            int y = outline.originY() + ((bit >> 2) & 3);
            int z = outline.originZ() + ((bit >> 4) & 3);
            LevelRenderer.renderLineBox(poseStack, consumer,
                    x - 0.002D, y - 0.002D, z - 0.002D,
                    x + 1.002D, y + 1.002D, z + 1.002D,
                    red, green, blue, alpha);
            mask &= mask - 1L;
        }
    }

    private static void applyLabelPose(PoseStack poseStack, Vec3 camera, HeatDebugLabel label) {
        Minecraft minecraft = Minecraft.getInstance();
        poseStack.translate((float) (label.x() - camera.x()), (float) (label.y() - camera.y()) + 0.07F, (float) (label.z() - camera.z()));
        poseStack.mulPose(minecraft.gameRenderer.getMainCamera().rotation());
        poseStack.scale(LABEL_SCALE, -LABEL_SCALE, LABEL_SCALE);
    }

    private static void renderCard(Matrix4f matrix, VertexConsumer consumer, float minX, float minY, float maxX, float maxY, int color) {
        int alpha = color >>> 24 & 0xFF;
        int red = color >>> 16 & 0xFF;
        int green = color >>> 8 & 0xFF;
        int blue = color & 0xFF;
        float z = 0.01F;

        consumer.addVertex(matrix, minX, maxY, z).setColor(red, green, blue, alpha).setLight(LightTexture.FULL_BRIGHT);
        consumer.addVertex(matrix, maxX, maxY, z).setColor(red, green, blue, alpha).setLight(LightTexture.FULL_BRIGHT);
        consumer.addVertex(matrix, maxX, minY, z).setColor(red, green, blue, alpha).setLight(LightTexture.FULL_BRIGHT);
        consumer.addVertex(matrix, minX, minY, z).setColor(red, green, blue, alpha).setLight(LightTexture.FULL_BRIGHT);
    }

    private static int color(int celsius) {
        if (celsius < 0) {
            return 0x80C8FF;
        }
        if (celsius < 40) {
            return 0xFFFFFF;
        }
        if (celsius < 120) {
            return 0xFFD166;
        }
        return 0xFF6B35;
    }
}
