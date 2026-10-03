package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

final class GlowhopperEatGlowBerryGoal extends Goal {
    private static final int HORIZONTAL_RANGE = 8;
    private static final int VERTICAL_RANGE = 3;

    private final GlowhopperEntity glowhopper;
    private BlockPos target;
    private BlockPos standTarget;
    private int elapsedTicks;
    private int jumpCooldown;

    GlowhopperEatGlowBerryGoal(GlowhopperEntity glowhopper) {
        this.glowhopper = glowhopper;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.glowhopper.isBaby()
            || this.glowhopper.isPassenger()
            || this.glowhopper.getCurrentLightLevel() > GlowhopperEntity.BERRY_SEARCH_LIGHT_THRESHOLD
            || !this.glowhopper.canSearchForBerry()) {
            return false;
        }

        this.glowhopper.scheduleNextBerrySearch();
        BerryTarget found = this.findNearestBerry();
        if (found == null) {
            this.target = null;
            this.standTarget = null;
            return false;
        }

        this.target = found.berry();
        this.standTarget = found.stand();
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null
            && this.standTarget != null
            && this.elapsedTicks < 240
            && !this.glowhopper.isBaby()
            && !this.glowhopper.isPassenger()
            && CaveVines.hasGlowBerries(this.glowhopper.level().getBlockState(this.target));
    }

    @Override
    public void start() {
        this.elapsedTicks = 0;
        this.jumpCooldown = 0;
        this.glowhopper.setForaging(true);
        this.moveUnderTarget();
    }

    @Override
    public void tick() {
        this.elapsedTicks++;
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
        }

        if (this.target == null || this.standTarget == null) {
            return;
        }

        this.glowhopper.getLookControl().setLookAt(
            this.target.getX() + 0.5,
            this.target.getY() + 0.5,
            this.target.getZ() + 0.5
        );

        double dx = this.glowhopper.getX() - (this.standTarget.getX() + 0.5);
        double dz = this.glowhopper.getZ() - (this.standTarget.getZ() + 0.5);
        double horizontalDistanceSqr = dx * dx + dz * dz;

        if (horizontalDistanceSqr > 1.6) {
            this.moveUnderTarget();
            return;
        }

        double mouthReach = this.glowhopper.getY() + this.glowhopper.getBbHeight() + 0.35;
        if (mouthReach >= this.target.getY() + 0.2) {
            this.eatBerry();
            return;
        }

        if (this.glowhopper.onGround() && this.jumpCooldown == 0) {
            this.glowhopper.performBerryJump();
            this.jumpCooldown = 12;
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.standTarget = null;
        this.elapsedTicks = 0;
        this.glowhopper.setForaging(false);
    }

    private void moveUnderTarget() {
        if (this.standTarget != null) {
            this.glowhopper.getNavigation().moveTo(
                this.standTarget.getX() + 0.5,
                this.standTarget.getY(),
                this.standTarget.getZ() + 0.5,
                1.0
            );
        }
    }

    private BerryTarget findNearestBerry() {
        BlockPos origin = this.glowhopper.blockPosition();
        BerryTarget best = null;
        double bestDistance = Double.MAX_VALUE;

        for (int y = 0; y <= VERTICAL_RANGE; y++) {
            for (int x = -HORIZONTAL_RANGE; x <= HORIZONTAL_RANGE; x++) {
                for (int z = -HORIZONTAL_RANGE; z <= HORIZONTAL_RANGE; z++) {
                    BlockPos candidate = origin.offset(x, y, z);
                    if (!CaveVines.hasGlowBerries(this.glowhopper.level().getBlockState(candidate))) {
                        continue;
                    }

                    BlockPos stand = this.findStandPosition(candidate);
                    if (stand == null) {
                        continue;
                    }

                    double distance = stand.distSqr(origin);
                    if (distance < bestDistance) {
                        best = new BerryTarget(candidate.immutable(), stand.immutable());
                        bestDistance = distance;
                    }
                }
            }
        }

        return best;
    }

    private BlockPos findStandPosition(BlockPos berry) {
        for (int down = 1; down <= VERTICAL_RANGE; down++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (Math.abs(x) + Math.abs(z) > 1) {
                        continue;
                    }

                    BlockPos stand = berry.offset(x, -down, z);
                    if (this.canStandAt(stand)) {
                        return stand;
                    }
                }
            }
        }

        return null;
    }

    private boolean canStandAt(BlockPos stand) {
        BlockState at = this.glowhopper.level().getBlockState(stand);
        if (!at.getCollisionShape(this.glowhopper.level(), stand).isEmpty()) {
            return false;
        }

        BlockPos above = stand.above();
        if (!this.glowhopper.level().getBlockState(above)
            .getCollisionShape(this.glowhopper.level(), above)
            .isEmpty()) {
            return false;
        }

        BlockPos floor = stand.below();
        return this.glowhopper.level().getBlockState(floor)
            .isFaceSturdy(this.glowhopper.level(), floor, Direction.UP);
    }

    private void eatBerry() {
        if (!(this.glowhopper.level() instanceof ServerLevel level) || this.target == null) {
            return;
        }

        BlockState state = level.getBlockState(this.target);
        if (!CaveVines.hasGlowBerries(state)) {
            return;
        }

        BlockState eatenState = state.setValue(CaveVines.BERRIES, false);
        level.setBlock(this.target, eatenState, 2);
        level.playSound(
            null,
            this.target,
            SoundEvents.CAVE_VINES_PICK_BERRIES,
            SoundSource.NEUTRAL,
            1.0F,
            0.9F + this.glowhopper.getRandom().nextFloat() * 0.2F
        );
        level.gameEvent(GameEvent.BLOCK_CHANGE, this.target, GameEvent.Context.of(this.glowhopper, eatenState));
        this.glowhopper.refillGlowCharge();
        this.glowhopper.startEatAnimation();
        this.target = null;
        this.standTarget = null;
    }

    private record BerryTarget(BlockPos berry, BlockPos stand) {
    }
}
