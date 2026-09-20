package com.frostflamestudio.ascendant.system;

import com.frostflamestudio.ascendant.AscendantMod;
import com.frostflamestudio.ascendant.data.PlayerClass;
import com.frostflamestudio.ascendant.registry.ModAttachments;
import com.frostflamestudio.ascendant.entity.AvatarEntity;
import com.frostflamestudio.ascendant.registry.ModEntities;
import com.frostflamestudio.ascendant.registry.ModItems;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;

public class TutorialSystem {
    public static final ResourceKey<Level> WAITING = ResourceKey.create(
        Registries.DIMENSION,
        ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "waiting")
    );

    public static final ResourceKey<Level> TUTORIAL = ResourceKey.create(
        Registries.DIMENSION,
        ResourceLocation.fromNamespaceAndPath(AscendantMod.MODID, "tutorial")
    );

    private static final String AVATAR_TAG = "ascendant_avatar";
    private static final String HOLOGRAM_TAG = "ascendant_class_hologram";

    private static ListTag floatList(float... values) {
        var list = new ListTag();
        for (float value : values) {
            list.add(FloatTag.valueOf(value));
        }
        return list;
    }

    public static void placeWaitingRoom(ServerLevel level) {
        for (int x = -8; x <= 8; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -8; z <= 8; z++) {
                    boolean isShell =
                        x == -8 || x == 8
                        || z == -8 || z == 8
                        || y == 0
                        || y == 6;

                    if (isShell) {
                        level.setBlockAndUpdate(
                            new BlockPos(x, y, z),
                            Blocks.WHITE_CONCRETE.defaultBlockState()
                        );
                    }
                }
            }
        }

        level.setBlockAndUpdate(
            new BlockPos(0, 6, 0),
            Blocks.SEA_LANTERN.defaultBlockState()
        );
        level.setBlockAndUpdate(new BlockPos( 7, 6,  7), Blocks.SEA_LANTERN.defaultBlockState());
        level.setBlockAndUpdate(new BlockPos( 7, 6, -7), Blocks.SEA_LANTERN.defaultBlockState());
        level.setBlockAndUpdate(new BlockPos(-7, 6,  7), Blocks.SEA_LANTERN.defaultBlockState());
        level.setBlockAndUpdate(new BlockPos(-7, 6, -7), Blocks.SEA_LANTERN.defaultBlockState());

        //pads
        level.setBlockAndUpdate(
            new BlockPos(-5, 1, -5),
            Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState()
        );

        level.setBlockAndUpdate(
            new BlockPos(-3, 1, -5),
            Blocks.ORANGE_CONCRETE.defaultBlockState()
        );

        level.setBlockAndUpdate(
            new BlockPos(-1, 1, -5),
            Blocks.GRAY_CONCRETE.defaultBlockState()
        );

        level.setBlockAndUpdate(
            new BlockPos(1, 1, -5),
            Blocks.LIME_CONCRETE.defaultBlockState()
        );

        level.setBlockAndUpdate(
            new BlockPos(3, 1, -5),
            Blocks.PURPLE_CONCRETE.defaultBlockState()
        );

        level.setBlockAndUpdate(
            new BlockPos(5, 1, -5),
            Blocks.PINK_CONCRETE.defaultBlockState()
        );

        //avatar
        placeAvatar(level);
        placeHolograms(level);
    }

    public static void placeAvatar(ServerLevel level) {
        var search = new AABB(-8, 0, -8, 8, 7, 8);

        var avatars = level.getEntitiesOfClass(AvatarEntity.class, search);
        if (!avatars.isEmpty()) {
            return;
        }

        for (var stand : level.getEntitiesOfClass(ArmorStand.class, search)) {
            if (stand.getTags().contains(AVATAR_TAG)) {
                stand.discard();
            }
        }

        var avatar = ModEntities.AVATAR.get().create(level);
        if (avatar == null) {
            return;
        }

        avatar.moveTo(0.5, 1.0, 3.5, 180.0F, 0.0F);
        avatar.setYBodyRot(180.0F);
        avatar.setYHeadRot(180.0F);
        avatar.setCustomName(Component.translatable("npc.ascendant.avatar"));
        avatar.setCustomNameVisible(true);
        level.addFreshEntity(avatar);
    }

    public static void placeHolograms(ServerLevel level) {
        var search = new AABB(-8, 0, -8, 8, 7, 8);
        var holograms = level.getEntitiesOfClass(Display.TextDisplay.class, search);
    
        for (var hologram : holograms) {
            if (hologram.getTags().contains(HOLOGRAM_TAG)) {
                hologram.discard();
            }
        }

        var itemHolograms = level.getEntitiesOfClass(Display.ItemDisplay.class, search);
        for (var hologram : itemHolograms) {
            if (hologram.getTags().contains(HOLOGRAM_TAG)) {
                hologram.discard();
            }
        }
    
        placeClassHologram(level, -5, -5, PlayerClass.LIGHT_WARRIOR);
        placeClassHologram(level, -3, -5, PlayerClass.MEDIUM_WARRIOR);
        placeClassHologram(level, -1, -5, PlayerClass.HEAVY_WARRIOR);
        placeClassHologram(level, 1, -5, PlayerClass.ARCHER);
        placeClassHologram(level, 3, -5, PlayerClass.CASTER);
        placeClassHologram(level, 5, -5, PlayerClass.HEALER);
    }

    private static void placeClassItemHologram(
        ServerLevel level,
        int x,
        int z,
        PlayerClass playerClass
    ) {
        ItemStack stack = switch (playerClass) {
            case LIGHT_WARRIOR -> new ItemStack(ModItems.STEEL_DAGGER.get());
            case MEDIUM_WARRIOR -> new ItemStack(ModItems.STEEL_LONGSWORD.get());
            case HEAVY_WARRIOR -> new ItemStack(ModItems.STEEL_ARMING_SWORD.get());
            case ARCHER -> new ItemStack(ModItems.WOODEN_BOW.get());
            case CASTER -> new ItemStack(ModItems.WOODEN_STAFF.get());
            case HEALER -> new ItemStack(ModItems.HOLY_SEAL.get());
            default -> null;
        };
        if (stack == null) {
            return;
        }
    
        var hologram = EntityType.ITEM_DISPLAY.create(level);
        if (hologram == null) {
            return;
        }
    
        hologram.moveTo(x + 0.5, 3.6, z + 0.5, 0.0F, 0.0F);
        hologram.addTag(HOLOGRAM_TAG);
    
        var nbt = new CompoundTag();
        hologram.saveWithoutId(nbt);
        nbt.put("item", stack.save(level.registryAccess()));
        nbt.putString("item_display", "gui");
        nbt.putString("billboard", "center");
    
        var transformation = new CompoundTag();
        transformation.put("translation", floatList(0.0F, 0.0F, 0.0F));
        transformation.put("left_rotation", floatList(0.0F, 0.0F, 0.0F, 1.0F));
        transformation.put("right_rotation", floatList(0.0F, 0.0F, 0.0F, 1.0F));
        transformation.put("scale", floatList(1.0F, 1.0F, 1.0F));
        nbt.put("transformation", transformation);
    
        hologram.load(nbt);
        level.addFreshEntity(hologram);
    }

    private static void placeClassHologram(
        ServerLevel level,
        int x,
        int z,
        PlayerClass playerClass
    ) {
        var hologram = EntityType.TEXT_DISPLAY.create(level);
        if (hologram == null) {
            return;
        }
    
        hologram.moveTo(x + 0.5, 3.0, z + 0.5, 0.0F, 0.0F);
        hologram.addTag(HOLOGRAM_TAG);
    
        var nbt = new CompoundTag();
        hologram.saveWithoutId(nbt);
        nbt.putString(
            "text",
            Component.Serializer.toJson(
                playerClass.getDisplayName(),
                level.registryAccess()
            )
        );
        nbt.putString("billboard", "center");
        nbt.putInt("background", 0);
    
        var transformation = new CompoundTag();
        transformation.put("translation", floatList(0.0F, 0.0F, 0.0F));
        transformation.put("left_rotation", floatList(0.0F, 0.0F, 0.0F, 1.0F));
        transformation.put("right_rotation", floatList(0.0F, 0.0F, 0.0F, 1.0F));
        transformation.put("scale", floatList(1.0F, 1.0F, 1.0F));
        nbt.put("transformation", transformation);
    
        hologram.load(nbt);
        level.addFreshEntity(hologram);
    
        placeClassItemHologram(level, x, z, playerClass);
    }

    public static void sendToWaiting(ServerPlayer player) {
        var server = player.getServer();
        if (server == null) {
            return;
        }

        var level = server.getLevel(TutorialSystem.WAITING);
        if (level == null) {
            return;
        }

        TutorialSystem.placeWaitingRoom(level);
        player.teleportTo(level, 0.5, 1.0, 0.5, 0.0f, 0.0f);
    }

    public static void sendToTutorial(ServerPlayer player) {
        var server = player.getServer();
        if (server == null) {
            return;
        }

        var level = server.getLevel(TutorialSystem.TUTORIAL);
        if (level == null) {
            return;
        }

        player.teleportTo(level, 0.5, 1.0, 0.5, 0.0f, 0.0f);
    }

    public static PlayerClass classFromPad(BlockState state) {
        if (state.is(Blocks.LIGHT_BLUE_CONCRETE)) {
            return PlayerClass.LIGHT_WARRIOR;
        }
        if (state.is(Blocks.ORANGE_CONCRETE)) {
            return PlayerClass.MEDIUM_WARRIOR;
        }
        if (state.is(Blocks.GRAY_CONCRETE)) {
            return PlayerClass.HEAVY_WARRIOR;
        }
        if (state.is(Blocks.LIME_CONCRETE)) {
            return PlayerClass.ARCHER;
        }
        if (state.is(Blocks.PURPLE_CONCRETE)) {
            return PlayerClass.CASTER;
        }
        if (state.is(Blocks.PINK_CONCRETE)) {
            return PlayerClass.HEALER;
        }

        return null;
    }

    public static void onPlayerTick(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        
        if (!player.level().dimension().equals(WAITING)) {
            return;
        }

        if (player.getData(ModAttachments.PLAYER_DATA).getAvatarLine() < 4) {
            return;
        }

        var padPos = player.blockPosition().below();
        var state = player.level().getBlockState(padPos);
        var playerClass = classFromPad(state);

        if (playerClass != null) {
            player.displayClientMessage(playerClass.getDisplayName(), true);
        }
    }

    public static boolean isAvatar(Entity entity) {
        return entity instanceof AvatarEntity;
    }

    public static void talkToAvatar(Player player) {
        var playerData = player.getData(ModAttachments.PLAYER_DATA);
        int line = playerData.getAvatarLine();
        int shown = Math.min(line, 3);

        player.sendSystemMessage(
            Component.translatable("message.ascendant.avatar.line" + shown)
        );
    
        if (line < 3) {
            playerData.setAvatarLine(line + 1);
        }
        else {
            playerData.setAvatarLine(4);
        }

        player.syncData(ModAttachments.PLAYER_DATA);
    }
}
