package net.vercte.slabstack.fabric.client;

import io.github.fabricators_of_create.porting_lib.models.CustomParticleIconModel;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class StackedSlabBakedModelFabric implements BakedModel, CustomParticleIconModel {
    BakedModel topModel = null;
    BakedModel bottomModel = null;

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource) {
        if(topModel == null || bottomModel == null) return List.of();

        List<BakedQuad> topQuads = topModel.getQuads(blockState, direction, randomSource);
        List<BakedQuad> bottomQuads = bottomModel.getQuads(blockState, direction, randomSource);

        List<BakedQuad> combinedQuads = new LinkedList<>(topQuads);
        combinedQuads.addAll(bottomQuads);

        return combinedQuads;
    }

    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        Object data = blockView.getBlockEntityRenderData(pos);

        Pair<@Nullable BlockState, @Nullable BlockState> pair = dataToPair(data);
        if(pair == null) {
            BakedModel.super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            return;
        }

        BlockState top = pair.left();
        BlockState bottom = pair.right();
        if(top == null || bottom == null) {
            BakedModel.super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            return;
        }

        this.topModel = getBlockModel(top);
        this.emitBlockQuadsInner(blockView, pos, randomSupplier, context, top, this.topModel);

        this.bottomModel = getBlockModel(bottom);
        this.emitBlockQuadsInner(blockView, pos, randomSupplier, context, bottom, this.bottomModel);
    }

    private void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context,
                                     BlockState innerState, BakedModel innerModel) {
        context.pushTransform(MaterialFixer.create(innerState));
        innerModel.emitBlockQuads(blockView, innerState, pos, randomSupplier, context);
        context.popTransform();
    }

    @SuppressWarnings("unchecked")
    private @Nullable Pair<@Nullable BlockState, @Nullable BlockState> dataToPair(Object data) {
        if(!(data instanceof Pair)) return null;
        return (Pair<@Nullable BlockState, @Nullable BlockState>) data;
    }

    private BakedModel getBlockModel(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        if(topModel == null) return false;
        return topModel.isGui3d() || bottomModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        if(topModel == null) return false;
        return topModel.usesBlockLight() || bottomModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return topModel.getParticleIcon();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(Object data) {
        Pair<@Nullable BlockState, @Nullable BlockState> pair = dataToPair(data);
        if(pair == null) return this.getParticleIcon();

        BakedModel top = getBlockModel(pair.left());
        return top.getParticleIcon();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return topModel.getTransforms();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return topModel.getOverrides();
    }

    // thanks Create Fabric
    private record MaterialFixer(RenderMaterial material) implements RenderContext.QuadTransform {
        @Override
        public boolean transform(MutableQuadView quad) {
            if (quad.material().blendMode() == BlendMode.DEFAULT) {
                quad.material(material);
            }
            return true;
        }

        public static MaterialFixer create(BlockState materialState) {
            RenderType type = ItemBlockRenderTypes.getChunkRenderType(materialState);
            BlendMode blendMode = BlendMode.fromRenderLayer(type);
            MaterialFinder finder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).materialFinder();

            return new MaterialFixer(finder.blendMode(blendMode).find());
        }
    }
}
