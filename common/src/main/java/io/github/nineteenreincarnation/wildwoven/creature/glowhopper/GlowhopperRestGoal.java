package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

final class GlowhopperRestGoal extends Goal {
    private final GlowhopperEntity glowhopper;
    private int restTicks;
    private int nextRestAttempt;

    GlowhopperRestGoal(GlowhopperEntity glowhopper) {
        this.glowhopper = glowhopper;
        this.nextRestAttempt = 400 + glowhopper.getRandom().nextInt(501);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (--this.nextRestAttempt > 0) {
            return false;
        }

        this.nextRestAttempt = 400 + this.glowhopper.getRandom().nextInt(501);
        return this.glowhopper.onGround()
            && !this.glowhopper.isInWater()
            && !this.glowhopper.isPassenger()
            && this.glowhopper.getPanicTicks() == 0
            && !this.glowhopper.isInLove()
            && this.glowhopper.getNavigation().isDone();
    }

    @Override
    public boolean canContinueToUse() {
        return this.restTicks > 0
            && this.glowhopper.onGround()
            && !this.glowhopper.isInWater()
            && !this.glowhopper.isPassenger()
            && this.glowhopper.getPanicTicks() == 0
            && !this.glowhopper.isInLove();
    }

    @Override
    public void start() {
        this.restTicks = 100 + this.glowhopper.getRandom().nextInt(201);
        this.glowhopper.getNavigation().stop();
        this.glowhopper.setResting(true);
    }

    @Override
    public void tick() {
        this.restTicks--;
        this.glowhopper.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.restTicks = 0;
        this.glowhopper.setResting(false);
    }
}
