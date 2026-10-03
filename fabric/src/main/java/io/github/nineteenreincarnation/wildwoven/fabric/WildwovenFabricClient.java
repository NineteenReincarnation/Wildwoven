package io.github.nineteenreincarnation.wildwoven.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;

public final class WildwovenFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(WildwovenFabricEntities.GLOWHOPPER, NoopRenderer::new);
    }
}
