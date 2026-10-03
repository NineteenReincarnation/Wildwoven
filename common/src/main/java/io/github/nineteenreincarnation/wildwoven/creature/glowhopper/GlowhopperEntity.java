package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.jspecify.annotations.Nullable;

public final class GlowhopperEntity extends Animal {
    public static final int FULL_CHARGE_TICKS = 15 * 60 * 20;
    public static final int ADULT_BASE_LIGHT = 4;
    public static final int ADULT_MAX_LIGHT = 15;
    public static final int BABY_BASE_LIGHT = 2;
    public static final int BABY_MAX_LIGHT = 9;
    public static final int BERRY_SEARCH_LIGHT_THRESHOLD = 6;

    private static final EntityDataAccessor<Integer> DATA_CHARGE_TICKS =
        SynchedEntityData.defineId(GlowhopperEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_RESTING =
        SynchedEntityData.defineId(GlowhopperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_PANICKING =
        SynchedEntityData.defineId(GlowhopperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_FORAGING =
        SynchedEntityData.defineId(GlowhopperEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_EATING =
        SynchedEntityData.defineId(GlowhopperEntity.class, EntityDataSerializers.BOOLEAN);

    private int panicTicks;
    private int berrySearchCooldown;
    private int normalHopCooldown;
    private int carriedWaterTicks;
    private int eatAnimationTicks;
    private @Nullable BlockPos activeLightPos;
    private int activeLightLevel = -1;

    public GlowhopperEntity(EntityType<? extends GlowhopperEntity> type, net.minecraft.world.level.Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
            .add(Attributes.MAX_HEALTH, 10.0)
            .add(Attributes.MOVEMENT_SPEED, 0.26)
            .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GlowhopperPanicGoal(this, 1.6));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1, stack -> stack.is(Items.GLOW_BERRIES), false));
        this.goalSelector.addGoal(4, new GlowhopperEatGlowBerryGoal(this));
        this.goalSelector.addGoal(5, new GlowhopperRestGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHARGE_TICKS, 0);
        builder.define(DATA_RESTING, false);
        builder.define(DATA_PANICKING, false);
        builder.define(DATA_FORAGING, false);
        builder.define(DATA_EATING, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("GlowCharge", this.getGlowChargeTicks());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setGlowChargeTicks(input.getIntOr("GlowCharge", 0));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.getGlowChargeTicks() > 0) {
                this.setGlowChargeTicks(this.getGlowChargeTicks() - 1);
            }

            if (this.berrySearchCooldown > 0) {
                this.berrySearchCooldown--;
            }

            if (this.eatAnimationTicks > 0) {
                this.eatAnimationTicks--;
                if (this.eatAnimationTicks == 0) {
                    this.entityData.set(DATA_EATING, false);
                }
            }

            this.tickNormalHopping();
            this.tickHeadCarryWater();

            if (!(this.isPassenger() && this.getVehicle() instanceof Player)) {
                this.updateWorldLight(serverLevel);
            }
        }
    }

    @Override
    public void rideTick() {
        if (this.getVehicle() instanceof Player player) {
            this.setDeltaMovement(Vec3.ZERO);
            this.tick();

            if (this.getVehicle() == player) {
                this.setPos(player.getX(), player.getBoundingBox().maxY + 0.02, player.getZ());
                this.setYRot(player.getYHeadRot());
                this.setYHeadRot(player.getYHeadRot());
                this.setYBodyRot(player.getYHeadRot());

                if (this.level() instanceof ServerLevel serverLevel) {
                    this.updateWorldLight(serverLevel);
                }
            }
        } else {
            super.rideTick();
        }
    }

