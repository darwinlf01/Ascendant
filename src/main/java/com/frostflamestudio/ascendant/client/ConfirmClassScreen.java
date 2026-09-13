package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.network.SelectIdentityPayload;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class ConfirmClassScreen extends Screen {
    private final PlayerClass playerClass;

    public ConfirmClassScreen(PlayerClass playerClass) {
        super(Component.translatable(
            "screen.ascendant.confirm_class",
            playerClass.getDisplayName()
        ));
        this.playerClass = playerClass;
    }

    @Override protected void init() {
        this.addRenderableWidget(
            Button.builder(Component.translatable("gui.yes"), button -> {
                PacketDistributor.sendToServer(
                    new SelectIdentityPayload(this.playerClass)
                );
                this.onClose();
            })
                .bounds(this.width / 2 - 75, this.height / 2 - 12, 150, 20)
                .build()
        );

        this.addRenderableWidget(
            Button.builder(Component.translatable("gui.no"), button -> this.onClose())
                .bounds(this.width / 2 - 75, this.height / 2 + 16, 150, 20)
                .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }
    
}
