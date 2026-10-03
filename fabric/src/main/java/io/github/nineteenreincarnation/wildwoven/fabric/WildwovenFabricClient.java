package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.client.ClientGlowhopperCarryInput;
import io.github.nineteenreincarnation.wildwoven.client.glowhopper.GlowhopperModel;
import io.github.nineteenreincarnation.wildwoven.client.glowhopper.GlowhopperModelLayers;
import io.github.nineteenreincarnation.wildwoven.client.glowhopper.GlowhopperRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class WildwovenFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientGlowhopperCarryInput.installSender(ClientPlayNetworking::send);

        EntityModelLayerRegistry.registerModelLayer(
            GlowhopperModelLayers.ADULT,
            GlowhopperModel::createAdultLayer
        );
        EntityModelLayerRegistry.registerModelLayer(
            GlowhopperModelLayers.BABY,
            GlowhopperModel::createBabyLayer
        );

        EntityRendererRegistry.register(
            WildwovenFabricEntities.GLOWHOPPER,
            GlowhopperRenderer::new
        );
    }
}
