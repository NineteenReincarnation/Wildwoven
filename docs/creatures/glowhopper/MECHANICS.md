# Glowhopper Mechanics

This document records the currently confirmed Glowhopper rules. Implementation details may be tuned for performance or compatibility without changing these gameplay rules.

## Base properties

- Passive creature.
- Approximate adult body length: 0.7 blocks.
- Approximate adult height: 0.5 blocks.
- Health: 10 points / 5 hearts.
- No attack.
- Can be leashed.
- Can enter boats and minecarts.
- Follows a player holding glow berries.

## Spawning

- Primary natural spawn: land inside lush caves.
- Pack size: 2–4.
- Initial main spawn weight: 80; intended as medium-low density and subject to playtest tuning.
- Secondary spread: ordinary underground Overworld cave positions may spawn Glowhoppers when a lush-cave biome is sampled within about 7 chunks / 112 blocks.
- Initial spread weight: 8, approximately 10% of the primary weight.
- Surface positions outside lush caves are rejected.
- The 112-block proximity check uses sparse biome samples rather than checking every block so the mechanic does not create excessive spawn-time work.

## Glow system

Glowhoppers never become completely dark.

### Adult

- Baseline light: 4.
- Fully charged light: 15.

### Juvenile

- Baseline light: 2.
- Fully charged light: 9.
- The lower strength matches the smaller yellow/gold juvenile lamps in the reference.

### Charging

- Glow berries are the charge food.
- One glow berry refills the charge to 15 minutes.
- Additional berries refill the timer; they do not stack beyond full.
- During those 15 minutes the emitted light gradually falls from the charged maximum toward the baseline value.
- Once charge expires, the creature remains at its baseline light indefinitely.

The emitted light is **real Minecraft world light**, not only a client visual. It therefore participates in normal block-light calculations and can affect hostile-mob spawning.

## Wild berry feeding

- Adult Glowhoppers begin periodically looking for glow berries when their current light is 6 or lower.
- Search area is approximately 8 blocks horizontally and 3 blocks vertically.
- If none are found, normal life continues and another search is attempted after a random 30–60 seconds.
- A Glowhopper that reaches a berry eats the berry but leaves the cave vine intact.
- Wild self-feeding only recharges glow; it does not trigger breeding.
- Juveniles do not perform the adult high-jump wild-foraging behavior.

## Player feeding, breeding and growth

- Feeding any Glowhopper a glow berry refills its charge.
- Feeding an eligible adult also enters the normal 30-second breeding-ready state.
- Two breeding-ready adults can breed using vanilla animal behavior.
- After breeding, vanilla's 5-minute parent breeding cooldown is used.
- Natural juvenile growth time is 20 minutes.
- Feeding a juvenile reduces 10% of its remaining growth time per berry and also refills its glow charge.

## AI priority

1. Flee after being hurt.
2. Breed while breeding-ready.
3. Follow a tempting player / forage for glow berries when appropriate.
4. Rest or wander.

### Hurt flee

- Damage triggers approximately 4–6 seconds of faster ground movement.
- Afterward the creature returns to its normal behavior.
- If a head-carried Glowhopper itself is damaged, it immediately gets down and enters this flee behavior.
- Damage to the player alone does not knock the Glowhopper off.

### Rest

- When idle, a Glowhopper may lie prone with all four legs spread outward.
- A rest lasts roughly 5–15 seconds.
- Rest attempts are deliberately infrequent rather than constant.
- The final model/animation may add small idle head movement without changing gameplay.

## Movement

- Normal locomotion uses quadruped walking, as requested by the creator; vanilla navigation still jumps over obstacles.
- The approximately 2.5-block jump is reserved for adult berry-foraging attempts.
- A self-initiated high foraging jump receives fall-damage grace for that jump.
- Ordinary large falls still use normal fall damage.

## Player head carry

### Put on

- Adult only.
- Empty hand + sneak + right click the Glowhopper.
- No taming is required.
- A helmet does not block carrying.
- One player may carry at most one Glowhopper.

### Pose

- The Glowhopper lies prone on the player's head.
- All four legs spread outward around the head.
- It must not appear to stand on the player.

### While carried

- Glow charge continues to decay.
- Its real world light follows the player.
- The Glowhopper remains attackable.
- Player damage alone does not remove it.
- If the Glowhopper is directly damaged, it immediately dismounts and flees.
- Short water exposure is tolerated.
- After about 4.5 seconds of continuous water exposure it gets down, representing it recognizing danger.
- It is intended to follow the player through teleportation, dimension changes and relogging; this must be verified at runtime.
- For now it cannot be fed while on the player's head; take it down first.

### Take off

- Intended input: empty hand + sneak + right click air.
- This requires a small client-to-server interaction bridge because vanilla sends no ordinary use-item packet for an empty-hand air click.
- The network/input bridge is implemented separately from the entity's normal interaction code.

## Drops

- 0–2 glow berries.
- 1–3 experience, matching the vanilla animal base range.

## Validation still required

The rules above are considered decided, but the following need runtime validation rather than further design invention:

- actual lush-cave and nearby-cave spawn density;
- cost of the 112-block biome proximity sampling;
- high-jump height and landing behavior;
- real-light cleanup across chunk unloads, crashes and overlapping Glowhoppers;
- passenger persistence across logout and dimension travel;
- head hitbox and final visual placement;
- resource-pack/player-animation compatibility after the real model is added.
