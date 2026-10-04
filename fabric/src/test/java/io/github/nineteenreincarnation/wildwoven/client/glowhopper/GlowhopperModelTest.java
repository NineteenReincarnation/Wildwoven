package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class GlowhopperModelTest {
    private static final String[] LEGS = {
        "right_front_leg", "left_front_leg", "right_hind_leg", "left_hind_leg"
    };

    private static GlowhopperModel model(boolean baby) {
        return new GlowhopperModel((baby ? GlowhopperModel.createBabyLayer()
            : GlowhopperModel.createAdultLayer()).bakeRoot());
    }

    @Test
    void diagonalPairsAlternateAndStoppingReturnsFeetToNeutral() {
        for (boolean baby : new boolean[]{false, true}) {
            GlowhopperModel model = model(baby);
            GlowhopperRenderState state = new GlowhopperRenderState();
            state.isBaby = baby;
            state.walkAnimationSpeed = 0.7F;
            state.walkAnimationPos = 0.2F;
            model.setupAnim(state);
            ModelPart root = model.root();
            assertEquals(root.getChild(LEGS[0]).xRot, root.getChild(LEGS[3]).xRot, 0.00001F);
            assertEquals(root.getChild(LEGS[1]).xRot, root.getChild(LEGS[2]).xRot, 0.00001F);
            assertEquals(-root.getChild(LEGS[0]).xRot, root.getChild(LEGS[1]).xRot, 0.00001F);
            assertNotEquals(0.0F, root.getChild(LEGS[0]).xRot);
            state.walkAnimationSpeed = 0.0F;
            model.setupAnim(state);
            for (String leg : LEGS) {
                assertEquals(0.0F, root.getChild(leg).xRot, 0.000001F);
                assertEquals(root.getChild(leg).getInitialPose().y(), root.getChild(leg).y);
            }
        }
    }

    @Test
    void BakedFeetDoNotPassThroughTheFloorDuringTheWalkCycle() {
        for (boolean baby : new boolean[]{false, true}) {
            GlowhopperModel model = model(baby);
            GlowhopperRenderState state = new GlowhopperRenderState();
            state.isBaby = baby;
            state.walkAnimationSpeed = 1.0F;
            for (int frame = 0; frame < 120; frame++) {
                state.walkAnimationPos = frame * 0.07F;
                model.setupAnim(state);
                for (String name : LEGS) {
                    float[] lowest = {Float.NEGATIVE_INFINITY};
                    model.root().getChild(name).getExtentsForGui(new PoseStack(),
                        vertex -> lowest[0] = Math.max(lowest[0], vertex.y()));
                    assertTrue(lowest[0] <= 1.50001F, name + " penetrates floor at frame " + frame + ": " + lowest[0]);
                }
            }
        }
    }

    @Test
    void RestAndCarryFeetStayOnTheSurfaceAndPosesDoNotAccumulate() {
        for (boolean baby : new boolean[]{false, true}) {
            GlowhopperModel model = model(baby);
            GlowhopperRenderState state = new GlowhopperRenderState();
            state.isBaby = baby;
            for (boolean carried : new boolean[]{false, true}) {
                state.carriedOnHead = carried;
                state.resting = !carried;
                state.ageInTicks = 0;
                model.setupAnim(state);
                float bodyY = model.root().getChild("body").y;
                for (int i = 0; i < 60; i++) {
                    model.setupAnim(state);
                    assertEquals(bodyY, model.root().getChild("body").y);
                }
                for (String name : LEGS) {
                    float[] lowest = {Float.NEGATIVE_INFINITY};
                    model.root().getChild(name).getExtentsForGui(new PoseStack(),
                        vertex -> lowest[0] = Math.max(lowest[0], vertex.y()));
                    assertEquals(1.5F, lowest[0], 0.00001F);
                }
            }
            state.carriedOnHead = state.resting = false;
            model.setupAnim(state);
            assertEquals(model.root().getChild("body").getInitialPose().y(), model.root().getChild("body").y);
            for (String leg : LEGS) {
                assertEquals(0.0F, model.root().getChild(leg).zRot);
            }
        }
    }
}
