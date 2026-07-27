package com.minecart.central_heater.client;

import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import com.minecart.central_heater.network.CycleSimilarStackSelectionPayload;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class ClientSimilarStackControls {
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        int direction = switch (event.getKeyCode()) {
            case GLFW.GLFW_KEY_RIGHT -> 1;
            case GLFW.GLFW_KEY_LEFT -> -1;
            default -> 0;
        };
        if (direction == 0 || !(event.getScreen() instanceof AbstractContainerScreen<?> screen)) {
            return;
        }

        AbstractContainerMenu menu = screen.getMenu();
        if (canCycle(menu.getCarried())) {
            sendCycle(menu, -1, direction);
            event.setCanceled(true);
            return;
        }

        Slot hoveredSlot = screen.getSlotUnderMouse();
        if (hoveredSlot == null || !canCycle(hoveredSlot.getItem())) {
            return;
        }

        int slotIndex = menu.slots.indexOf(hoveredSlot);
        if (slotIndex >= 0) {
            sendCycle(menu, slotIndex, direction);
            event.setCanceled(true);
        }
    }

    private static boolean canCycle(ItemStack stack) {
        return SimilarStacker.hasContents(stack) && SimilarStacker.contentsOf(stack).size() > 1;
    }

    private static void sendCycle(AbstractContainerMenu menu, int slotIndex, int direction) {
        PacketDistributor.sendToServer(new CycleSimilarStackSelectionPayload(menu.containerId, slotIndex, direction));
    }
}
