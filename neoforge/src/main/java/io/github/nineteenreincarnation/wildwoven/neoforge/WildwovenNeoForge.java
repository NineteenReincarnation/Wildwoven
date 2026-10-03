package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.ServerGlowhopperCarry;
import io.github.nineteenreincarnation.wildwoven.network.RemoveHeadGlowhopperPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(Wildwoven.MOD_ID)
public final class WildwovenNeoForge {
    public WildwovenNeoForge(IEventBus modEventBus) {
        WildwovenNeoForgeEntities.register(modEventBus);
        WildwovenNeoForgeItems.register(modEventBus);
        WildwovenNeoForgeCreativeTabs.register(modEventBus);
        Wildwoven.initialize();
        modEventBus.addListener(this::registerPayloads);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Wildwoven.MOD_ID).optional();

        registrar.playToServer(
            RemoveHeadGlowhopperPayload.TYPE,
            RemoveHeadGlowhopperPayload.STREAM_CODEC,
            (payload, context) -> context.enqueueWork(() -> {
                if (context.flow() == PacketFlow.SERVERBOUND
                    && context.player() instanceof ServerPlayer player) {
                    ServerGlowhopperCarry.removeFromHead(player);
                }
            })
        );
    }
}
