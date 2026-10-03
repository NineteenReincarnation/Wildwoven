package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperEntity;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("deprecation")
public final class GlowhopperRenderer
    extends AgeableMobRenderer<GlowhopperEntity, GlowhopperRenderState, GlowhopperModel> {

    private static final Identifier ADULT_TEXTURE = Identifier.fromNamespaceAndPath(
        Wildwoven.MOD_ID,
        "textures/entity/glowhopper/glowhopper.png"
    );

    private static final Identifier BABY_TEXTURE = Identifier.fromNamespaceAndPath(
        Wildwoven.MOD_ID,
        "textures/entity/glowhopper/glowhopper_baby.png"
    );

    public GlowhopperRenderer(EntityRendererProvider.Context context) {
        super(
            context,
            new GlowhopperModel(context.bakeLayer(GlowhopperModelLayers.ADULT)),
            new GlowhopperModel(context.bakeLayer(GlowhopperModelLayers.BABY)),
            0.25F
        );
    }

    @Override
    public Identifier getTextureLocation(GlowhopperRenderState state) {
        return state.isBaby ? BABY_TEXTURE : ADULT_TEXTURE;
    }

    @Override
    public GlowhopperRenderState createRenderState() {
        return new GlowhopperRenderState();
    }

    @Override
    public void extractRenderState(
        GlowhopperEntity entity,
        GlowhopperRenderState state,
        float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        state.resting = entity.isResting();
        state.carriedOnHead = entity.isPassenger() && entity.getVehicle() instanceof Player;
        state.panicking = entity.isPanicking();
        state.foraging = entity.isForaging();
        state.eating = entity.isEatingBerry();
        state.airborne = !entity.onGround();
        state.verticalSpeed = (float)entity.getDeltaMovement().y;
        state.animationOffset = (entity.getId() * 0.7548777F) % ((float)Math.PI * 2.0F);
        state.emittedLight = entity.getCurrentLightLevel();
    }
}
