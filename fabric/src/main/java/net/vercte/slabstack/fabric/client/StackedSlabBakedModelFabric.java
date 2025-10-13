package net.vercte.slabstack.fabric.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class StackedSlabBakedModelFabric implements BakedModel {
    BakedModel top;
    BakedModel bottom;

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        List<BakedQuad> topQuads = top.getQuads(blockState, direction, randomSource);
        List<BakedQuad> bottomQuads = bottom.getQuads(blockState, direction, randomSource);

        List<BakedQuad> combinedQuads = new LinkedList<>(topQuads);
        combinedQuads.addAll(bottomQuads);

        return combinedQuads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return top.useAmbientOcclusion() || bottom.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return top.isGui3d() || bottom.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return top.usesBlockLight() || bottom.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true; // TODO: ?
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return top.getParticleIcon();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return top.getTransforms();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return top.getOverrides();
    }
}
