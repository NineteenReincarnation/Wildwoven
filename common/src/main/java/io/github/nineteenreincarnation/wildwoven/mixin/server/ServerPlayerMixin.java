package io.github.nineteenreincarnation.wildwoven.mixin.server;

import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperEntity;
import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperModule;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Unique
    private @Nullable GlowhopperEntity wildwoven$dimensionCarriedGlowhopper;

    @Inject(
        method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
        at = @At("HEAD")
    )
    private void wildwoven$captureHeadGlowhopper(
        TeleportTransition transition,
        CallbackInfoReturnable<ServerPlayer> cir
    ) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        if (transition.newLevel().dimension().equals(player.level().dimension())) {
            this.wildwoven$dimensionCarriedGlowhopper = null;
            return;
        }

        this.wildwoven$dimensionCarriedGlowhopper = player.getPassengers().stream()
            .filter(GlowhopperEntity.class::isInstance)
            .map(GlowhopperEntity.class::cast)
            .findFirst()
            .orElse(null);
    }

    @Inject(
        method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
        at = @At("RETURN")
    )
    private void wildwoven$restoreHeadGlowhopperAfterDimensionChange(
        TeleportTransition transition,
        CallbackInfoReturnable<ServerPlayer> cir
    ) {
        GlowhopperEntity carried = this.wildwoven$dimensionCarriedGlowhopper;
        this.wildwoven$dimensionCarriedGlowhopper = null;

        ServerPlayer player = cir.getReturnValue();
        if (carried == null || player == null || carried.isRemoved()) {
            return;
        }

        if (carried.level() == player.level()) {
            carried.startRiding(player, true, false);
            return;
        }

        Entity teleported = carried.teleport(
            new TeleportTransition(
                player.level(),
                player.position(),
                Vec3.ZERO,
                player.getYRot(),
                player.getXRot(),
                Set.of(),
                TeleportTransition.DO_NOTHING
            )
        );

        if (teleported instanceof GlowhopperEntity glowhopper) {
            glowhopper.startRiding(player, true, false);
        }
    }

    @Inject(method = "loadAndSpawnParentVehicle", at = @At("TAIL"))
    private void wildwoven$restoreSavedHeadGlowhopper(ValueInput playerInput, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        if (player.getPassengers().stream().anyMatch(GlowhopperEntity.class::isInstance)) {
            return;
        }

        for (ValueInput passengerInput : playerInput.childrenListOrEmpty("Passengers")) {
            if (EntityType.by(passengerInput).orElse(null) != GlowhopperModule.entityType()) {
                continue;
            }

            Entity loaded = EntityType.loadEntityRecursive(
                passengerInput,
                player.level(),
                EntitySpawnReason.LOAD,
                entity -> player.level().addWithUUID(entity) ? entity : null
            );

            if (loaded instanceof GlowhopperEntity glowhopper) {
                glowhopper.startRiding(player, true, false);
                break;
            }
        }
    }
}
