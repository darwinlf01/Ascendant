package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.data.Race;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CharacterCreationScreen extends Screen {
    private Race selectedRace = Race.NONE;
    private PlayerClass selectedClass = PlayerClass.NONE;

    public CharacterCreationScreen() {
        super(Component.translatable("screen.ascendant.character_creation"));
    }

    @Override protected void init() {
        this.addRenderableWidget(
            CycleButton.builder(Race::getDisplayName)
                .withValues(Race.values())
                .withInitialValue(this.selectedRace)
                .create(
                    this.width / 2 - 75,
                    this.height / 2 - 24,
                    150,
                    20,
                    Component.translatable("screen.ascendant.race"),
                    (button, race) -> this.selectedRace = race
                )
        );

        this.addRenderableWidget(
            CycleButton.builder(PlayerClass::getDisplayName)
                .withValues(PlayerClass.values())
                .withInitialValue(this.selectedClass)
                .create(
                    this.width / 2 - 75,
                    this.height / 2,
                    150,
                    20,
                    Component.translatable("screen.ascendant.player_class"),
                    (button, playerClass) -> this.selectedClass = playerClass
                )
        );

        this.addRenderableWidget(
            Button.builder(Component.translatable("gui.done"), button -> this.onClose())
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
