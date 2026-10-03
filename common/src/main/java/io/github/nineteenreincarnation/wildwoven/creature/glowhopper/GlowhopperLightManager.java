package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;

final class GlowhopperLightManager {
    private static final Map<ServerLevel, Map<BlockPos, LightCell>> LEVELS = new WeakHashMap<>();

    private GlowhopperLightManager() {
    }

    static boolean update(ServerLevel level, BlockPos pos, UUID owner, int brightness) {
        brightness = Math.max(0, Math.min(15, brightness));
        if (brightness == 0) {
            remove(level, pos, owner);
            return false;
        }

        Map<BlockPos, LightCell> cells = LEVELS.computeIfAbsent(level, ignored -> new HashMap<>());
        BlockPos immutablePos = pos.immutable();
        LightCell cell = cells.get(immutablePos);

        if (cell == null) {
            BlockState original = level.getBlockState(immutablePos);
            if (!original.isAir() && !original.is(Blocks.WATER)) {
                return false;
            }

            cell = new LightCell(original);
            cells.put(immutablePos, cell);
        }

        cell.contributors.put(owner, brightness);
        apply(level, immutablePos, cell);
        return true;
    }

    static void remove(ServerLevel level, BlockPos pos, UUID owner) {
        Map<BlockPos, LightCell> cells = LEVELS.get(level);
        if (cells == null) {
            return;
        }

        BlockPos immutablePos = pos.immutable();
        LightCell cell = cells.get(immutablePos);
        if (cell == null) {
            return;
        }

        cell.contributors.remove(owner);
        if (cell.contributors.isEmpty()) {
            BlockState current = level.getBlockState(immutablePos);
            if (current.is(Blocks.LIGHT)) {
                level.setBlock(immutablePos, cell.originalState, 3);
            }

            cells.remove(immutablePos);
            if (cells.isEmpty()) {
                LEVELS.remove(level);
            }
            return;
        }

        apply(level, immutablePos, cell);
    }

    private static void apply(ServerLevel level, BlockPos pos, LightCell cell) {
        int brightness = cell.contributors.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        boolean waterlogged = cell.originalState.is(Blocks.WATER);
        BlockState desired = Blocks.LIGHT.defaultBlockState()
            .setValue(LightBlock.LEVEL, brightness)
            .setValue(LightBlock.WATERLOGGED, waterlogged);

        if (!level.getBlockState(pos).equals(desired)) {
            level.setBlock(pos, desired, 3);
        }
    }

    private static final class LightCell {
        private final BlockState originalState;
        private final Map<UUID, Integer> contributors = new HashMap<>();

        private LightCell(BlockState originalState) {
            this.originalState = originalState;
        }
    }
}
