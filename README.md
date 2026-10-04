# Wildwoven

Wildwoven is a long-term Minecraft creature project focused on adding new mobs that feel at home in the vanilla game.

Repository: https://github.com/NineteenReincarnation/Wildwoven

## Target

- Minecraft: **26.2**
- Java: **25**
- Loaders: **Fabric + NeoForge**
- Development branch: **main**
- Initial version line: **1.1.x+26.2**

## Design direction

Wildwoven prioritizes:

- vanilla-oriented creature silhouettes, textures, behavior and interaction;
- gameplay mechanics that fit Minecraft instead of replacing its core style;
- clear separation between creatures so the project can grow without becoming a single tangled entity package;
- shared infrastructure only when it is genuinely reusable across multiple creatures;
- loader-independent logic in `common/` whenever practical;
- explicit creator decisions for creature behavior: unclear creature mechanics are not invented during implementation.

## Creatures

| Creature | ID | Status |
| --- | --- | --- |
| Glowhopper / 灯莓兽 | `wildwoven:glowhopper` | Core mechanics, reference-based models, textures and walking implemented; runtime playtesting pending |

## Layout

```text
common/       shared Minecraft-facing and loader-independent code
fabric/       Fabric bootstrap and loader-specific integration
neoforge/     NeoForge bootstrap and loader-specific integration
docs/         architecture, rules and creature specifications
art/          editable source assets grouped by creature
```

Each creature receives its own code, documentation and art boundary. Glowhopper begins under `creature/glowhopper/`, `docs/creatures/glowhopper/` and `art/creatures/glowhopper/`.

## Build

```bash
./gradlew buildAll
```

Artifacts are produced separately for Fabric and NeoForge.

> Wildwoven is under active development. A successful compile is not treated as proof of runtime behavior, visual correctness, balance or compatibility.
