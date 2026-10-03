package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class WildwovenFabricItems {
    private static final String GLOWHOPPER_SPAWN_EGG_ID = "glowhopper_spawn_egg";

    private static final ResourceKey<Item> GLOWHOPPER_SPAWN_EGG_KEY = ResourceKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, GLOWHOPPER_SPAWN_EGG_ID)
    );

    private static final ResourceKey<CreativeModeTab> SPAWN_EGGS_TAB = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB,
        Identifier.withDefaultNamespace("spawn_eggs")
    );

    public static final SpawnEggItem GLOWHOPPER_SPAWN_EGG = Registry.register(
        BuiltInRegistries.ITEM,
        GLOWHOPPER_SPAWN_EGG_KEY,
        new SpawnEggItem(
            new Item.Properties()
                .setId(GLOWHOPPER_SPAWN_EGG_KEY)
                .spawnEgg(WildwovenFabricEntities.GLOWHOPPER)
        )
    );

    private WildwovenFabricItems() {
    }

    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(SPAWN_EGGS_TAB)
            .register(output -> output.accept(GLOWHOPPER_SPAWN_EGG));
    }
}
