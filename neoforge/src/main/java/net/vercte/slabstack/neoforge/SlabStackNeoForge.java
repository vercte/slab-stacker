package net.vercte.slabstack.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.vercte.slabstack.SlabStack;
import net.neoforged.fml.common.Mod;
import net.vercte.slabstack.neoforge.platform.services.NeoforgeRegistryHelper;

@Mod(SlabStack.ID)
public final class SlabStackNeoForge {
    public SlabStackNeoForge(IEventBus bus) {
        SlabStack.init();

        NeoforgeRegistryHelper.registerAll(bus);
    }
}
