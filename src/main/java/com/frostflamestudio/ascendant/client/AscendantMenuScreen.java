package com.frostflamestudio.ascendant.client;

import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.data.StatType;
import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.data.Profession;
import com.frostflamestudio.ascendant.data.Race;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class AscendantMenuScreen extends Screen {
    private static final int LINE_HEIGHT = 12;

    private MenuSection section = MenuSection.STATUS;
    private boolean inspectingRace;
    private int raceLineY = -1;
    private boolean inspectingClass;
    private int classLineY = -1;
    private int statusLeftX;
    private int statusRightX;

    public AscendantMenuScreen() {
        super(Component.translatable("screen.ascendant.menu"));
    }

    @Override
    protected void init() {
        if (this.inspectingRace || this.inspectingClass) {
            this.addRenderableWidget(
                Button.builder(Component.translatable("gui.done"), button -> {
                    this.inspectingRace = false;
                    this.inspectingClass = false;
                    this.rebuildWidgets();
                })
                    .bounds(this.width / 2 - 75, this.height - 28, 150, 20)
                    .build()
            );
            return;
        }

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

        if (this.inspectingRace) {
            this.renderRaceInspect(guiGraphics);
            return;
        }
        if (this.inspectingClass) {
            this.renderClassInspect(guiGraphics);
            return;
        }

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

            this.statusLeftX = this.width / 2 - 160;
            this.statusRightX = this.width / 2 + 50;
            int leftX = this.statusLeftX;
            int rightX = this.statusRightX;
            int y = 80;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.name"),
                player.getName()
            );
            y += LINE_HEIGHT;

            this.raceLineY = y;
            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.race"),
                Component.translatable(
                    "stat.ascendant.named_level",
                    playerData.getRace().getDisplayName(),
                    0
                )
            );
            y += LINE_HEIGHT;
            this.classLineY = y;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.player_class"),
                playerData.getPlayerClass() == PlayerClass.NONE
                    ? Component.translatable("stat.ascendant.na")
                    : Component.translatable(
                        "stat.ascendant.named_level",
                        playerData.getPlayerClass().getDisplayName(),
                        0
                    )
            );
            y += LINE_HEIGHT;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.profession"),
                playerData.getProfession() == Profession.NONE
                    ? Component.translatable("stat.ascendant.na")
                    : Component.translatable(
                        "stat.ascendant.named_level",
                        playerData.getProfession().getDisplayName(),
                        playerData.getProfession() == Profession.MINING
                            ? playerData.getMiningLevel()
                            : 0
                    )
            );
            y += LINE_HEIGHT;

            y += LINE_HEIGHT;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.health"),
                Component.translatable(
                    "stat.ascendant.health_value",
                    (int) player.getHealth(),
                    (int) player.getMaxHealth()
                )
            );
            y += LINE_HEIGHT;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.mana"),
                Component.translatable(
                    "stat.ascendant.health_value",
                    stats.getMana(),
                    stats.getMaxMana()
                )
            );
            y += LINE_HEIGHT;

            this.drawLabeled(
                guiGraphics, leftX, y,
                Component.translatable("stat.ascendant.stamina"),
                Component.translatable(
                    "stat.ascendant.health_value",
                    stats.getStamina(),
                    stats.getMaxStamina()
                )
            );

            int statY = 80;
            for (var type : StatType.values()) {
                if (type == StatType.FREE_POINTS) {
                    statY += LINE_HEIGHT;
                }
                this.drawLabeled(
                    guiGraphics,
                    rightX,
                    statY,
                    type.getDisplayName(),
                    Component.literal(String.valueOf(stats.get(type)))
                );
                statY += LINE_HEIGHT;
            }
        } else {
            this.raceLineY = -1;
            this.classLineY = -1;
            guiGraphics.drawCenteredString(
                this.font,
                Component.translatable("screen.ascendant.tab.empty"),
                this.width / 2,
                80,
                0xAAAAAA
            );
        }
    }

    private void renderRaceInspect(GuiGraphics guiGraphics) {
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        int panelLeft = this.width / 2 - 160;
        int panelRight = this.width / 2 + 160;
        int panelTop = 48;
        int panelBottom = this.height - 52;

        guiGraphics.fill(panelLeft, panelTop, panelRight, panelBottom, 0xC0000000);

        var title = Component.translatable("race.ascendant.human");
        guiGraphics.drawCenteredString(this.font, title, this.width / 2, panelTop + 12, 0xFFD080);

        int textLeft = panelLeft + 12;
        int textWidth = panelRight - panelLeft - 24;
        int textY = panelTop + 36;

        var lines = this.font.split(
            Component.translatable("race.ascendant.human.description"),
            textWidth
        );
        for (FormattedCharSequence line : lines) {
            guiGraphics.drawString(this.font, line, textLeft, textY, 0xFFFFFF);
            textY += LINE_HEIGHT;
        }
    }

    private void renderClassInspect(GuiGraphics guiGraphics) {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        var playerClass = player.getData(ModAttachments.PLAYER_DATA).getPlayerClass();
        if (playerClass == PlayerClass.NONE) {
            return;
        }

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        int panelLeft = this.width / 2 - 160;
        int panelRight = this.width / 2 + 160;
        int panelTop = 48;
        int panelBottom = this.height - 52;

        guiGraphics.fill(panelLeft, panelTop, panelRight, panelBottom, 0xC0000000);

        var title = playerClass.getDisplayName();
        guiGraphics.drawCenteredString(this.font, title, this.width / 2, panelTop + 12, 0xFFD080);

        int textLeft = panelLeft + 12;
        int textWidth = panelRight - panelLeft - 24;
        int textY = panelTop + 36;

        var lines = this.font.split(
            Component.translatable(
                "player_class.ascendant." + playerClass.name().toLowerCase() + ".description"
            ),
            textWidth
        );
        for (FormattedCharSequence line : lines) {
            guiGraphics.drawString(this.font, line, textLeft, textY, 0xFFFFFF);
            textY += LINE_HEIGHT;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.inspectingRace
            && this.section == MenuSection.STATUS
            && button == 0
            && this.raceLineY >= 0
            && mouseX >= this.statusLeftX
            && mouseX < this.statusRightX - 8
            && mouseY >= this.raceLineY
            && mouseY < this.raceLineY + LINE_HEIGHT) {
            var player = Minecraft.getInstance().player;
            if (player != null
                && player.getData(ModAttachments.PLAYER_DATA).getRace() == Race.HUMAN) {
                this.inspectingRace = true;
                this.rebuildWidgets();
                return true;
            }
        }

        if (!this.inspectingClass
            && !this.inspectingRace
            && this.section == MenuSection.STATUS
            && button == 0
            && this.classLineY >= 0
            && mouseX >= this.statusLeftX
            && mouseX < this.statusRightX - 8
            && mouseY >= this.classLineY
            && mouseY < this.classLineY + LINE_HEIGHT) {
            var player = Minecraft.getInstance().player;
            if (player != null
                && player.getData(ModAttachments.PLAYER_DATA).getPlayerClass() != PlayerClass.NONE) {
                this.inspectingClass = true;
                this.rebuildWidgets();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void drawLabeled(GuiGraphics guiGraphics, int x, int y, Component label, Component value) {
        guiGraphics.drawString(this.font, label, x, y, 0xAAAAAA);
        guiGraphics.drawString(this.font, value, x + this.font.width(label) + 8, y, 0xFFFFFF);
    }
}
