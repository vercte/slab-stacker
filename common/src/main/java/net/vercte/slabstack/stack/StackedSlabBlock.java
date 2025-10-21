package net.vercte.slabstack.stack;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StackedSlabBlock extends Block implements EntityBlock {
    public static IntegerProperty LIGHT = IntegerProperty.create("light", 0, 15);

    public StackedSlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIGHT, 0));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(LIGHT, 0);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new StackedSlabBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (l, p, s, be) -> {
            if(be instanceof StackedSlabBlockEntity ss && ss.isDirty()) {
                ss.updateMaterials();
            }
        };
    }

    public Pair<@Nullable BlockState, @Nullable BlockState> getMaterial(BlockGetter getter, BlockPos pos) {
        if(getter.getBlockEntity(pos) instanceof StackedSlabBlockEntity be) return be.getMaterials();
        return Pair.of(null, null);
    }

    // TODO: NOT WORKIGN!!!
    @Override
    @SuppressWarnings("DataFlowIssue")
    protected @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        Pair<@Nullable BlockState, @Nullable BlockState> materials = this.getMaterial(blockGetter, blockPos);
        if(materials.left() == null || materials.right() == null) return Shapes.block();

        VoxelShape topMaterial = materials.left().getOcclusionShape(blockGetter, blockPos);
        VoxelShape bottomMaterial = materials.right().getOcclusionShape(blockGetter, blockPos);

        return Shapes.or(topMaterial, bottomMaterial);
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean propagatesSkylightDown(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
        Pair<@Nullable BlockState, @Nullable BlockState> materials = this.getMaterial(blockGetter, blockPos);
        if(materials.left() == null || materials.right() == null) return false;

        return materials.left().propagatesSkylightDown(blockGetter, blockPos) || materials.right().propagatesSkylightDown(blockGetter, blockPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(LIGHT); }
}
