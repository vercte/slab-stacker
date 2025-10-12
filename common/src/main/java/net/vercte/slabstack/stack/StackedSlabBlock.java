package net.vercte.slabstack.stack;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class StackedSlabBlock extends Block implements EntityBlock {
    public StackedSlabBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new StackedSlabBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel, T blockEntity) {
        if(blockEntity instanceof StackedSlabBlockEntity be) return be.getListener();
        return null;
    }
}
