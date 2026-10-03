package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class WildwovenFabricCreativeTabs {
    public static final Identifier MAIN_ID =
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, "main");

    private WildwovenFabricCreativeTabs() {
    }

    public static void register() {
        Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            MAIN_ID,
            FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.wildwoven.main"))
                .icon(() -> new ItemStack(WildwovenFabricItems.GLOWHOPPER_SPAWN_EGG))
                .displayItems((parameters, output) -> {
                    output.accept(WildwovenFabricItems.GLOWHOPPER_SPAWN_EGG);
                })
                .build()
        );
    }
}
