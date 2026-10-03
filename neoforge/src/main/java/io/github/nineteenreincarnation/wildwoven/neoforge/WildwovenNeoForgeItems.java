package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WildwovenNeoForgeItems {
    private static final ResourceKey<CreativeModeTab> SPAWN_EGGS_TAB = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB,
        Identifier.withDefaultNamespace("spawn_eggs")
    );

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Wildwoven.MOD_ID);

    public static final DeferredItem<SpawnEggItem> GLOWHOPPER_SPAWN_EGG = ITEMS.registerItem(
        "glowhopper_spawn_egg",
        SpawnEggItem::new,
        properties -> properties.spawnEgg(WildwovenNeoForgeEntities.GLOWHOPPER.get())
    );

    private WildwovenNeoForgeItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(WildwovenNeoForgeItems::addCreativeTabContents);
    }

    private static void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(SPAWN_EGGS_TAB)) {
            event.accept(GLOWHOPPER_SPAWN_EGG.get());
        }
    }
}
