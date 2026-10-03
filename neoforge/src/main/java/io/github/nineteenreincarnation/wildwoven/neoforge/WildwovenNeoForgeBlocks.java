package io.github.nineteenreincarnation.wildwoven.neoforge;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperLightBlock;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperLightModule;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WildwovenNeoForgeBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
        DeferredRegister.createBlocks(Wildwoven.MOD_ID);

    public static final DeferredBlock<GlowhopperLightBlock> GLOWHOPPER_LIGHT =
        BLOCKS.registerBlock(
            GlowhopperLightModule.ID,
            GlowhopperLightBlock::new,
            () -> BlockBehaviour.Properties.of()
                .replaceable()
                .strength(-1.0F, 3600000.8F)
                .noLootTable()
                .noOcclusion()
                .lightLevel(LightBlock.LIGHT_EMISSION)
        );

    private WildwovenNeoForgeBlocks() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        GlowhopperLightModule.bindBlock(GLOWHOPPER_LIGHT::get);
    }
}
