package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import java.util.Objects;
import java.util.function.Supplier;

public final class GlowhopperLightModule {
    public static final String ID = "glowhopper_light";

    private static Supplier<GlowhopperLightBlock> block;

    private GlowhopperLightModule() {
    }

    public static void bindBlock(Supplier<GlowhopperLightBlock> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        if (block != null && block != supplier) {
            throw new IllegalStateException("Glowhopper light block is already bound");
        }
        block = supplier;
    }

    static GlowhopperLightBlock block() {
        if (block == null) {
            throw new IllegalStateException("Glowhopper light block has not been bound by the active loader");
        }
        return block.get();
    }
}
