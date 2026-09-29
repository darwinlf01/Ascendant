package com.frostflamestudio.ascendant.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class HolySealCurio implements ICurioItem {
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(
        SlotContext slotContext,
        ResourceLocation id,
        ItemStack stack
    ) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = LinkedHashMultimap.create();
        modifiers.put(
            Attributes.MAX_HEALTH,
            new AttributeModifier(id, 2.0, AttributeModifier.Operation.ADD_VALUE)
        );
        return modifiers;
    }
}