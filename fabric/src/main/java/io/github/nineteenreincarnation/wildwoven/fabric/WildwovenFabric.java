package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.fabricmc.api.ModInitializer;

public final class WildwovenFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Wildwoven.initialize();
    }
}
