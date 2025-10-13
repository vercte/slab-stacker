package net.vercte.slabstack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.vercte.slabstack.platform.Services;
import net.vercte.slabstack.stack.StackedSlabBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final Supplier<Block> STACKED_SLAB = register(() -> new StackedSlabBlock(
                BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(StackedSlabBlock.LIGHT))
            ),
            "stacked_slab"
    );

    public static final Supplier<Block> LIGHT_SLAB = register(() -> new SlabBlock(
                    BlockBehaviour.Properties.of().lightLevel(state -> 15)
            ),
            "light_slab"
    );

    public static <T extends Block> Supplier<T> register(Supplier<T> block, String id) {
        return Services.REGISTRY.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static void init() {}
}
