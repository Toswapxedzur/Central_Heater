package com.minecart.central_heater.network;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.item.similar_stack.SimilarStacker;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CycleSimilarStackSelectionPayload(int containerId, int slotIndex, int direction) implements CustomPacketPayload {
    public static final Type<CycleSimilarStackSelectionPayload> TYPE = new Type<>(CentralHeater.modLoc("cycle_similar_stack_selection"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CycleSimilarStackSelectionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            CycleSimilarStackSelectionPayload::containerId,
            ByteBufCodecs.VAR_INT,
            CycleSimilarStackSelectionPayload::slotIndex,
            ByteBufCodecs.VAR_INT,
            CycleSimilarStackSelectionPayload::direction,
            CycleSimilarStackSelectionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CycleSimilarStackSelectionPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        AbstractContainerMenu menu = player.containerMenu;
        if (menu.containerId != payload.containerId()) {
            return;
        }

        if (payload.slotIndex() == -1) {
            ItemStack carried = menu.getCarried();
            if (SimilarStacker.cycleSelected(carried, payload.direction())) {
                menu.setCarried(carried);
                menu.broadcastChanges();
            }
            return;
        }

        if (!menu.isValidSlotIndex(payload.slotIndex())) {
            return;
        }

        Slot slot = menu.getSlot(payload.slotIndex());
        ItemStack stack = slot.getItem();
        if (SimilarStacker.cycleSelected(stack, payload.direction())) {
            slot.setChanged();
            menu.broadcastChanges();
        }
    }
}
