package net.vercte.slabstack.stack;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.vercte.slabstack.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

public class StackedSlabBlockEntity extends BlockEntity implements GameEventListener.Provider<StackedSlabBlockEntity.StackedSlabListener> {
    private BlockState topMaterial;
    private BlockState bottomMaterial;

    private StackedSlabListener listener;

    public StackedSlabBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.STACKED_SLAB.get(), blockPos, blockState);
        listener = new StackedSlabListener(blockState, new BlockPositionSource(blockPos));
    }

    public void setMaterials(BlockState top, BlockState bottom) {
        this.topMaterial = top;
        this.bottomMaterial = bottom;
    }

    public StackedSlabListener getListener() {
        return listener;
    }

    public static class StackedSlabListener implements GameEventListener {
        BlockState blockState;
        PositionSource positionSource;

        public StackedSlabListener(BlockState blockState, PositionSource positionSource) {
            this.blockState = blockState;
            this.positionSource = positionSource;
        }

        @Override
        public @NotNull PositionSource getListenerSource() {
            return positionSource;
        }

        @Override
        public int getListenerRadius() {
            return 1;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel, Holder<GameEvent> holder, GameEvent.Context context, Vec3 vec3) {
            if(holder.is(GameEvent.BLOCK_PLACE.key())) {
                LogUtils.getLogger().info("{}, {}", context.affectedState(), new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z));
            }
            return false;
        }

        @Override
        public @NotNull DeliveryMode getDeliveryMode() {
            return DeliveryMode.UNSPECIFIED;
        }
    }
}
