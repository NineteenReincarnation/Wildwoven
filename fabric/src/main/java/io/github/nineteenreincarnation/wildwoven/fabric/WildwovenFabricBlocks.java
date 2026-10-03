package io.github.nineteenreincarnation.wildwoven.fabric;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperLightBlock;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperLightModule;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class WildwovenFabricBlocks {
    private static final ResourceKey<Block> GLOWHOPPER_LIGHT_KEY = ResourceKey.create(
        Registries.BLOCK,
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, GlowhopperLightModule.ID)
    );

    public static final GlowhopperLightBlock GLOWHOPPER_LIGHT = Registry.register(
        BuiltInRegistries.BLOCK,
        GLOWHOPPER_LIGHT_KEY,
        new GlowhopperLightBlock(
            BlockBehaviour.Properties.of()
                .setId(GLOWHOPPER_LIGHT_KEY)
                .replaceable()
                .strength(-1.0F, 3600000.8F)
                .noLootTable()
                .noOcclusion()
                .lightLevel(LightBlock.LIGHT_EMISSION)
        )
    );

    private WildwovenFabricBlocks() {
    }

    public static void register() {
        GlowhopperLightModule.bindBlock(() -> GLOWHOPPER_LIGHT);
    }
}
