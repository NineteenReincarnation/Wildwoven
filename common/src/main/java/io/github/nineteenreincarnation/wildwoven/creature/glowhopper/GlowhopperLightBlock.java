package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class GlowhopperLightBlock extends LightBlock {
    private static final int CLEANUP_DELAY_TICKS = 60;

    public GlowhopperLightBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(
        BlockState state,
        Level level,
        BlockPos pos,
        BlockState oldState,
        boolean movedByPiston
    ) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, CLEANUP_DELAY_TICKS);
        }
    }

    @Override
    protected void tick(
        BlockState state,
        ServerLevel level,
        BlockPos pos,
        RandomSource random
    ) {
        if (GlowhopperLightManager.hasContributors(level, pos)) {
            level.scheduleTick(pos, this, CLEANUP_DELAY_TICKS);
            return;
        }

        BlockState replacement = state.getValue(WATERLOGGED)
            ? Blocks.WATER.defaultBlockState()
            : Blocks.AIR.defaultBlockState();
        level.setBlock(pos, replacement, 3);
    }
}
