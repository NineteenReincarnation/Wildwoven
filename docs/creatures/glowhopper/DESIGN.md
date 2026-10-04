# Glowhopper / 灯莓兽

## Visual authority

The creator's three-view sheet and enlarged adult front view are the visual reference.
The current implementation follows their protruding cuboid moss clumps, tan face,
brown-green body, thick short square legs, stepped hooked stalk and framed lanterns.
Earlier placeholder instructions about flat regions, three buds, or green juvenile
buds do not describe the approved reference and have been superseded.

## Adult geometry and color

- A broad cuboid body with an inset tan face; no separate projecting snout.
- The face replaces the front body slice and remains rigid with the moss shell.
  Look, eat and idle animation moves the body/model rather than turning a face slab.
- A flat moss canopy with separately modeled forehead, side and rear clumps.
- Forehead clumps have distinct depths and hanging edges; texture cannot replace them.
- Exactly two deep brown square eyes, on the front face only.
- Four short, thick cuboid legs, with lighter brown upper halves and darker lower halves.
- One stalk made of three staggered green cuboids, with a hanging tip lantern.
- Two forward side lanterns and two rear corner lanterns, not buds on top of the back.
- Mature lanterns have saturated orange edges and a pale yellow central band.
- Moss has restrained patches and separate light/mid/dark olive greens.
- Approximate 0.7-block body length. The ornamental stalk extends above the body.

## Juvenile

- Separate mesh, about 70% adult width/length with shorter legs and fewer forehead clumps.
- Brighter moss and smaller gold/yellow lamps instead of the adult orange lamp palette.
- Retains one hooked tip lamp and four corner lamps, as depicted in the three-view sheet.
- Gameplay light remains the previously confirmed 2 baseline / 9 maximum.

## Poses

Ground rest lowers the body and lays all four legs nearly flat around it. When carried,
the belly sits at the player's head surface and the legs remain spread. Walking uses
alternating diagonal pairs; jumping extends front and rear legs in opposing directions.

## Asset boundary

`art/creatures/glowhopper/build_assets.py` is the geometry/UV/palette source. It generates
both runtime Java layers, their four texture atlases, JSON meshes and editable adult/baby
Blockbench projects. Java procedural animation lives in `GlowhopperModel.java`.
Textures belong in `common/src/main/resources/assets/wildwoven/textures/entity/glowhopper/`.
