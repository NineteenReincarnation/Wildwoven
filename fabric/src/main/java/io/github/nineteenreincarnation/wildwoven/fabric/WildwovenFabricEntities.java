package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperEntity;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperModule;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

public final class WildwovenFabricEntities {
    private static final ResourceKey<EntityType<?>> GLOWHOPPER_KEY = ResourceKey.create(
        Registries.ENTITY_TYPE,
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, GlowhopperModule.ID)
    );

    public static final EntityType<GlowhopperEntity> GLOWHOPPER = Registry.register(
        BuiltInRegistries.ENTITY_TYPE,
        GLOWHOPPER_KEY,
        EntityType.Builder.of(GlowhopperEntity::new, MobCategory.CREATURE)
            .sized(0.7F, 0.5F)
            .noLootTable()
            .build(GLOWHOPPER_KEY)
    );

    private WildwovenFabricEntities() {
    }

    public static void register() {
        GlowhopperModule.bindEntityType(() -> GLOWHOPPER);

        FabricDefaultAttributeRegistry.register(GLOWHOPPER, GlowhopperEntity.createAttributes());
        SpawnPlacements.register(
            GLOWHOPPER,
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            GlowhopperEntity::checkSpawnRules
        );

        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(Biomes.LUSH_CAVES),
            MobCategory.CREATURE,
            GLOWHOPPER,
            80,
            2,
            4
        );

        BiomeModifications.addSpawn(
            BiomeSelectors.foundInOverworld().and(BiomeSelectors.excludeByKey(Biomes.LUSH_CAVES)),
            MobCategory.CREATURE,
            GLOWHOPPER,
            8,
            2,
            4
        );
    }
}
