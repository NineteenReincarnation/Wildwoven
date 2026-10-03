package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Wildwoven.MOD_ID)
public final class WildwovenNeoForge {
    public WildwovenNeoForge(IEventBus modEventBus) {
        WildwovenNeoForgeEntities.register(modEventBus);
        Wildwoven.initialize();
    }
}
