package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.ServerGlowhopperCarry;
import io.github.nineteenreincarnation.wildwoven.network.RemoveHeadGlowhopperPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class WildwovenFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        WildwovenFabricEntities.register();
        WildwovenFabricItems.register();
        WildwovenFabricCreativeTabs.register();
        Wildwoven.initialize();

        PayloadTypeRegistry.serverboundPlay().register(
            RemoveHeadGlowhopperPayload.TYPE,
            RemoveHeadGlowhopperPayload.STREAM_CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
            RemoveHeadGlowhopperPayload.TYPE,
            (payload, context) -> ServerGlowhopperCarry.removeFromHead(context.player())
        );
    }
}
