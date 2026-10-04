# Glowhopper source assets

The creator's design sheet and adult close-up are the reference for this reconstruction.

- `build_assets.py`: canonical cuboids, pivots, UVs and adult/baby palettes.
- `glowhopper.bbmodel`, `glowhopper_baby.bbmodel`: editable, embedded-texture Blockbench projects.
- `adult_mesh.json`, `baby_mesh.json`: generated geometry used for offline visual QA.
- `render_preview.py`: depth-buffered orthographic renders and walking animation.
- `previews/adult_detail.png`: enlarged adult at the reference front-view angle.
- `previews/model_sheet.png`: adult/baby orthographic views and action poses.
- `previews/walk.gif`: adult/baby procedural walking preview.

Use Python 3 with Pillow and NumPy:

```sh
python art/creatures/glowhopper/build_assets.py
python art/creatures/glowhopper/render_preview.py
```

The generator updates only the marked geometry region in runtime `GlowhopperModel.java`;
handwritten animation remains intact. It also regenerates both Blockbench projects and
runtime textures. Keep edits to generated `.bbmodel` files synchronized with the Python
source before regenerating. Native model tests live in `fabric/src/test/java/`.
Offline renders use the actual mesh/texture assets with approximate lighting.
