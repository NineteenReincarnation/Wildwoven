package io.github.nineteenreincarnation.wildwoven.creature;

import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperModule;
import java.util.concurrent.atomic.AtomicBoolean;

public final class WildwovenCreatures {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    private WildwovenCreatures() {
    }

    public static void initialize() {
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        GlowhopperModule.initialize();
    }
}
