package net.vercte.slabstack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vercte.slabstack.platform.Services;
import net.vercte.slabstack.stack.StackedSlabBlockEntity;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final Supplier<BlockEntityType<StackedSlabBlockEntity>> STACKED_SLAB = register(
            Services.REGISTRY.getBlockEntityType(StackedSlabBlockEntity::new, ModBlocks.STACKED_SLAB),
            "stacked_slab"
    );

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(Supplier<BlockEntityType<T>> blockEntity, String id) {
        return Services.REGISTRY.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntity);
    }

    public static void init() {}
}
