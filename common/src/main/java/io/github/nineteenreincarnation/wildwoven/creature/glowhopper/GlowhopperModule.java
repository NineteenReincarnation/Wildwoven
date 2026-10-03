package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Entry boundary for Glowhopper-specific systems.
 *
 * <p>Concrete entity, AI, interaction and rendering systems remain inside the
 * glowhopper package. Unconfirmed creature mechanics are intentionally not
 * implemented here.</p>
 */
public final class GlowhopperModule {
    public static final String ID = "glowhopper";

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();

    private GlowhopperModule() {
    }

    public static void initialize() {
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        // Entity registration and confirmed mechanics attach here after the
        // remaining Glowhopper behavior decisions are resolved.
    }
}
