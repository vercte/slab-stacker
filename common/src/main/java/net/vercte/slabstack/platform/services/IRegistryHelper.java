package net.vercte.slabstack.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public interface IRegistryHelper {
    <T> Supplier<T> register(Registry<? super T> registry, String id, Supplier<T> object);

    <T extends BlockEntity> Supplier<BlockEntityType<T>> getBlockEntityType(BlockEntitySupplier<T> be, Supplier<Block> block);

    @FunctionalInterface
    interface BlockEntitySupplier<T extends BlockEntity> {
        T create(BlockPos blockPos, BlockState blockState);
    }
}
