package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperEntity;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperModule;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WildwovenNeoForgeEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.createEntities(Wildwoven.MOD_ID);

    private static final ResourceKey<EntityType<?>> GLOWHOPPER_KEY = ResourceKey.create(
        Registries.ENTITY_TYPE,
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, GlowhopperModule.ID)
    );

    public static final Supplier<EntityType<GlowhopperEntity>> GLOWHOPPER = ENTITY_TYPES.register(
        GlowhopperModule.ID,
        () -> EntityType.Builder.of(GlowhopperEntity::new, MobCategory.CREATURE)
            .sized(0.7F, 0.5F)
            .noLootTable()
            .build(GLOWHOPPER_KEY)
    );

    private WildwovenNeoForgeEntities() {
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
        GlowhopperModule.bindEntityType(GLOWHOPPER);
        modEventBus.addListener(WildwovenNeoForgeEntities::registerAttributes);
        modEventBus.addListener(WildwovenNeoForgeEntities::registerSpawnPlacements);
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(GLOWHOPPER.get(), GlowhopperEntity.createAttributes().build());
    }

    private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
            GLOWHOPPER.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            GlowhopperEntity::checkSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}
