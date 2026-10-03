package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class GlowhopperModel extends EntityModel<GlowhopperRenderState> {
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart headBud;
    private final ModelPart backBudLeft;
    private final ModelPart backBudRight;

    public GlowhopperModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.headBud = this.head.getChild("head_bud");
        this.backBudLeft = this.body.getChild("back_bud_left");
        this.backBudRight = this.body.getChild("back_bud_right");
    }

    public static LayerDefinition createAdultLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -2.5F, -5.0F, 8.0F, 5.0F, 10.0F),
            PartPose.offset(0.0F, 18.5F, 1.0F)
        );

        PartDefinition head = body.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 30)
                .addBox(-3.0F, -2.5F, -4.0F, 6.0F, 5.0F, 5.0F)
                .texOffs(48, 16)
                .addBox(-2.25F, -0.75F, -4.01F, 1.0F, 1.0F, 0.0F)
                .texOffs(48, 16)
                .addBox(1.25F, -0.75F, -4.01F, 1.0F, 1.0F, 0.0F),
            PartPose.offset(0.0F, -0.25F, -5.0F)
        );

        head.addOrReplaceChild(
            "head_bud",
            CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
            PartPose.offset(0.0F, -2.4F, -0.8F)
        );

        body.addOrReplaceChild(
            "back_bud_left",
            CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
            PartPose.offset(2.0F, -2.3F, 1.5F)
        );

        body.addOrReplaceChild(
            "back_bud_right",
            CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F),
            PartPose.offset(-2.0F, -2.3F, 1.5F)
        );

        addAdultLeg(root, "right_front_leg", -3.0F, -2.5F);
        addAdultLeg(root, "left_front_leg", 3.0F, -2.5F);
        addAdultLeg(root, "right_hind_leg", -3.0F, 4.5F);
        addAdultLeg(root, "left_hind_leg", 3.0F, 4.5F);

        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition createBabyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild(
            "body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-3.0F, -2.0F, -3.5F, 6.0F, 4.0F, 7.0F),
            PartPose.offset(0.0F, 20.0F, 1.0F)
        );

        PartDefinition head = body.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 30)
                .addBox(-3.0F, -2.5F, -3.5F, 6.0F, 5.0F, 5.0F)
                .texOffs(48, 16)
                .addBox(-2.25F, -0.65F, -3.51F, 1.0F, 1.0F, 0.0F)
                .texOffs(48, 16)
                .addBox(1.25F, -0.65F, -3.51F, 1.0F, 1.0F, 0.0F),
            PartPose.offset(0.0F, -1.3F, -3.6F)
        );

        head.addOrReplaceChild(
            "head_bud",
            CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 1.5F, 2.0F),
            PartPose.offset(0.0F, -2.45F, -0.3F)
        );

        body.addOrReplaceChild("back_bud_left", CubeListBuilder.create(), PartPose.offset(1.5F, -2.0F, 1.0F));
        body.addOrReplaceChild("back_bud_right", CubeListBuilder.create(), PartPose.offset(-1.5F, -2.0F, 1.0F));

        addBabyLeg(root, "right_front_leg", -2.25F, -1.75F);
        addBabyLeg(root, "left_front_leg", 2.25F, -1.75F);
        addBabyLeg(root, "right_hind_leg", -2.25F, 3.0F);
        addBabyLeg(root, "left_hind_leg", 2.25F, 3.0F);

        return LayerDefinition.create(mesh, 64, 64);
    }

    private static void addAdultLeg(PartDefinition root, String name, float x, float z) {
        root.addOrReplaceChild(
            name,
            CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
            PartPose.offset(x, 20.0F, z)
        );
    }

    private static void addBabyLeg(PartDefinition root, String name, float x, float z) {
        root.addOrReplaceChild(
            name,
            CubeListBuilder.create().texOffs(36, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
            PartPose.offset(x, 22.0F, z)
        );
    }

    @Override
    public void setupAnim(GlowhopperRenderState state) {
        super.setupAnim(state);

        float phase = state.ageInTicks + state.animationOffset;
        float idleBreath = Mth.sin(phase * 0.12F) * 0.08F;
        float headWander = Mth.sin(phase * 0.055F) * 0.035F;
        float budSway = Mth.sin(phase * 0.16F) * 0.045F;

        if (state.carriedOnHead) {
            this.applyPronePose(true);
        } else if (state.resting) {
            this.applyPronePose(false);
            this.body.y += idleBreath * 0.5F;
            this.head.xRot += Mth.sin(phase * 0.07F) * 0.025F;
            this.head.yRot += headWander * 0.7F;
        } else {
            this.applyActivePose(state, phase, idleBreath, headWander);
        }

        if (state.eating) {
            this.head.xRot += 0.48F + Mth.sin(phase * 1.8F) * 0.10F;
            this.body.xRot += 0.035F;
        } else if (state.foraging && !state.airborne) {
            this.head.xRot -= 0.34F;
        }

        this.applyBudInertia(state, budSway);
    }

    private void applyActivePose(GlowhopperRenderState state, float phase, float idleBreath, float headWander) {
        float lookYaw = Mth.clamp(state.yRot * ((float)Math.PI / 180.0F), -0.55F, 0.55F);
        float lookPitch = Mth.clamp(state.xRot * ((float)Math.PI / 180.0F), -0.40F, 0.45F);

        this.head.yRot = lookYaw;
        this.head.xRot = lookPitch;

        if (state.airborne) {
            float vertical = Mth.clamp(state.verticalSpeed * 2.0F, -1.0F, 1.0F);
            float extension = 0.55F + 0.35F * Math.abs(vertical);

            this.body.y -= Math.max(0.0F, vertical) * 0.35F;
            this.body.xRot = state.foraging
                ? -0.22F * vertical
                : -0.10F * vertical;

            this.rightFrontLeg.xRot = -extension;
            this.leftFrontLeg.xRot = -extension;
            this.rightHindLeg.xRot = extension * 0.85F;
            this.leftHindLeg.xRot = extension * 0.85F;

            if (state.panicking) {
                this.body.xRot += 0.12F;
                this.head.xRot -= 0.08F;
            }

            if (state.foraging) {
                this.head.xRot -= 0.28F;
                this.rightHindLeg.xRot += 0.18F;
                this.leftHindLeg.xRot += 0.18F;
            }
            return;
        }

        float movement = Mth.clamp(state.walkAnimationSpeed, 0.0F, 1.0F);
        float groundedPulse = Math.abs(Mth.sin(state.walkAnimationPos * 0.75F)) * movement;

        this.body.y += idleBreath - groundedPulse * 0.15F;
        this.head.zRot += headWander * (1.0F - movement);

        this.rightFrontLeg.xRot = -groundedPulse * 0.16F;
        this.leftFrontLeg.xRot = -groundedPulse * 0.16F;
        this.rightHindLeg.xRot = groundedPulse * 0.12F;
        this.leftHindLeg.xRot = groundedPulse * 0.12F;

        if (state.panicking) {
            this.body.xRot = 0.10F;
            this.head.xRot -= 0.06F;
        }
    }

    private void applyPronePose(boolean carried) {
        float spread = carried ? 1.18F : 1.02F;

        this.body.y += carried ? 2.25F : 1.85F;
        this.body.xRot = carried ? 0.02F : 0.06F;

        this.head.y += carried ? 0.9F : 0.65F;
        this.head.z += carried ? 0.15F : 0.0F;
        this.head.xRot = carried ? -0.05F : 0.02F;

        this.rightFrontLeg.zRot = spread;
        this.leftFrontLeg.zRot = -spread;
        this.rightHindLeg.zRot = spread;
        this.leftHindLeg.zRot = -spread;

        this.rightFrontLeg.xRot = -0.48F;
        this.leftFrontLeg.xRot = -0.48F;
        this.rightHindLeg.xRot = 0.44F;
        this.leftHindLeg.xRot = 0.44F;

        this.rightFrontLeg.x -= carried ? 0.4F : 0.2F;
        this.leftFrontLeg.x += carried ? 0.4F : 0.2F;
        this.rightHindLeg.x -= carried ? 0.4F : 0.2F;
        this.leftHindLeg.x += carried ? 0.4F : 0.2F;
    }

    private void applyBudInertia(GlowhopperRenderState state, float sway) {
        float velocityLag = Mth.clamp(-state.verticalSpeed * 0.32F, -0.18F, 0.18F);
        float panicScale = state.panicking ? 1.5F : 1.0F;

        this.headBud.xRot += velocityLag * 1.15F;
        this.headBud.zRot += sway * panicScale;

        this.backBudLeft.xRot += velocityLag * 0.85F;
        this.backBudRight.xRot += velocityLag * 0.85F;
        this.backBudLeft.zRot += sway * 0.75F * panicScale;
        this.backBudRight.zRot -= sway * 0.75F * panicScale;
    }
}
