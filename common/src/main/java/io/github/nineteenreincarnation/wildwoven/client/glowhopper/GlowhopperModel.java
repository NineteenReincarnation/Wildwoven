package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class GlowhopperModel extends EntityModel<GlowhopperRenderState> {
    private final ModelPart body, rightFrontLeg, leftFrontLeg, rightHindLeg, leftHindLeg, lanternStem;

    public GlowhopperModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.lanternStem = this.body.getChild("lantern_stem");
    }

    // BEGIN GENERATED GEOMETRY -- art/creatures/glowhopper/build_assets.py
    public static LayerDefinition createAdultLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-5.00F, -3.50F, -4.50F, 10.00F, 7.00F, 10.00F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(42, 0).addBox(-5.00F, -3.50F, -5.50F, 10.00F, 0.70F, 1.00F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(66, 0).addBox(-5.00F, -4.15F, -5.50F, 10.00F, 0.65F, 11.00F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(110, 0).addBox(-4.80F, -4.27F, -2.00F, 3.00F, 0.12F, 3.00F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(0, 19).addBox(0.20F, -4.25F, 1.00F, 3.80F, 0.10F, 3.50F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(17, 19).addBox(-6.20F, -2.55F, -4.60F, 1.40F, 2.05F, 2.40F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(17, 19).addBox(4.80F, -2.55F, -4.60F, 1.40F, 2.05F, 2.40F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(27, 19).addBox(-5.35F, -3.20F, -1.80F, 0.55F, 3.20F, 3.50F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(27, 19).addBox(4.80F, -3.20F, -1.80F, 0.55F, 3.20F, 3.50F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(38, 19).addBox(-6.00F, -2.50F, 2.70F, 1.20F, 2.00F, 2.40F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(38, 19).addBox(4.80F, -2.50F, 2.70F, 1.20F, 2.00F, 2.40F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(48, 19).addBox(-5.05F, -3.60F, 4.70F, 3.25F, 3.20F, 1.00F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(59, 19).addBox(-1.80F, -3.80F, 4.90F, 3.50F, 4.00F, 0.80F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(70, 19).addBox(1.70F, -3.50F, 4.80F, 3.35F, 3.00F, 0.90F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(0.00F, 17.50F, 0.00F));
        PartDefinition head = body.addOrReplaceChild("head",
            CubeListBuilder.create()
                .texOffs(81, 19).addBox(-5.00F, -2.80F, -2.05F, 10.00F, 6.30F, 1.05F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(106, 19).addBox(-5.25F, -2.50F, -2.28F, 1.90F, 2.50F, 1.30F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(115, 19).addBox(-5.05F, -4.20F, -2.30F, 1.85F, 2.30F, 1.25F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(0, 29).addBox(-3.20F, -3.90F, -2.20F, 1.80F, 2.60F, 1.10F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(8, 29).addBox(-1.40F, -4.20F, -2.32F, 3.20F, 2.90F, 1.25F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(19, 29).addBox(-0.30F, -1.30F, -2.22F, 0.70F, 1.30F, 0.70F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(24, 29).addBox(1.80F, -3.90F, -2.18F, 1.55F, 2.50F, 1.10F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(32, 29).addBox(3.35F, -4.00F, -2.28F, 1.90F, 4.00F, 1.25F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(120, 120).addBox(-3.40F, -0.05F, -2.07F, 1.60F, 1.75F, 0.03F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(120, 120).addBox(1.50F, -0.05F, -2.07F, 1.60F, 1.75F, 0.03F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(0.00F, 0.00F, -3.50F));
        PartDefinition lantern_stem = body.addOrReplaceChild("lantern_stem",
            CubeListBuilder.create()
                .texOffs(41, 29).addBox(-1.00F, -2.00F, -1.00F, 2.00F, 2.00F, 2.00F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(51, 29).addBox(-0.50F, -3.80F, -1.04F, 2.00F, 2.00F, 2.08F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(62, 29).addBox(1.00F, -5.50F, -1.08F, 3.00F, 2.00F, 2.16F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(75, 29).addBox(2.50F, -3.50F, -1.00F, 2.00F, 2.20F, 2.00F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(0.00F, -4.15F, 0.00F));
        PartDefinition right_front_berry = body.addOrReplaceChild("right_front_berry",
            CubeListBuilder.create()
                .texOffs(85, 29).addBox(0.00F, 0.00F, -1.00F, 2.00F, 3.50F, 2.00F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-7.00F, -0.50F, -3.90F));
        PartDefinition right_hind_berry = body.addOrReplaceChild("right_hind_berry",
            CubeListBuilder.create()
                .texOffs(85, 29).addBox(0.00F, 0.00F, -1.00F, 2.00F, 3.50F, 2.00F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-5.20F, -0.50F, 5.60F));
        PartDefinition left_front_berry = body.addOrReplaceChild("left_front_berry",
            CubeListBuilder.create()
                .texOffs(85, 29).addBox(0.00F, 0.00F, -1.00F, 2.00F, 3.50F, 2.00F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(5.00F, -0.50F, -3.90F));
        PartDefinition left_hind_berry = body.addOrReplaceChild("left_hind_berry",
            CubeListBuilder.create()
                .texOffs(85, 29).addBox(0.00F, 0.00F, -1.00F, 2.00F, 3.50F, 2.00F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(3.20F, -0.50F, 5.60F));
        PartDefinition right_front_leg = root.addOrReplaceChild("right_front_leg",
            CubeListBuilder.create()
                .texOffs(95, 29).addBox(-1.30F, 0.00F, -1.30F, 2.60F, 4.00F, 2.60F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-3.20F, 20.00F, -3.80F));
        PartDefinition right_hind_leg = root.addOrReplaceChild("right_hind_leg",
            CubeListBuilder.create()
                .texOffs(95, 29).addBox(-1.30F, 0.00F, -1.30F, 2.60F, 4.00F, 2.60F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-3.20F, 20.00F, 3.80F));
        PartDefinition left_front_leg = root.addOrReplaceChild("left_front_leg",
            CubeListBuilder.create()
                .texOffs(95, 29).addBox(-1.30F, 0.00F, -1.30F, 2.60F, 4.00F, 2.60F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(3.20F, 20.00F, -3.80F));
        PartDefinition left_hind_leg = root.addOrReplaceChild("left_hind_leg",
            CubeListBuilder.create()
                .texOffs(95, 29).addBox(-1.30F, 0.00F, -1.30F, 2.60F, 4.00F, 2.60F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(3.20F, 20.00F, 3.80F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    public static LayerDefinition createBabyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.50F, -2.45F, -3.15F, 7.00F, 4.90F, 7.00F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(30, 0).addBox(-3.50F, -2.45F, -3.85F, 7.00F, 0.49F, 0.70F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(48, 0).addBox(-3.50F, -2.91F, -3.85F, 7.00F, 0.46F, 7.70F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(80, 0).addBox(-3.36F, -2.99F, -1.40F, 2.10F, 0.09F, 2.10F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(91, 0).addBox(0.14F, -2.97F, 0.70F, 2.66F, 0.07F, 2.45F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(104, 0).addBox(-4.34F, -1.78F, -3.22F, 0.98F, 1.43F, 1.68F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(104, 0).addBox(3.36F, -1.78F, -3.22F, 0.98F, 1.43F, 1.68F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(112, 0).addBox(-3.74F, -2.24F, -1.26F, 0.38F, 2.24F, 2.45F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(120, 0).addBox(3.36F, -2.24F, -1.26F, 0.39F, 2.24F, 2.45F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(0, 14).addBox(-4.20F, -1.75F, 1.89F, 0.84F, 1.40F, 1.68F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(0, 14).addBox(3.36F, -1.75F, 1.89F, 0.84F, 1.40F, 1.68F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(8, 14).addBox(-3.53F, -2.52F, 3.29F, 2.27F, 2.24F, 0.70F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(16, 14).addBox(-1.26F, -2.66F, 3.43F, 2.45F, 2.80F, 0.56F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(25, 14).addBox(1.19F, -2.45F, 3.36F, 2.34F, 2.10F, 0.63F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(0.00F, 20.15F, 0.00F));
        PartDefinition head = body.addOrReplaceChild("head",
            CubeListBuilder.create()
                .texOffs(33, 14).addBox(-3.50F, -1.96F, -1.43F, 7.00F, 4.41F, 0.73F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(51, 14).addBox(-3.67F, -1.75F, -1.60F, 1.33F, 1.75F, 0.91F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(58, 14).addBox(-3.53F, -2.94F, -1.61F, 1.29F, 1.61F, 0.88F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(65, 14).addBox(-2.24F, -2.73F, -1.54F, 1.26F, 1.82F, 0.77F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(72, 14).addBox(-0.98F, -2.94F, -1.62F, 2.24F, 2.03F, 0.87F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(81, 14).addBox(2.34F, -2.80F, -1.60F, 1.33F, 2.80F, 0.88F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(120, 120).addBox(-2.38F, -0.03F, -1.45F, 1.12F, 1.22F, 0.02F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(120, 120).addBox(1.05F, -0.03F, -1.45F, 1.12F, 1.22F, 0.02F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(0.00F, 0.00F, -2.45F));
        PartDefinition lantern_stem = body.addOrReplaceChild("lantern_stem",
            CubeListBuilder.create()
                .texOffs(88, 14).addBox(-0.70F, -1.40F, -0.70F, 1.40F, 1.40F, 1.40F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(96, 14).addBox(-0.35F, -2.66F, -0.73F, 1.40F, 1.40F, 1.46F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(104, 14).addBox(0.70F, -3.85F, -0.76F, 2.10F, 1.40F, 1.52F, CubeDeformation.NONE, 0.5F, 0.5F)
                .texOffs(114, 14).addBox(1.75F, -2.45F, -0.70F, 1.40F, 1.54F, 1.40F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(0.00F, -2.91F, 0.00F));
        PartDefinition right_front_berry = body.addOrReplaceChild("right_front_berry",
            CubeListBuilder.create()
                .texOffs(122, 14).addBox(0.00F, 0.00F, -0.70F, 1.40F, 2.45F, 1.40F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-4.90F, -0.35F, -2.73F));
        PartDefinition right_hind_berry = body.addOrReplaceChild("right_hind_berry",
            CubeListBuilder.create()
                .texOffs(122, 14).addBox(0.00F, 0.00F, -0.70F, 1.40F, 2.45F, 1.40F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-3.64F, -0.35F, 3.92F));
        PartDefinition left_front_berry = body.addOrReplaceChild("left_front_berry",
            CubeListBuilder.create()
                .texOffs(122, 14).addBox(0.00F, 0.00F, -0.70F, 1.40F, 2.45F, 1.40F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(3.50F, -0.35F, -2.73F));
        PartDefinition left_hind_berry = body.addOrReplaceChild("left_hind_berry",
            CubeListBuilder.create()
                .texOffs(122, 14).addBox(0.00F, 0.00F, -0.70F, 1.40F, 2.45F, 1.40F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(2.24F, -0.35F, 3.92F));
        PartDefinition right_front_leg = root.addOrReplaceChild("right_front_leg",
            CubeListBuilder.create()
                .texOffs(0, 22).addBox(-0.91F, 0.00F, -0.91F, 1.82F, 2.10F, 1.82F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-2.24F, 21.90F, -2.66F));
        PartDefinition right_hind_leg = root.addOrReplaceChild("right_hind_leg",
            CubeListBuilder.create()
                .texOffs(0, 22).addBox(-0.91F, 0.00F, -0.91F, 1.82F, 2.10F, 1.82F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(-2.24F, 21.90F, 2.66F));
        PartDefinition left_front_leg = root.addOrReplaceChild("left_front_leg",
            CubeListBuilder.create()
                .texOffs(0, 22).addBox(-0.91F, 0.00F, -0.91F, 1.82F, 2.10F, 1.82F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(2.24F, 21.90F, -2.66F));
        PartDefinition left_hind_leg = root.addOrReplaceChild("left_hind_leg",
            CubeListBuilder.create()
                .texOffs(0, 22).addBox(-0.91F, 0.00F, -0.91F, 1.82F, 2.10F, 1.82F, CubeDeformation.NONE, 0.5F, 0.5F),
            PartPose.offset(2.24F, 21.90F, 2.66F));
        return LayerDefinition.create(mesh, 256, 256);
    }
    // END GENERATED GEOMETRY

    @Override
    public void setupAnim(GlowhopperRenderState state) {
        super.setupAnim(state);
        float time = state.ageInTicks + state.animationOffset;
        float movement = Mth.clamp(state.walkAnimationSpeed, 0.0F, 1.0F);
        if (state.carriedOnHead || state.resting) {
            this.applyPronePose(state.isBaby, state.carriedOnHead);
            if (!state.carriedOnHead) {
                this.body.y += Mth.sin(time * 0.12F) * 0.04F;
                this.body.xRot += Mth.sin(time * 0.07F) * 0.015F;
            }
            return;
        }
        // Face and moss are an integrated shell, not a neck joint. Turning the thin
        // face independently cuts it through the torso and exposes diagonal seams.
        this.root().yRot = Mth.clamp(state.yRot * Mth.DEG_TO_RAD, -0.12F, 0.12F);
        float lookPitch = Mth.clamp(state.xRot * Mth.DEG_TO_RAD, -0.08F, 0.08F);
        if (state.airborne) {
            float vertical = Mth.clamp(state.verticalSpeed * 2.0F, -1.0F, 1.0F);
            this.body.xRot = -0.14F * vertical;
            this.rightFrontLeg.xRot = this.leftFrontLeg.xRot = -0.55F;
            this.rightHindLeg.xRot = this.leftHindLeg.xRot = 0.48F;
            if (state.foraging) {
                this.body.xRot -= 0.08F;
            }
        } else {
            this.applyWalkPose(state.walkAnimationPos, movement, state.isBaby);
            this.body.y += Mth.sin(time * 0.12F) * 0.05F * (1.0F - movement);
            this.body.xRot = lookPitch;
            this.body.zRot += Mth.sin(time * 0.055F) * 0.015F * (1.0F - movement);
        }
        if (state.eating) {
            this.body.xRot += 0.06F + Mth.sin(time * 1.8F) * 0.03F;
        }
        this.lanternStem.xRot = Mth.clamp(-state.verticalSpeed * 0.18F, -0.10F, 0.10F);
        this.lanternStem.zRot = Mth.sin(time * 0.12F) * 0.018F * (state.isBaby ? 0.6F : 1.0F)
            - this.body.zRot * 0.5F;
    }

    private void applyWalkPose(float position, float movement, boolean baby) {
        // Actual distance drives alternating diagonal pairs; feet lift only on the swing phase.
        float cycle = position * 1.8F;
        float stride = (baby ? 0.48F : 0.55F) * movement;
        float swing = Mth.cos(cycle) * stride;
        this.rightFrontLeg.xRot = this.leftHindLeg.xRot = swing;
        this.leftFrontLeg.xRot = this.rightHindLeg.xRot = -swing;
        float legLength = baby ? 2.1F : 4.0F;
        float halfDepth = baby ? 0.91F : 1.3F;
        float clearance = Math.max(0.0F, (float)(legLength * (Math.cos(swing) - 1.0)
            + halfDepth * Math.abs(Math.sin(swing))));
        this.rightFrontLeg.y -= clearance;
        this.leftFrontLeg.y -= clearance;
        this.rightHindLeg.y -= clearance;
        this.leftHindLeg.y -= clearance;
        float lift = (baby ? 0.35F : 0.55F) * movement;
        this.rightFrontLeg.y -= Math.max(0.0F, Mth.sin(cycle)) * lift;
        this.leftHindLeg.y -= Math.max(0.0F, Mth.sin(cycle)) * lift;
        this.leftFrontLeg.y -= Math.max(0.0F, -Mth.sin(cycle)) * lift;
        this.rightHindLeg.y -= Math.max(0.0F, -Mth.sin(cycle)) * lift;
        this.body.y -= (1.0F - Mth.cos(cycle * 2.0F)) * 0.09F * movement;
        this.body.zRot = Mth.sin(cycle) * 0.025F * movement;
    }

    private void applyPronePose(boolean baby, boolean carried) {
        this.body.y += baby ? (carried ? 1.4F : 1.0F) : (carried ? 3.0F : 2.6F);
        float spread = carried ? 1.50F : 1.45F;
        this.rightFrontLeg.zRot = this.rightHindLeg.zRot = spread;
        this.leftFrontLeg.zRot = this.leftHindLeg.zRot = -spread;
        this.rightFrontLeg.xRot = this.leftFrontLeg.xRot = -0.08F;
        this.rightHindLeg.xRot = this.leftHindLeg.xRot = 0.08F;
        float length = baby ? 2.1F : 4.0F;
        float halfWidth = baby ? 0.91F : 1.3F;
        float halfDepth = halfWidth;
        float legY = (float)(24.0 - length * Math.cos(spread) * Math.cos(0.08F)
            - halfWidth * Math.sin(spread) - halfDepth * Math.cos(spread) * Math.sin(0.08F));
        this.rightFrontLeg.y = this.leftFrontLeg.y = legY;
        this.rightHindLeg.y = this.leftHindLeg.y = legY;
    }
}
