# Glowhopper / 灯莓兽

## Identity

- Type: passive creature.
- Intended biome identity: lush caves and nearby cave systems.
- Overall design target: simple Minecraft-vanilla creature rather than a detailed voxel sculpture.
- Approximate adult size currently specified as about **0.7 blocks long and 0.5 blocks high**.
- Health: **10 points / 5 hearts**.
- Attack: none.

## Visual rules

### Adult

- Blocky, low-detail silhouette.
- Four legs are simple small cuboids; no detailed feet or paw sculpting.
- Texture uses a restrained number of flat color regions rather than noisy voxel-by-voxel color variation.
- Exactly one pair of eyes:
  - front view shows two eyes;
  - side view shows only the eye on that visible side.
- Mature lamp/berry structures use the adult glow-berry visual language.

### Juvenile

The juvenile follows vanilla baby-mob design language instead of being a uniformly scaled adult.

- cuter proportions;
- larger-looking head relative to the body;
- shorter body and legs;
- fewer visual details;
- fewer lamp/berry structures;
- lamp/berry structures look immature and remain greenish, closer to the body's green palette rather than mature orange-yellow berries.

Whether the juvenile's immature lamps also change gameplay light output remains an unresolved mechanic and is not assumed here.

### Resting pose

Glowhopper has a prone resting/idle pose. Its body lies low and all four legs extend outward around the body.

### Player head pose

When carried on a player's head, Glowhopper uses the same basic prone language:

- belly/body rests over the top of the player's head;
- all four legs extend outward around the head;
- it must not appear to stand upright on the player.

The model and animation structure should be authored so this pose can be represented cleanly without distorting the normal standing pose.

## Asset boundary

Editable source models and references belong under:

`art/creatures/glowhopper/`

Runtime textures belong under:

`common/src/main/resources/assets/wildwoven/textures/entity/glowhopper/`
