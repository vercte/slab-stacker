package net.vercte.slabstack.stack;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vercte.slabstack.ModBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StackedSlabBlockEntity extends BlockEntity {
    @Nullable private BlockState topMaterial;
    @Nullable private BlockState bottomMaterial;

    public StackedSlabBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.STACKED_SLAB.get(), blockPos, blockState);
    }

    public void setMaterials(@NotNull BlockState top, @NotNull BlockState bottom) {
        this.topMaterial = top;
        this.bottomMaterial = bottom;

        Level level = getLevel();
        if(level == null || level.isClientSide()) return;

        BlockState newState = getBlockState().setValue(StackedSlabBlock.LIGHT, getLight(top, bottom));
        level.setBlock(getBlockPos(), newState, 11);
    }

    public Pair<@Nullable BlockState, @Nullable BlockState> getMaterials() {
        return Pair.of(topMaterial, bottomMaterial);
    }

    public static int getLight(BlockState top, BlockState bottom) {
        int light = 0;
        if(top != null) light = top.getLightEmission();
        if(bottom != null) light = Math.max(bottom.getLightEmission(), light);

        return light;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        BlockState top = null;
        BlockState bottom = null;

        if(compoundTag.contains("TopMaterial", CompoundTag.TAG_COMPOUND))
            top = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), compoundTag.getCompound("TopMaterial"));

        if(compoundTag.contains("BottomMaterial", CompoundTag.TAG_COMPOUND))
            bottom = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), compoundTag.getCompound("BottomMaterial"));

        if(top != null && bottom != null) setMaterials(top, bottom);
        LogUtils.getLogger().info("top {}, bottom {}", topMaterial, bottomMaterial);

        super.loadAdditional(compoundTag, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        if(topMaterial != null) compoundTag.put("TopMaterial", NbtUtils.writeBlockState(topMaterial));
        if(bottomMaterial != null) compoundTag.put("BottomMaterial", NbtUtils.writeBlockState(bottomMaterial));
        super.saveAdditional(compoundTag, provider);
    }
}
