package io.github.nineteenreincarnation.wildwoven;

import io.github.nineteenreincarnation.wildwoven.creature.WildwovenCreatures;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Wildwoven {
    public static final String MOD_ID = "wildwoven";

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    private Wildwoven() {
    }

    public static void initialize() {
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        WildwovenCreatures.initialize();
    }
}
