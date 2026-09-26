package com.frostflamestudio.ascendant.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum Skill {
    BASIC_ARCHERY(PlayerClass.ARCHER, Kind.PASSIVE, Grade.INFERIOR),
    BASIC_ONE_HANDED(PlayerClass.ARCHER, Kind.PASSIVE, Grade.INFERIOR),
    ARCHERS_EYE(PlayerClass.ARCHER, Kind.ACTIVE, Grade.COMMON);

    public enum Kind {
        PASSIVE,
        ACTIVE
    }

    public enum Grade {
        INFERIOR,
        COMMON
    }

    private final PlayerClass playerClass;
    private final Kind kind;
    private final Grade grade;

    Skill(PlayerClass playerClass, Kind kind, Grade grade) {
        this.playerClass = playerClass;
        this.kind = kind;
        this.grade = grade;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public Kind getKind() {
        return kind;
    }

    public Grade getGrade() {
        return grade;
    }

    public Component getDisplayName() {
        return Component.translatable("skill.ascendant." + name().toLowerCase());
    }

    public Component getDescription() {
        return Component.translatable("skill.ascendant." + name().toLowerCase() + ".description");
    }

    public Component getBarLabel() {
        return Component.translatable("skill.ascendant." + name().toLowerCase() + ".bar");
    }

    public Component getGradeName() {
        return Component.translatable("skill.ascendant.grade." + grade.name().toLowerCase());
    }

    public Component getKindName() {
        return Component.translatable("skill.ascendant.kind." + kind.name().toLowerCase());
    }

    public static final StreamCodec<ByteBuf, Skill> STREAM_CODEC =
        ByteBufCodecs.STRING_UTF8.map(Skill::valueOf, Skill::name);
}