package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;

public final class GlowhopperModule {
    public static final String ID = "glowhopper";

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();
    private static Supplier<EntityType<GlowhopperEntity>> entityType;

    private GlowhopperModule() {
    }

    public static void bindEntityType(Supplier<EntityType<GlowhopperEntity>> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        if (entityType != null && entityType != supplier) {
            throw new IllegalStateException("Glowhopper entity type is already bound");
        }

        entityType = supplier;
    }

    public static EntityType<GlowhopperEntity> entityType() {
        if (entityType == null) {
            throw new IllegalStateException("Glowhopper entity type has not been bound by the active loader");
        }

        return entityType.get();
    }

    public static void initialize() {
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        if (entityType == null) {
            throw new IllegalStateException("Glowhopper must be registered before Wildwoven common initialization");
        }
    }
}
