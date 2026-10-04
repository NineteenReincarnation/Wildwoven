package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
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

    @Test
    void integratedFaceStaysRigidThroughLookEatJumpRestAndCarry() {
        for (boolean baby : new boolean[]{false, true}) {
            GlowhopperModel model = model(baby);
            GlowhopperRenderState state = new GlowhopperRenderState();
            state.isBaby = baby;
            state.ageInTicks = 23;
            for (int action = 0; action < 6; action++) {
                state.eating = action == 1;
                state.airborne = action == 2 || action == 3;
                state.foraging = action == 3;
                state.resting = action == 4;
                state.carriedOnHead = action == 5;
                state.verticalSpeed = 0.4F;
                for (float yaw : new float[]{-90, -30, 0, 30, 90}) {
                    for (float pitch : new float[]{-60, 0, 60}) {
                        state.yRot = yaw;
                        state.xRot = pitch;
                        model.setupAnim(state);
                        ModelPart face = model.root().getChild("body").getChild("head");
                        assertEquals(0, face.xRot, 0.000001F, "Face must not pitch through the shell");
                        assertEquals(0, face.yRot, 0.000001F, "Face must not turn through the shell");
                        assertEquals(0, face.zRot, 0.000001F, "Face must not roll through the shell");
                    }
                }
            }
            state.eating = state.airborne = state.foraging = state.resting = state.carriedOnHead = false;
            state.verticalSpeed = state.yRot = state.xRot = 0;
            model.setupAnim(state);
            assertEquals(0, model.root().yRot, 0.000001F, "Look rotation must reset");
        }
    }

    @Test
    void torsoAndFaceSlicesMeetWithoutOverlappingVolume() {
        for (boolean baby : new boolean[]{false, true}) {
            GlowhopperModel model = model(baby);
            ModelPart body = model.root().getChild("body");
            ModelPart head = body.getChild("head");
            ModelPart.Cube[] torso = {null}, face = {null};
            body.visit(new PoseStack(), (pose, path, index, cube) -> {
                if (torso[0] == null) torso[0] = cube;
            });
            head.visit(new PoseStack(), (pose, path, index, cube) -> {
                if (face[0] == null) face[0] = cube;
            });
            assertEquals(torso[0].minZ, head.z + face[0].maxZ, 0.000001F,
                "The face must replace the front torso slice, not overlay its sides/bottom");
        }
    }

    private record BakedBox(String name, float[] min, float[] max) {
        boolean contains(float[] p) {
            for (int axis = 0; axis < 3; axis++) {
                if (p[axis] <= min[axis] + 0.0000001F || p[axis] >= max[axis] - 0.0000001F) return false;
            }
            return true;
        }
    }

    @Test
    void bakedMeshHasNoExposedOverlappingCoplanarFaces() {
        for (boolean baby : new boolean[]{false, true}) {
            List<BakedBox> boxes = new ArrayList<>();
            model(baby).root().visit(new PoseStack(), (pose, path, index, cube) -> {
                float[] min = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
                float[] max = {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                for (float x : new float[]{cube.minX, cube.maxX}) {
                    for (float y : new float[]{cube.minY, cube.maxY}) {
                        for (float z : new float[]{cube.minZ, cube.maxZ}) {
                            Vector3f point = pose.pose().transformPosition(x / 16, y / 16, z / 16, new Vector3f());
                            float[] xyz = {point.x(), point.y(), point.z()};
                            for (int axis = 0; axis < 3; axis++) {
                                min[axis] = Math.min(min[axis], xyz[axis]);
                                max[axis] = Math.max(max[axis], xyz[axis]);
                            }
                        }
                    }
                }
                boxes.add(new BakedBox(path + "/" + index, min, max));
            });
            for (int i = 0; i < boxes.size(); i++) {
                for (int j = i + 1; j < boxes.size(); j++) {
                    BakedBox a = boxes.get(i), b = boxes.get(j);
                    for (int axis = 0; axis < 3; axis++) {
                        int u = (axis + 1) % 3, v = (axis + 2) % 3;
                        float loU = Math.max(a.min[u], b.min[u]), hiU = Math.min(a.max[u], b.max[u]);
                        float loV = Math.max(a.min[v], b.min[v]), hiV = Math.min(a.max[v], b.max[v]);
                        if (hiU - loU < 0.00001F || hiV - loV < 0.00001F) continue;
                        for (boolean upper : new boolean[]{false, true}) {
                            float planeA = (upper ? a.max : a.min)[axis];
                            float planeB = (upper ? b.max : b.min)[axis];
                            if (Math.abs(planeA - planeB) > 0.000001F) continue;
                            for (float su : new float[]{0.2F, 0.5F, 0.8F}) {
                                for (float sv : new float[]{0.2F, 0.5F, 0.8F}) {
                                    float[] point = new float[3];
                                    point[axis] = planeA + (upper ? 0.00005F : -0.00005F);
                                    point[u] = loU + su * (hiU - loU);
                                    point[v] = loV + sv * (hiV - loV);
                                    assertTrue(boxes.stream().anyMatch(box -> box.contains(point)),
                                        "Exposed coplanar faces: " + a.name + " / " + b.name + " axis " + axis);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
