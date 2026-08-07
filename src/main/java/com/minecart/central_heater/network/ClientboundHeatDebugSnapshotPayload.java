package com.minecart.central_heater.network;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.client.ClientHeatDebugState;
import com.minecart.central_heater.heat.debug.HeatDebugLabel;
import com.minecart.central_heater.heat.debug.HeatDebugOutline;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record ClientboundHeatDebugSnapshotPayload(boolean enabled, List<HeatDebugLabel> labels, List<HeatDebugOutline> outlines) implements CustomPacketPayload {
    public static final Type<ClientboundHeatDebugSnapshotPayload> TYPE = new Type<>(CentralHeater.modLoc("heat_debug_snapshot"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundHeatDebugSnapshotPayload> STREAM_CODEC = StreamCodec.of(
            ClientboundHeatDebugSnapshotPayload::write,
            ClientboundHeatDebugSnapshotPayload::read
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClientboundHeatDebugSnapshotPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientHeatDebugState.apply(payload.enabled(), payload.labels(), payload.outlines()));
    }

    private static void write(RegistryFriendlyByteBuf buffer, ClientboundHeatDebugSnapshotPayload payload) {
        buffer.writeBoolean(payload.enabled());
        buffer.writeVarInt(payload.labels().size());
        for (HeatDebugLabel label : payload.labels()) {
            buffer.writeDouble(label.x());
            buffer.writeDouble(label.y());
            buffer.writeDouble(label.z());
            buffer.writeVarInt(label.celsius());
        }
        buffer.writeVarInt(payload.outlines().size());
        for (HeatDebugOutline outline : payload.outlines()) {
            buffer.writeInt(outline.originX());
            buffer.writeInt(outline.originY());
            buffer.writeInt(outline.originZ());
            buffer.writeLong(outline.blockMask());
            buffer.writeInt(outline.color());
        }
    }

    private static ClientboundHeatDebugSnapshotPayload read(RegistryFriendlyByteBuf buffer) {
        boolean enabled = buffer.readBoolean();
        int count = Math.min(buffer.readVarInt(), 512);
        List<HeatDebugLabel> labels = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            labels.add(new HeatDebugLabel(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readVarInt()));
        }
        int outlineCount = Math.min(buffer.readVarInt(), 512);
        List<HeatDebugOutline> outlines = new ArrayList<>(outlineCount);
        for (int i = 0; i < outlineCount; i++) {
            outlines.add(new HeatDebugOutline(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readLong(), buffer.readInt()));
        }
        return new ClientboundHeatDebugSnapshotPayload(enabled, labels, outlines);
    }
}
