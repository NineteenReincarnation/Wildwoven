package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = Wildwoven.MOD_ID, dist = Dist.CLIENT)
public final class WildwovenNeoForgeClient {
    public WildwovenNeoForgeClient(IEventBus modEventBus) {
        modEventBus.addListener(this::registerRenderers);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(WildwovenNeoForgeEntities.GLOWHOPPER.get(), NoopRenderer::new);
    }
}
