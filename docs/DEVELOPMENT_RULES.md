# Development Rules

## Branching

Development is performed directly on `main`.

- Do not create feature branches for normal Wildwoven development.
- Do not create version-isolation branches merely because Minecraft or loader versions change.
- Keep version-specific boundaries explicit in code only where upstream APIs require them.

## Creature specification

The creator owns creature behavior and gameplay identity.

If a concrete creature mechanic is unclear, implementation stops at that decision boundary and the question is returned to the creator. Do not silently invent missing behavior, balance values or interaction rules.

Technical implementation details may be decided in code when they do not alter the creature's intended gameplay.

## Structure

- Keep each creature isolated in its own package and documentation folder.
- Prefer high cohesion and low coupling.
- Reuse shared systems when reuse is real, not speculative.
- Avoid both monolithic all-creature classes and unnecessary micro-files.
- Keep loader-specific code thin.
- Keep resource and editable art files grouped by creature.

## Vanilla-oriented design

New content should read as Minecraft content first:

- simple, recognizable silhouettes;
- restrained texture detail;
- behavior communicated through movement and interaction rather than UI-heavy systems;
- no QTE-style interaction unless explicitly requested;
- avoid adding complexity that exists only to make the mod feel more elaborate.

## Change discipline

- Document confirmed creature rules before or alongside implementation.
- Mark unresolved decisions explicitly rather than guessing.
- Keep README and creature docs synchronized with material behavior changes.
- Treat successful compilation as necessary but not sufficient validation.
