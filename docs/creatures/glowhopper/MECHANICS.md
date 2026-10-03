# Glowhopper Mechanics

This file records mechanics already confirmed by the creator. Values not confirmed here remain open decisions and are not to be invented during implementation.

## Base properties

- Passive creature.
- Approximate body length: 0.7 blocks.
- Approximate height: 0.5 blocks.
- Health: 10 points / 5 hearts.
- No attack.

## Spawning

- Primary spawn: on land in lush caves.
- Group size: 2–4.
- Secondary spread: may rarely appear in ordinary underground cave areas when a lush-cave biome can be sampled within 7 chunks / 112 blocks.
- The exact probability/rate of this secondary spawn is not yet specified.

## Glow charge

- Food/charge item: glow berries.
- Eating one glow berry refills charge to 15 minutes.
- Full charge corresponds to light level 15.
- Additional berries refill the state rather than stacking extra duration.
- Light decreases by 1 level after each elapsed minute.
- After 15 minutes, the Glowhopper is unlit.
- A wild Glowhopper seeks glow berries growing on cave vines within the currently specified reachable vertical range of about 3 blocks.
- It moves beneath the berry and jumps to bite/eat it; if it cannot reach the target it may remain below and attempt to reach it.

## Player interaction

- No taming requirement.
- Empty hand + sneak + right click on an adult places it on the player's head.
- The intended interaction uses the same action again to remove it; the exact input handling while the creature is already on the player's head still needs to be made implementation-safe.
- Only adults can be placed on the player's head.
- Glow charge continues to decay while carried on the player's head.
- Feeding an adult a glow berry refills glow charge and enters a 30-second breeding-ready window.

## Breeding and growth

- Two adult Glowhoppers that have each been fed a glow berry may breed if they meet during the 30-second breeding window.
- A newborn does not begin in the mature adult visual state.
- Natural growth time: 20 minutes.
- Feeding a juvenile a glow berry reduces **10% of its remaining growth time** per berry.
- Feeding a juvenile also starts its own 15-minute glow timer.
- The juvenile's exact emitted light strength while its lamps are visually immature remains unresolved.

## AI priority

Current intended priority order:

1. flee after being hurt;
2. breeding behavior while breeding-ready;
3. seek/eat reachable cave-vine glow berries;
4. idle/wander behavior.

The newly confirmed prone resting pose is part of idle presentation; its trigger frequency and duration are not yet specified.

## Movement

- Primary locomotion is hopping, broadly comparable to rabbit-like movement.
- Intended standing jump height is about 2.5 blocks so it can reach cave-vine berries.

## Drops

- Death drop: 0–2 glow berries.
- Experience: 1–3.

## Explicitly unresolved

These points must be decided before their implementation is treated as final:

- exact secondary-spawn chance/frequency;
- whether wild berry eating visibly consumes/removes the berry from the cave vine;
- hurt-flee duration/speed/distance;
- prone-rest trigger conditions, frequency and duration;
- safe and intuitive removal input while carried on the player's head;
- carried-state rules for damage, death, water, sleeping, logout and dimension travel;
- whether the carried Glowhopper keeps a normal hurtbox/collision target;
- juvenile emitted brightness while its lamp structures are immature.
