package net.vercte.slabstack.fabric.mixin;

import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vercte.slabstack.stack.StackedSlabBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StackedSlabBlockEntity.class)
public abstract class StackedSlabBlockEntitySelfMixin implements RenderDataBlockEntity {
    @Shadow
    public abstract Pair<@Nullable BlockState, @Nullable BlockState> getMaterials();

    public Object getRenderData() {
        return this.getMaterials();
    }
}
