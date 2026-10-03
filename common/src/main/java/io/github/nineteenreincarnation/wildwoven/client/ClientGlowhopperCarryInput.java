package io.github.nineteenreincarnation.wildwoven.client;

import io.github.nineteenreincarnation.wildwoven.creature.glowhopper.GlowhopperEntity;
import io.github.nineteenreincarnation.wildwoven.network.RemoveHeadGlowhopperPayload;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;

public final class ClientGlowhopperCarryInput {
    private static Consumer<RemoveHeadGlowhopperPayload> sender;

    private ClientGlowhopperCarryInput() {
    }

    public static void installSender(Consumer<RemoveHeadGlowhopperPayload> payloadSender) {
        sender = Objects.requireNonNull(payloadSender, "payloadSender");
    }

    public static boolean tryRemoveHeadGlowhopper(Minecraft minecraft) {
        if (sender == null || minecraft.player == null) {
            return false;
        }

        if (!minecraft.player.isSecondaryUseActive()
            || !minecraft.player.getMainHandItem().isEmpty()
            || !minecraft.player.getOffhandItem().isEmpty()) {
            return false;
        }

        if (minecraft.hitResult != null && minecraft.hitResult.getType() != HitResult.Type.MISS) {
            return false;
        }

        boolean carryingGlowhopper = minecraft.player.getPassengers().stream()
            .anyMatch(GlowhopperEntity.class::isInstance);
        if (!carryingGlowhopper) {
            return false;
        }

        sender.accept(RemoveHeadGlowhopperPayload.INSTANCE);
        return true;
    }
}
