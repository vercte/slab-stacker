package net.vercte.slabstack.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.vercte.slabstack.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlabBlock.class)
public class SlabBlockMixin {
    @Inject(method = "getStateForPlacement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"), cancellable = true)
    public void stackSlabs(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<BlockState> cir) {
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPos);
        if(blockState.getBlock() instanceof SlabBlock) {
            cir.setReturnValue(ModBlocks.STACKED_SLAB.get().defaultBlockState());
        }
    }

    @ModifyExpressionValue(method = "canBeReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    public boolean replaceIfOtherSlab(boolean original, BlockState blockState, BlockPlaceContext blockPlaceContext) {
        ItemStack itemStack = blockPlaceContext.getItemInHand();
        Item item = itemStack.getItem();

        if(item instanceof BlockItem blockItem) {
            return original || blockItem.getBlock() instanceof SlabBlock;
        }
        return original;
    }
}
