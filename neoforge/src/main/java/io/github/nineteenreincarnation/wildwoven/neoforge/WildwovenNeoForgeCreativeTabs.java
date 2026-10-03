package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WildwovenNeoForgeCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Wildwoven.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register(
        "main",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.wildwoven.main"))
            .icon(() -> WildwovenNeoForgeItems.GLOWHOPPER_SPAWN_EGG.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WildwovenNeoForgeItems.GLOWHOPPER_SPAWN_EGG.get());
            })
            .build()
    );

    private WildwovenNeoForgeCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
