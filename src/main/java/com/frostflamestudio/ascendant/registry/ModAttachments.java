package com.frostflamestudio.ascendant.registry;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.PlayerData;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AscendantMod.MODID);
    
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerData>> PLAYER_DATA =
        ATTACHMENTS.register(
            "player_data",
            () -> AttachmentType
                    .serializable(() -> new PlayerData())
                    .copyOnDeath()
                    .sync((holder, to) -> holder == to, PlayerData.STREAM_CODEC)
                    .build()
        );
}
