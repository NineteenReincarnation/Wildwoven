package io.github.nineteenreincarnation.wildwoven.mixin.server;

import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(
        method = "remove",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;save(Lnet/minecraft/server/level/ServerPlayer;)V",
            shift = At.Shift.AFTER
        )
    )
    private void wildwoven$unloadHeadGlowhopperWithPlayer(ServerPlayer player, CallbackInfo ci) {
        player.getPassengers().stream()
            .filter(GlowhopperEntity.class::isInstance)
            .map(GlowhopperEntity.class::cast)
            .toList()
            .forEach(glowhopper -> glowhopper.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER));
    }
}
