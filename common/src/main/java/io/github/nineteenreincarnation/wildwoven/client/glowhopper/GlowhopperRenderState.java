package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public final class GlowhopperRenderState extends LivingEntityRenderState {
    public boolean resting;
    public boolean carriedOnHead;
    public boolean panicking;
    public boolean foraging;
    public boolean eating;
    public boolean airborne;
    public float verticalSpeed;
    public float animationOffset;
    public int emittedLight;
}
