package net.vercte.slabstack.stack;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
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


    public Pair<@Nullable BlockState, @Nullable BlockState> getMaterial(BlockGetter getter, BlockPos pos) {
        if(getter.getBlockEntity(pos) instanceof StackedSlabBlockEntity be) return be.getMaterials();
        return Pair.of(null, null);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(LIGHT); }
}
