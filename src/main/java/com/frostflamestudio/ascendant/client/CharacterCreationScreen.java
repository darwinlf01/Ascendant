package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.network.SelectIdentityPayload;
import com.frostflamestudio.ascendant.registry.ModAttachments;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class CharacterCreationScreen extends Screen {
    private PlayerClass selectedClass = PlayerClass.NONE;

    public CharacterCreationScreen() {
        super(Component.translatable("screen.ascendant.character_creation"));

        var player = Minecraft.getInstance().player;

        if (player != null) {
            var playerData = player.getData(ModAttachments.PLAYER_DATA);
            this.selectedClass = playerData.getPlayerClass();
            if (this.selectedClass == PlayerClass.NONE) {
                this.selectedClass = PlayerClass.LIGHT_WARRIOR;
            }
        }
    }

    @Override protected void init() {
        this.addRenderableWidget(
            CycleButton.builder(PlayerClass::getDisplayName)
                .withValues(PlayerClass.playable())
                .withInitialValue(this.selectedClass)
                .create(
                    this.width / 2 - 75,
                    this.height / 2 - 24,
                    150,
                    20,
                    Component.translatable("screen.ascendant.player_class"),
                    (button, playerClass) -> this.selectedClass = playerClass
                )
        );

        this.addRenderableWidget(
            Button.builder(Component.translatable("gui.done"), button -> {
                PacketDistributor.sendToServer(
                    new SelectIdentityPayload(this.selectedClass)
                );
                this.onClose();
                })
                .bounds(this.width / 2 - 75, this.height / 2 + 24, 150, 20)
                .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }
}
