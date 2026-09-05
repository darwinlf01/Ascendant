package com.frostflamestudio.ascendant;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AscendantMod.MODID, dist = Dist.CLIENT)
public class AscendantClient {
    public AscendantClient(ModContainer container) {
    }
}
