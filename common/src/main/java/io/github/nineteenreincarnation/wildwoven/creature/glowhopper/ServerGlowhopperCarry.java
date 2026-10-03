package io.github.nineteenreincarnation.wildwoven.creature.glowhopper;

import net.minecraft.server.level.ServerPlayer;

public final class ServerGlowhopperCarry {
    private ServerGlowhopperCarry() {
    }

    public static void removeFromHead(ServerPlayer player) {
        if (!player.isShiftKeyDown()
            || !player.getMainHandItem().isEmpty()
            || !player.getOffhandItem().isEmpty()) {
            return;
        }

        player.getPassengers().stream()
            .filter(GlowhopperEntity.class::isInstance)
            .map(GlowhopperEntity.class::cast)
            .findFirst()
            .ifPresent(GlowhopperEntity::stopRiding);
    }
}
