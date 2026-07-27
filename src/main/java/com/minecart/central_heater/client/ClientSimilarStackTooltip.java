package com.minecart.central_heater.client;

import com.minecart.central_heater.item.similar_stack.SimilarStackTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ClientSimilarStackTooltip implements ClientTooltipComponent {
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot");
    private static final int SLOT_WIDTH = 18;
    private static final int SLOT_HEIGHT = 20;

    private final List<ItemStack> contents;

    public ClientSimilarStackTooltip(SimilarStackTooltip tooltip) {
        this.contents = tooltip.contents();
    }

    @Override
    public int getHeight() {
        return SLOT_HEIGHT + 4;
    }

    @Override
    public int getWidth(Font font) {
        return contents.size() * SLOT_WIDTH;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        for (int i = 0; i < contents.size(); i++) {
            renderItemSlot(guiGraphics, font, x + i * SLOT_WIDTH, y, i);
        }
    }

    private void renderItemSlot(GuiGraphics guiGraphics, Font font, int x, int y, int index) {
        guiGraphics.blitSprite(SLOT_SPRITE, x, y, 0, SLOT_WIDTH, SLOT_HEIGHT);

        ItemStack stack = contents.get(index);
        guiGraphics.renderItem(stack, x + 1, y + 1, index);
        guiGraphics.renderItemDecorations(font, stack, x + 1, y + 1);
        if (index == 0) {
            AbstractContainerScreen.renderSlotHighlight(guiGraphics, x + 1, y + 1, 0);
            guiGraphics.renderOutline(x, y, SLOT_WIDTH, SLOT_HEIGHT, 0xFFFFD75A);
        }
    }
}
