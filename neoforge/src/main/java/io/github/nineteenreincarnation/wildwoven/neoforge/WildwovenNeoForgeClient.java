package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.client.ClientGlowhopperCarryInput;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@Mod(value = Wildwoven.MOD_ID, dist = Dist.CLIENT)
public final class WildwovenNeoForgeClient {
    public WildwovenNeoForgeClient(IEventBus modEventBus) {
        ClientGlowhopperCarryInput.installSender(ClientPacketDistributor::sendToServer);
        modEventBus.addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WildwovenNeoForgeEntities.GLOWHOPPER.get(), NoopRenderer::new);
    }
}
