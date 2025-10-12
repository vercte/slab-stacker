package net.vercte.slabstack.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlabBlock.class)
public class SlabBlockMixin {
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
