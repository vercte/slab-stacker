package net.vercte.slabstack.neoforge.platform.services;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vercte.slabstack.SlabStack;
import net.vercte.slabstack.platform.services.IRegistryHelper;

import java.util.HashMap;
import java.util.function.Supplier;

public class NeoforgeRegistryHelper implements IRegistryHelper {
    private static final HashMap<Registry<?>, DeferredRegister<?>> REGISTERS = new HashMap<>();

    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String id, Supplier<T> item) {
        return getOrCreateRegistrar(registry).register(id, item);
    }

    @SuppressWarnings("unchecked")
    private <T> DeferredRegister<T> getOrCreateRegistrar(Registry<T> registry) {
        DeferredRegister<T> existing = (DeferredRegister<T>) REGISTERS.get(registry);
        if(existing != null) return existing;

        DeferredRegister<T> created = DeferredRegister.create(registry, SlabStack.ID);
        REGISTERS.put(registry, created);
        return created;
    }

    public static void registerAll(IEventBus bus) {
        REGISTERS.forEach((r, d) -> {
            d.register(bus);
        });
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> getBlockEntityType(BlockEntitySupplier<T> be, Supplier<Block> block) {
        return () -> BlockEntityType.Builder.of(be::create, block.get()).build(null);
    }
}
