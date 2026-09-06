package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.network.SelectIdentityPayload;
import com.frostflamestudio.ascendant.system.CharacterSystem;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = AscendantMod.MODID)
public class NetworkEvents {
    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(
            SelectIdentityPayload.TYPE,
            SelectIdentityPayload.STREAM_CODEC,
            NetworkEvents::onSelectIdentity
        );
    }

    public static void onSelectIdentity(SelectIdentityPayload payload, IPayloadContext context) {
        AscendantMod.LOGGER.info(
            "Select identity: {} {}",
            payload.race(),
            payload.playerClass()
        );

        CharacterSystem.selectIdentity(context.player(), payload.race(), payload.playerClass());
    }
}
