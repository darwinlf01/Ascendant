package com.frostflamestudio.ascendant.event;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.Race;
import com.frostflamestudio.ascendant.network.SelectIdentityPayload;
import com.frostflamestudio.ascendant.system.CharacterSystem;
import com.frostflamestudio.ascendant.network.AssignSkillSlotPayload;
import com.frostflamestudio.ascendant.system.SkillSystem;

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
        event.registrar("1").playToServer(
            AssignSkillSlotPayload.TYPE,
            AssignSkillSlotPayload.STREAM_CODEC,
            NetworkEvents::onAssignSkillSlot
        );
    }

    public static void onSelectIdentity(SelectIdentityPayload payload, IPayloadContext context) {
        AscendantMod.LOGGER.info(
            "Select identity: {} {}",
            Race.HUMAN,
            payload.playerClass()
        );

        CharacterSystem.selectIdentity(context.player(), payload.playerClass());
    }

    public static void onAssignSkillSlot(AssignSkillSlotPayload payload, IPayloadContext context) {
        SkillSystem.assignSlot(context.player(), payload.skill(), payload.slot());
    }
}
