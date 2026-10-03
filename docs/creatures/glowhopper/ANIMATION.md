# Glowhopper Animation Direction

## Goal

Glowhopper animation should feel alive while remaining recognizably vanilla Minecraft. The design deliberately avoids constant motion, exaggerated squash-and-stretch, or highly cinematic third-party creature animation.

The implementation studies current 26.2 vanilla patterns from rabbits, frogs, foxes and sniffers:

- **Rabbit:** a clear hop state instead of continuous leg cycling, plus a separate low-frequency idle head motion.
- **Frog:** distinct state animations for locomotion and special actions rather than one universal loop.
- **Fox:** explicit pose changes for rest/sleep instead of trying to fake every state through the walk cycle.
- **Sniffer:** large actions have readable anticipation and recovery, with secondary parts following the main body motion.

Wildwoven applies those principles without copying their shapes or animation definitions.

## Motion hierarchy

Glowhopper animation follows this order:

1. body/root motion communicates the action;
2. legs support the action;
3. head keeps attention readable;
4. lamp buds receive small delayed/inertial motion.

The buds must never wobble continuously like rubber antennae. Their movement is secondary and restrained.

## Adult states

### Quiet idle

- very small body breathing motion;
- slow, low-amplitude head drift;
- no constant foot shuffling;
- lamp buds sway only slightly.

### Prone rest

- body lowered close to the ground;
- all four legs spread outward;
- head remains low;
- subtle breathing/head motion may continue;
- intended duration remains 5–15 seconds from the gameplay specification.

### Normal hop

- small movement hop;
- legs extend during airtime rather than cycling like a walking quadruped;
- body pitches only slightly;
- landing returns quickly to the neutral pose.

### Panic hop

- same locomotion language as the normal hop;
- stronger forward body bias;
- quicker-looking secondary bud response;
- no separate exaggerated cartoon run.

### Berry-foraging high jump

- visually distinct from the normal hop;
- stronger leg extension;
- body and head bias upward toward the target;
- bud inertia is more visible because vertical speed is greater;
- gameplay jump height remains controlled by entity logic, not animation.

### Eat

- short downward/front head dip;
- small repeated bite motion only while the eat state is active;
- body contribution is minimal.

### Player-head carry

- shares the prone language;
- body is flatter and more stable than ground rest;
- all four legs extend farther around the player's head;
- motion is intentionally quiet to avoid becoming visually noisy during normal player movement.

## Juvenile

Juveniles use the same animation language but a separate model:

- proportionally larger head;
- shorter body and much shorter cuboid legs;
- only one immature lamp bud in the first model pass;
- no adult wild-foraging high-jump behavior;
- smaller secondary-motion amplitude.

The juvenile should read as simpler and cuter, not as a uniformly scaled adult.

## Current implementation

The first implementation uses procedural pose blending driven by render state:

- rest;
- carried-on-head;
- panic;
- foraging;
- eating;
- airborne/vertical speed;
- vanilla look and walk state.

This keeps the animation responsive to actual physics while leaving room for authored keyframe clips later if runtime testing shows a specific action needs stronger anticipation or recovery.

## Runtime validation

The model/animation implementation must still be tested in game for:

- silhouette at normal Minecraft camera distances;
- eye readability from front and side;
- adult versus baby proportions;
- leg spread against player skins and helmets;
- hop cadence at real movement speed;
- high-jump readability;
- lamp-bud motion at low FPS and high FPS;
- no clipping during boats, minecarts, leashes and head carrying;
- compatibility with player-animation resource packs while the Glowhopper is carried.
