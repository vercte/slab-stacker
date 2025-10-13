package net.vercte.slabstack.fabric.platform.services;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vercte.slabstack.SlabStack;
import net.vercte.slabstack.platform.services.IRegistryHelper;

import java.util.function.Supplier;


public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, String id, Supplier<T> item) {
        T value = Registry.register(registry, SlabStack.at(id), item.get());
        return () -> value;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> getBlockEntityType(BlockEntitySupplier<T> be, Supplier<Block> block) {
        return () -> BlockEntityType.Builder.of(be::create, block.get()).build(null);

    }
}
