package io.github.nineteenreincarnation.wildwoven.network;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record RemoveHeadGlowhopperPayload() implements CustomPacketPayload {
    public static final RemoveHeadGlowhopperPayload INSTANCE = new RemoveHeadGlowhopperPayload();

    public static final Type<RemoveHeadGlowhopperPayload> TYPE = new Type<>(
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, "remove_head_glowhopper")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveHeadGlowhopperPayload> STREAM_CODEC =
        new StreamCodec<>() {
            @Override
            public RemoveHeadGlowhopperPayload decode(RegistryFriendlyByteBuf buffer) {
                return INSTANCE;
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, RemoveHeadGlowhopperPayload payload) {
            }
        };

    @Override
    public Type<RemoveHeadGlowhopperPayload> type() {
        return TYPE;
    }
}
