package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.data.StatType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AscendantMenuScreen extends Screen {
    private MenuSection section = MenuSection.STATUS;

    public AscendantMenuScreen() {
        super(Component.translatable("screen.ascendant.menu"));
    }

    @Override
    protected void init() {
        int buttonWidth = 72;
        int spacing = 4;
        var sections = MenuSection.values();
        int totalWidth = sections.length * buttonWidth + (sections.length - 1) * spacing;
        int startX = this.width / 2 - totalWidth / 2;

        for (int i = 0; i < sections.length; i++) {
            var menuSection = sections[i];
            int x = startX + i * (buttonWidth + spacing);
            this.addRenderableWidget(
                Button.builder(menuSection.getDisplayName(), button -> this.section = menuSection)
                    .bounds(x, 32, buttonWidth, 20)
                    .build()
            );
        }

        this.addRenderableWidget(
            Button.builder(Component.translatable("gui.done"), button -> this.onClose())
                .bounds(this.width / 2 - 75, this.height - 28, 150, 20)
                .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        guiGraphics.drawCenteredString(
            this.font,
            this.section.getDisplayName(),
            this.width / 2,
            60,
            0xFFD080
        );

        if (this.section == MenuSection.STATUS) {
            var player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            var playerData = player.getData(ModAttachments.PLAYER_DATA);
            var stats = playerData.getStatData();

            guiGraphics.fill(
                this.width / 2 - 180,
                72,
                this.width / 2 + 180,
                this.height - 52,
                0x80000000
            );

            int leftX = this.width / 2 - 160;
            int rightX = this.width / 2 + 8;
            int line = 12;
            int y = 80;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.name"),
                player.getName()
            );
            y += line;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.race"),
                playerData.getRace().getDisplayName()
            );
            y += line;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.player_class"),
                playerData.getPlayerClass().getDisplayName()
            );
            y += line;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.profession"),
                playerData.getProfession().getDisplayName()
            );
            y += line;

            y += line;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.health"),
                Component.translatable(
                    "stat.ascendant.health_value",
                    (int) player.getHealth(),
                    (int) player.getMaxHealth()
                )
            );
            y += line;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.mana"),
                Component.literal("—")
            );
            y += line;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.stamina"),
                Component.literal("—")
            );

            int statY = 80;
            for (var type : StatType.values()) {
                if (type == StatType.FREE_POINTS) {
                    statY += line;
                }
                this.drawLabeled(
                    guiGraphics,
                    rightX,
                    statY,
                    type.getDisplayName(),
                    Component.literal(String.valueOf(stats.get(type)))
                );
                statY += line;
            }
        } else {
            guiGraphics.drawCenteredString(
                this.font,
                Component.translatable("screen.ascendant.tab.empty"),
                this.width / 2,
                80,
                0xAAAAAA
            );
        }
    }

    private void drawLabeled(GuiGraphics guiGraphics, int x, int y, Component label, Component value) {
        guiGraphics.drawString(this.font, label, x, y, 0xAAAAAA);
        guiGraphics.drawString(this.font, value, x + 110, y, 0xFFFFFF);
    }
}