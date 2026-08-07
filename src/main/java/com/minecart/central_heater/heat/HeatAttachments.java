package com.minecart.central_heater.heat;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.heat.storage.ChunkHeatData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class HeatAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CentralHeater.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ChunkHeatData>> HEAT_CHUNK = ATTACHMENTS.register(
            "heat_chunk",
            () -> AttachmentType.serializable(ChunkHeatData::new).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}
