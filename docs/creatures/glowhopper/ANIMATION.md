# Glowhopper Animation

## Walking

Normal ground navigation uses vanilla MoveControl / JumpControl, replacing the previous
rabbit controller that stopped movement on the ground and forced repeated hops.

Actual vanilla walk distance drives a restrained quadruped cycle. Right front/left rear
move together, opposed to left front/right rear. Each swing foot lifts; stance-foot
height compensates the rotated cuboid so its lowest corner stays above the ground.
The body has small vertical/roll movement; the stalk responds gently. Zero movement
returns all feet to neutral. Juveniles use shorter legs and smaller steps.

## Other states

- Idle: small breathing and slow face drift; no foot shuffling.
- Rest: body lowered and four legs laid almost flat, with breathing.
- Head carry: belly meets the mounting surface; legs spread, with no walk/breath loop.
- Jump: front and hind legs extend in opposite directions; body pitch follows vertical speed.
- Berry forage: an additional upward face/body bias during the existing high jump.
- Eat: small repeated face dips while the entity eating flag is active.
- Panic: faster vanilla navigation, using the walking cycle; obstacle jumps remain supported.

Every render setup resets the baked pose, preventing accumulated offsets between states.

## Editable previews

Adult and baby `.bbmodel` files contain walk, rest, head_carry, jump and eat clips.
Their walk clips use the same diagonal pairing, foot clearance and lift as the runtime
cycle. Runtime remains driven by entity state rather than these preview clips.
`render_preview.py` renders real generated geometry/atlases with a depth buffer; its
lighting is an offline approximation. It produces an adult detail, contact sheet and GIF.

## Validation

JUnit checks the actual Minecraft-baked meshes for diagonal pairing, neutral feet on
stopping, no floor penetration over a full walk cycle, prone/carry ground contact and
repeatable pose resets for adults and babies. Fabric and NeoForge must both build.
Game checks still needed: appearance in lush cave lighting, movement cadence, helmets,
boats, minecarts and leashes. Offline renders and a successful build do not certify these.
