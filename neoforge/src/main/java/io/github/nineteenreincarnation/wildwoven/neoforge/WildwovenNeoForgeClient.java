package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.client.ClientGlowhopperCarryInput;
import io.github.nineteenreincarnation.wildwoven.client.glowhopper.GlowhopperModel;
import io.github.nineteenreincarnation.wildwoven.client.glowhopper.GlowhopperModelLayers;
import io.github.nineteenreincarnation.wildwoven.client.glowhopper.GlowhopperRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@Mod(value = Wildwoven.MOD_ID, dist = Dist.CLIENT)
public final class WildwovenNeoForgeClient {
    public WildwovenNeoForgeClient(IEventBus modEventBus) {
        ClientGlowhopperCarryInput.installSender(ClientPacketDistributor::sendToServer);
        modEventBus.addListener(this::registerLayers);
        modEventBus.addListener(this::registerRenderers);
    }

    private void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GlowhopperModelLayers.ADULT, GlowhopperModel::createAdultLayer);
        event.registerLayerDefinition(GlowhopperModelLayers.BABY, GlowhopperModel::createBabyLayer);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WildwovenNeoForgeEntities.GLOWHOPPER.get(), GlowhopperRenderer::new);
    }
}
