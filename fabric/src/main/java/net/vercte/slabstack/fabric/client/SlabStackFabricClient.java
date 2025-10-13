package net.vercte.slabstack.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public final class SlabStackFabricClient implements ClientModInitializer {
    public static final SlabStackAfterBake AFTER_BAKE = new SlabStackAfterBake();

    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(ctx -> ctx.modifyModelAfterBake().register(AFTER_BAKE));
    }
}
