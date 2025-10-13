package net.vercte.slabstack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.vercte.slabstack.platform.Services;

import java.util.function.Supplier;

// TODO: This is just for testing
public class ModItems {
    public static final Supplier<BlockItem> LIGHT_SLAB = register(() ->
            new BlockItem(ModBlocks.LIGHT_SLAB.get(), new Item.Properties()),
            "light_slab"
    );

    public static <T extends Item> Supplier<T> register(Supplier<T> item, String id) {
        return Services.REGISTRY.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void init() {}
}
