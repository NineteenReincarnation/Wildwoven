package io.github.nineteenreincarnation.wildwoven.client.glowhopper;

import io.github.nineteenreincarnation.wildwoven.Wildwoven;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public final class GlowhopperModelLayers {
    public static final ModelLayerLocation ADULT = new ModelLayerLocation(
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, "glowhopper"),
        "main"
    );

    public static final ModelLayerLocation BABY = new ModelLayerLocation(
        Identifier.fromNamespaceAndPath(Wildwoven.MOD_ID, "glowhopper_baby"),
        "main"
    );

    private GlowhopperModelLayers() {
    }
}
