package net.vercte.slabstack;

import net.minecraft.resources.ResourceLocation;

public final class SlabStack {
    public static final String ID = "slabstack";

    public static void init() {
        ModBlocks.init();
        ModBlockEntities.init();
        ModItems.init();
    }

    public static ResourceLocation at(String location) {
        return ResourceLocation.fromNamespaceAndPath(ID, location);
    }
}
