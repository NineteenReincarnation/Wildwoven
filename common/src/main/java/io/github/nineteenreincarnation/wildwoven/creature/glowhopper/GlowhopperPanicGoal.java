package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import net.minecraft.world.entity.ai.goal.PanicGoal;

final class GlowhopperPanicGoal extends PanicGoal {
    private final GlowhopperEntity glowhopper;

    GlowhopperPanicGoal(GlowhopperEntity glowhopper, double speedModifier) {
        super(glowhopper, speedModifier);
        this.glowhopper = glowhopper;
    }

    @Override
    protected boolean shouldPanic() {
        return this.glowhopper.getPanicTicks() > 0;
    }

    @Override
    public void tick() {
        super.tick();
        this.glowhopper.setSpeedModifier(this.speedModifier);
    }

    @Override
    public boolean canContinueToUse() {
        return this.glowhopper.getPanicTicks() > 0 && super.canContinueToUse();
    }
}