    @Override
    public void onRemoval(RemovalReason reason) {
        this.clearWorldLight();
        super.onRemoval(reason);
    }

    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource source, float amount) {
        super.actuallyHurt(level, source, amount);

        if (this.isPassenger() && this.getVehicle() instanceof Player) {
            this.stopRiding();
        }

        this.panicTicks = 80 + this.getRandom().nextInt(41);
        this.entityData.set(DATA_PANICKING, true);
        this.setResting(false);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        int count = this.getRandom().nextInt(3);
        if (count > 0) {
            this.spawnAtLocation(level, new ItemStack(Items.GLOW_BERRIES, count));
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.GLOW_BERRIES)) {
            if (this.isPassenger() && this.getVehicle() instanceof Player) {
                return InteractionResult.PASS;
            }

            if (!this.level().isClientSide()) {
                this.refillGlowCharge();
                this.usePlayerItem(player, hand, stack);

                if (this.isBaby()) {
                    int seconds = AgeableMob.getSpeedUpSecondsWhenFeeding(-this.getAge());
                    this.ageUp(seconds, true);
                } else if (this.getAge() == 0) {
                    this.setInLove(player);
                }
            }

            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        if (!this.isBaby()
            && stack.isEmpty()
            && player.isSecondaryUseActive()
            && !this.isPassenger()
            && player.getPassengers().stream().noneMatch(GlowhopperEntity.class::isInstance)) {
            if (!this.level().isClientSide()) {
                this.setResting(false);
                this.startRiding(player, true, true);
            }

            return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.GLOW_BERRIES);
    }

    @Override
    public @Nullable GlowhopperEntity getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return GlowhopperModule.entityType().create(level, EntitySpawnReason.BREEDING);
    }

    public int getGlowChargeTicks() {
        return this.entityData.get(DATA_CHARGE_TICKS);
    }

    public void setGlowChargeTicks(int ticks) {
        this.entityData.set(DATA_CHARGE_TICKS, Mth.clamp(ticks, 0, FULL_CHARGE_TICKS));
    }

    public void refillGlowCharge() {
        this.setGlowChargeTicks(FULL_CHARGE_TICKS);
    }

    public int getCurrentLightLevel() {
        int base = this.isBaby() ? BABY_BASE_LIGHT : ADULT_BASE_LIGHT;
        int max = this.isBaby() ? BABY_MAX_LIGHT : ADULT_MAX_LIGHT;
        int charge = this.getGlowChargeTicks();
        if (charge <= 0) {
            return base;
        }

        double fraction = charge / (double)FULL_CHARGE_TICKS;
        return Mth.clamp(base + Mth.ceil((max - base) * fraction), base, max);
    }

    public boolean isResting() {
        return this.entityData.get(DATA_RESTING);
    }

    public boolean isPanicking() {
        return this.entityData.get(DATA_PANICKING);
    }

    public boolean isForaging() {
        return this.entityData.get(DATA_FORAGING);
    }

    public boolean isEatingBerry() {
        return this.entityData.get(DATA_EATING);
    }

    void setForaging(boolean foraging) {
        this.entityData.set(DATA_FORAGING, foraging);
        if (foraging) {
            this.setResting(false);
        }
    }

    void startEatAnimation() {
        this.eatAnimationTicks = 10;
        this.entityData.set(DATA_EATING, true);
    }

    void setResting(boolean resting) {
        this.entityData.set(DATA_RESTING, resting);
    }

    int getPanicTicks() {
        return this.panicTicks;
    }

    void tickPanic() {
        if (this.panicTicks > 0) {
            this.panicTicks--;
        }

        if (this.panicTicks <= 0 && this.entityData.get(DATA_PANICKING)) {
            this.entityData.set(DATA_PANICKING, false);
        }
    }

    boolean canSearchForBerry() {
        return this.berrySearchCooldown <= 0;
    }

    void scheduleNextBerrySearch() {
        this.berrySearchCooldown = 600 + this.getRandom().nextInt(601);
    }

    void performBerryJump() {
        if (!this.onGround()) {
            return;
        }

        Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(movement.x, 0.68, movement.z);
        this.setIgnoreFallDamageFromCurrentImpulse(true, this.position());
        this.needsSync = true;
    }

    private void tickNormalHopping() {
        if (this.isPassenger() || this.isResting() || this.getPanicTicks() > 0 || !this.onGround()) {
            return;
        }

        if (this.normalHopCooldown > 0) {
            this.normalHopCooldown--;
            return;
        }

        if (!this.getNavigation().isDone() && this.getDeltaMovement().horizontalDistanceSqr() > 0.0004) {
            Vec3 movement = this.getDeltaMovement();
            this.setDeltaMovement(movement.x, 0.28, movement.z);
            this.needsSync = true;
            this.normalHopCooldown = 7 + this.getRandom().nextInt(5);
        }
    }

    private void tickHeadCarryWater() {
        if (!(this.getVehicle() instanceof Player player)) {
            this.carriedWaterTicks = 0;
            return;
        }

        if (player.isInWater()) {
            this.carriedWaterTicks++;
            if (this.carriedWaterTicks >= 90) {
                this.stopRiding();
                this.carriedWaterTicks = 0;
            }
        } else {
            this.carriedWaterTicks = 0;
        }
    }

    private void updateWorldLight(ServerLevel level) {
        int desiredLight = this.getCurrentLightLevel();
        BlockPos desiredPos = this.blockPosition();

        if (this.activeLightPos != null
            && (!this.activeLightPos.equals(desiredPos) || this.activeLightLevel != desiredLight)) {
            GlowhopperLightManager.remove(level, this.activeLightPos, this.getUUID());
            this.activeLightPos = null;
            this.activeLightLevel = -1;
        }

        if (this.activeLightPos == null
            && GlowhopperLightManager.update(level, desiredPos, this.getUUID(), desiredLight)) {
            this.activeLightPos = desiredPos.immutable();
            this.activeLightLevel = desiredLight;
        }
    }

    private void clearWorldLight() {
        if (this.activeLightPos != null && this.level() instanceof ServerLevel serverLevel) {
            GlowhopperLightManager.remove(serverLevel, this.activeLightPos, this.getUUID());
        }

        this.activeLightPos = null;
        this.activeLightLevel = -1;
    }

    public static boolean checkSpawnRules(
        EntityType<GlowhopperEntity> type,
        ServerLevelAccessor level,
        EntitySpawnReason reason,
        BlockPos pos,
        RandomSource random
    ) {
        BlockState below = level.getBlockState(pos.below());
        if (!below.isFaceSturdy(level, pos.below(), Direction.UP)) {
            return false;
        }

        if (level.getBiome(pos).is(Biomes.LUSH_CAVES)) {
            return true;
        }

        if (level.canSeeSky(pos)) {
            return false;
        }

        return hasNearbyLushCave(level, pos);
    }

    private static boolean hasNearbyLushCave(ServerLevelAccessor level, BlockPos origin) {
        final int horizontalRadius = 112;
        final int horizontalStep = 32;
        final int[] yOffsets = {-24, 0, 24};

        for (int yOffset : yOffsets) {
            for (int x = -horizontalRadius; x <= horizontalRadius; x += horizontalStep) {
                for (int z = -horizontalRadius; z <= horizontalRadius; z += horizontalStep) {
                    if (x * x + z * z > horizontalRadius * horizontalRadius) {
                        continue;
                    }

                    BlockPos sample = origin.offset(x, yOffset, z);
                    if (level.getBiome(sample).is(Biomes.LUSH_CAVES)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
