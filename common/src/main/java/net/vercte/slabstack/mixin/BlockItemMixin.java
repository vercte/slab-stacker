package net.vercte.slabstack.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.vercte.slabstack.ModBlocks;
import net.vercte.slabstack.stack.StackedSlabBlock;
import net.vercte.slabstack.stack.StackedSlabBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Shadow
    protected abstract @Nullable BlockState getPlacementState(BlockPlaceContext blockPlaceContext);

    @Shadow
    protected abstract boolean placeBlock(BlockPlaceContext blockPlaceContext, BlockState blockState);

    @Shadow
    protected abstract BlockState updateBlockStateFromTag(BlockPos blockPos, Level level, ItemStack itemStack, BlockState blockState);

    @Shadow
    public static boolean updateCustomBlockEntityTag(Level level, @Nullable Player player, BlockPos blockPos, ItemStack itemStack) {return false;}

    @Shadow
    private static void updateBlockEntityComponents(Level level, BlockPos blockPos, ItemStack itemStack) {}

    @Shadow
    protected abstract SoundEvent getPlaceSound(BlockState blockState);

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;updatePlacementContext(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/item/context/BlockPlaceContext;"), cancellable = true)
    public void stackSlabsIfPossible(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<InteractionResult> cir, @Local(argsOnly = true) BlockPlaceContext blockPlaceContextRefresh) {
        BlockPos targetBlockPos = blockPlaceContext.getClickedPos();
        BlockState originalBlockState = blockPlaceContext.getLevel().getBlockState(targetBlockPos);
        BlockState resultingBlockState = this.getPlacementState(blockPlaceContextRefresh);

        ItemStack heldItem = blockPlaceContext.getItemInHand();
        Item item = heldItem.getItem();

        if(!(originalBlockState.getBlock() instanceof SlabBlock)) return;

        if(resultingBlockState == null) return;
        if(originalBlockState.is(resultingBlockState.getBlock())) return;

        int light = StackedSlabBlockEntity.getLight(originalBlockState, resultingBlockState);
        BlockState stackedSlabBlockState = ModBlocks.STACKED_SLAB.get().defaultBlockState().setValue(StackedSlabBlock.LIGHT, light);

        if(item instanceof BlockItem blockItem && blockItem.getBlock() instanceof SlabBlock) {
            if (!this.placeBlock(blockPlaceContextRefresh, stackedSlabBlockState)) {
                cir.setReturnValue(InteractionResult.FAIL);
            } else {
                BlockPos blockPos = blockPlaceContextRefresh.getClickedPos();
                Level level = blockPlaceContextRefresh.getLevel();
                Player player = blockPlaceContextRefresh.getPlayer();
                ItemStack itemStack = blockPlaceContextRefresh.getItemInHand();
                BlockState finallyPlacedBlockState = level.getBlockState(blockPos);
                if (finallyPlacedBlockState.is(resultingBlockState.getBlock())) {
                    finallyPlacedBlockState = this.updateBlockStateFromTag(blockPos, level, itemStack, finallyPlacedBlockState);
                    updateCustomBlockEntityTag(level, player, blockPos, itemStack);
                    updateBlockEntityComponents(level, blockPos, itemStack);
                    finallyPlacedBlockState.getBlock().setPlacedBy(level, blockPos, finallyPlacedBlockState, player, itemStack);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
                    }
                }

                SoundType soundType = finallyPlacedBlockState.getSoundType();
                if(finallyPlacedBlockState.is(ModBlocks.STACKED_SLAB.get())) {
                    BlockEntity be = level.getBlockEntity(blockPos);
                    if(be instanceof StackedSlabBlockEntity ssbe) {
                        if(originalBlockState.getValue(SlabBlock.TYPE) == SlabType.TOP) {
                            ssbe.setMaterials(originalBlockState, resultingBlockState.setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                        } else {
                            ssbe.setMaterials(resultingBlockState.setValue(SlabBlock.TYPE, SlabType.TOP), originalBlockState);
                        }
                    }
                }

                level.playSound(player, blockPos, this.getPlaceSound(resultingBlockState), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(player, finallyPlacedBlockState));
                itemStack.consume(1, player);
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
    }


}
