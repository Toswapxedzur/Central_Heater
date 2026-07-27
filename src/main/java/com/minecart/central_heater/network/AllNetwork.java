package com.minecart.central_heater.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AllNetwork {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(CycleSimilarStackSelectionPayload.TYPE, CycleSimilarStackSelectionPayload.STREAM_CODEC, CycleSimilarStackSelectionPayload::handle);
        registrar.playToClient(ClientboundHeatDebugSnapshotPayload.TYPE, ClientboundHeatDebugSnapshotPayload.STREAM_CODEC, ClientboundHeatDebugSnapshotPayload::handle);
    }
}
