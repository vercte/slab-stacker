package net.vercte.slabstack.fabric;

import net.vercte.slabstack.SlabStack;
import net.fabricmc.api.ModInitializer;

public final class SlabStackFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        SlabStack.init();
    }
}
