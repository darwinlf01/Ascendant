package com.frostflamestudio.ascendant.item;

import java.util.List;

import com.frostflamestudio.ascendant.system.QuiverSystem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LeatherQuiverItem extends Item {
    public static final int ARROW_CAPACITY = 16;
    public static final String ARROWS_KEY = "arrows";

    public LeatherQuiverItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createStack() {
        ItemStack stack = new ItemStack(com.frostflamestudio.ascendant.registry.ModItems.LEATHER_QUIVER.get());
        CompoundTag tag = new CompoundTag();
        tag.putInt(ARROWS_KEY, ARROW_CAPACITY);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static int getArrows(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return 0;
        }
        return data.copyTag().getInt(ARROWS_KEY);
    }

    public static void setArrows(ItemStack stack, int arrows) {
        int clamped = Math.max(0, Math.min(arrows, ARROW_CAPACITY));
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(ARROWS_KEY, clamped));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }
        if (QuiverSystem.infuse(player, stack)) {
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(
            "item.ascendant.leather_quiver.arrows",
            getArrows(stack),
            ARROW_CAPACITY
        ));
    }
}