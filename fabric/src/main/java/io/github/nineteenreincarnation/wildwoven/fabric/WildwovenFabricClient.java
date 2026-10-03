package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.client.ClientGlowhopperCarryInput;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;

public final class WildwovenFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientGlowhopperCarryInput.installSender(ClientPlayNetworking::send);
        EntityRendererRegistry.register(WildwovenFabricEntities.GLOWHOPPER, NoopRenderer::new);
    }
}
