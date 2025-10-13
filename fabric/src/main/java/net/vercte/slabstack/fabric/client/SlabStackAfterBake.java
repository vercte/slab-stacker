package net.vercte.slabstack.fabric.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier.AfterBake;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.Nullable;

// We're costco guys. Of course we get After Bake
public class SlabStackAfterBake implements AfterBake {
    @Override
    public @Nullable BakedModel modifyModelAfterBake(@Nullable BakedModel model, Context context) {
        if(isStackedSlab(context)) {
            return new StackedSlabBakedModelFabric();
        }
        return model;
    }

    private boolean isStackedSlab(Context context) {
        if(context.topLevelId() != null && context.topLevelId().id().getPath().equals("stacked_slab")) return true;
        else return context.resourceId() != null && context.resourceId().getPath().equals("block/stacked_slab");
    }
}
