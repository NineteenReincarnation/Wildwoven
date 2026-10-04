package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.LightCoordsUtil;

/** Only lamp pixels glow; the moss and face keep normal cave lighting. */
public final class GlowhopperGlowLayer extends RenderLayer<GlowhopperRenderState, GlowhopperModel> {
    private static final Identifier ADULT = Identifier.fromNamespaceAndPath(
        Wildwoven.MOD_ID, "textures/entity/glowhopper/glowhopper_glow.png");
    private static final Identifier BABY = Identifier.fromNamespaceAndPath(
        Wildwoven.MOD_ID, "textures/entity/glowhopper/glowhopper_baby_glow.png");

    public GlowhopperGlowLayer(RenderLayerParent<GlowhopperRenderState, GlowhopperModel> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                       GlowhopperRenderState state, float yaw, float pitch) {
        if (state.isInvisible) {
            return;
        }
        // Keep the juvenile softer even when fully charged; brightness tracks real world light.
        int alpha = Mth.clamp(Math.round(state.emittedLight / 15.0F * 255.0F), 0, 255);
        int tint = (alpha << 24) | 0x00FFFFFF;
        collector.order(1).submitModel(this.getParentModel(), state, poseStack,
            RenderTypes.entityTranslucentEmissive(state.isBaby ? BABY : ADULT),
            LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, tint, null, state.outlineColor, null);
    }
}
