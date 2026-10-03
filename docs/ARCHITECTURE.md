# Architecture

## Project boundary

Wildwoven uses a small multi-loader layout modeled after Fishing Reimagined.

- `common/` owns loader-independent code and Minecraft-facing code that can be shared unchanged.
- `fabric/` owns Fabric bootstrap and Fabric-only integration.
- `neoforge/` owns NeoForge bootstrap and NeoForge-only integration.
- `docs/` owns project rules and creature specifications.
- `art/` owns editable source assets that should not be mixed into runtime code.

Shared code must not import Fabric or NeoForge APIs.

## Creature ownership

Every creature is treated as its own feature boundary.

For Glowhopper:

```text
common/src/main/java/io/github/nineteenreincarnation/wildwoven/creature/glowhopper/
docs/creatures/glowhopper/
art/creatures/glowhopper/
common/src/main/resources/assets/wildwoven/textures/entity/glowhopper/
```

As implementation grows, Glowhopper-specific entity logic, AI goals, interactions, state, client model/render code and tests stay under the Glowhopper boundary unless a system is proven reusable by another creature.

Do not create a large generic abstraction before there is a second real use case.

## Shared systems

A system may move out of a creature package only when:

1. at least two creatures need the same behavior or infrastructure;
2. the shared API does not encode assumptions unique to one creature;
3. moving it genuinely reduces duplication or coupling.

## Authority

Gameplay state is server-authoritative. Client code renders synchronized state and owns presentation-only behavior.

## Compatibility

When Wildwoven affects player presentation, such as carrying a creature on the player's head, integration should preserve existing player/resource-pack animation behavior where technically possible and layer Wildwoven-specific presentation on top instead of replacing unrelated animation state.

## Validation

Compilation is only a static gate. Runtime spawning, AI behavior, interactions, multiplayer synchronization, animation, resource-pack compatibility and performance require separate validation.
