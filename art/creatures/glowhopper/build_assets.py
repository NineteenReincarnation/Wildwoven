"""Rebuild Java cuboids, pixel atlases and editable Blockbench models from one source.

Requires Pillow. Coordinates use Minecraft's model convention (y down, feet at 24).
No generated illustration is substituted for a render of the actual geometry.
"""
from pathlib import Path
import base64
import copy
import io
import json
import math
import re
import uuid
from PIL import Image

HERE = Path(__file__).resolve().parent
ROOT = HERE.parents[2]
TEXTURES = ROOT / 'common/src/main/resources/assets/wildwoven/textures/entity/glowhopper'
JAVA = ROOT / 'common/src/main/java/io/github/nineteenreincarnation/wildwoven/client/glowhopper/GlowhopperModel.java'
SIZE = 128
PIXELS = 2


def mesh(baby=False):
    parts = []
    def part(name, parent, pivot, cubes):
        parts.append(dict(name=name, parent=parent, pivot=pivot, cubes=cubes))
    def box(name, xyz, size, material):
        return dict(name=name, xyz=xyz, size=size, material=material)
    # Measured against the creator's enlarged adult front view.
    # A full tan cuboid sits beneath separate, protruding moss clumps.
    k = 0.70 if baby else 1.0
    body_y = 20.15 if baby else 17.5
    def scaled(cubes):
        for cube in cubes:
            cube['xyz'] = [q*k for q in cube['xyz']]
            cube['size'] = [q*k for q in cube['size']]
        return cubes
    part('body', None, [0, body_y, 0], scaled([
        box('brown_green_body', [-5, -3.5, -5.5], [10, 7, 11], 'shell'),
        box('flat_moss_top', [-5, -4.15, -5.5], [10, 0.65, 11], 'moss_mid'),
        box('top_left_patch', [-4.8, -4.12, -2], [3, 0.12, 3], 'moss_light'),
        box('top_rear_patch', [0.2, -4.10, 1], [3.8, 0.10, 3.5], 'moss_dark'),
        box('right_side_front_clump', [-6.2, -2.5, -4.6], [1.4, 2, 2.4], 'moss_light'),
        box('left_side_front_clump', [4.8, -2.5, -4.6], [1.4, 2, 2.4], 'moss_light'),
        box('right_side_middle_clump', [-5.35, -3.2, -1.8], [0.55, 3.2, 3.5], 'moss_mid'),
        box('left_side_middle_clump', [4.8, -3.2, -1.8], [0.55, 3.2, 3.5], 'moss_mid'),
        box('right_side_rear_clump', [-6.0, -2.5, 2.7], [1.2, 2, 2.4], 'moss_dark'),
        box('left_side_rear_clump', [4.8, -2.5, 2.7], [1.2, 2, 2.4], 'moss_dark'),
        box('back_left_moss', [-5, -3.6, 4.7], [3.2, 3.2, 1], 'moss_mid'),
        box('back_middle_moss', [-1.8, -3.8, 4.9], [3.5, 4, 0.8], 'moss_light'),
        box('back_right_moss', [1.7, -3.5, 4.8], [3.3, 3, 0.9], 'moss_mid'),
    ]))
    face_cubes = [
        box('tan_face_block', [-5, -2.8, -2.05], [10, 6.3, 1.0], 'face'),
        box('outer_right_moss', [-5.25, -2.5, -2.28], [1.9, 2.5, 1.3], 'moss_mid'),
        box('upper_right_moss', [-5, -4.2, -2.3], [1.8, 2.3, 1.25], 'moss_light'),
        box('inner_right_moss', [-3.2, -3.9, -2.2], [1.8, 2.6, 1.1], 'moss_dark'),
        box('middle_moss_block', [-1.4, -4.2, -2.32], [3.2, 2.9, 1.25], 'moss_light'),
        box('small_hanging_leaf', [-0.3, -1.3, -2.22], [0.7, 1.3, 0.7], 'moss_mid'),
        box('inner_left_moss', [1.8, -3.9, -2.18], [1.55, 2.5, 1.1], 'moss_mid'),
        box('outer_left_moss', [3.35, -4.0, -2.28], [1.9, 4, 1.25], 'moss_light'),
        box('right_square_eye', [-3.4, -0.05, -2.07], [1.6, 1.75, 0.025], 'eye'),
        box('left_square_eye', [1.5, -0.05, -2.07], [1.6, 1.75, 0.025], 'eye'),
    ]
    if baby:
        # Keep the same face language, with fewer independently protruding clumps.
        face_cubes = [c for c in face_cubes if c['name'] not in ('small_hanging_leaf','inner_left_moss')]
    part('head', 'body', [0,0,-3.5*k], scaled(face_cubes))
    part('lantern_stem', 'body', [0,-4.15*k,0], scaled([
        box('bottom_stem_cube', [-1,-2,-1], [2,2,2], 'stem'),
        box('middle_stem_cube', [-0.5,-3.8,-1], [2,2,2], 'moss_mid'),
        box('top_hook_cube', [1,-5.5,-1], [3,2,2], 'moss_light'),
        box('hanging_top_lantern', [2.5,-3.5,-1], [2,2.2,2], 'berry'),
    ]))
    for side,x in [('right',-7),('left',5)]:
        for end,z in [('front',-3.9),('hind',3.7)]:
            # Rear lamps nest at the back corners instead of protruding like a second ear.
            lamp_x = x if end == 'front' else (-5.2 if side == 'right' else 3.2)
            lamp_z = z if end == 'front' else 5.6
            part(f'{side}_{end}_berry','body',[lamp_x*k,-0.5*k,lamp_z*k],scaled([
                box('square_hanging_lantern',[0,0,-1],[2,3.5,2],'berry')]))
    leg_height = 2.1 if baby else 4
    for side,x in [('right',-3.2),('left',3.2)]:
        for end,z in [('front',-3.8),('hind',3.8)]:
            part(f'{side}_{end}_leg',None,[x*k,24-leg_height,z*k],[
                box('square_brown_leg',[-1.3*k,0,-1.3*k],[2.6*k,leg_height,2.6*k],'leg')])
    return parts


def faces(cube):
    x, y, z = cube['size']
    u, v = cube['uv']
    # Vanilla Cube's unfolded box UV, including flipped bottom rectangle.
    return dict(west=[u, v+z, z, y], north=[u+z, v+z, x, y],
                east=[u+z+x, v+z, z, y], south=[u+2*z+x, v+z, x, y],
                up=[u+z, v, x, z], down=[u+z+x, v, x, z])


def atlas(parts, baby):
    image = Image.new('RGBA', (SIZE*PIXELS, SIZE*PIXELS))
    glow = Image.new('RGBA', (SIZE*PIXELS, SIZE*PIXELS))
    x = y = row = 0
    used = {}
    moss = ['#4c6025', '#53682a', '#5b702d', '#617531', '#697d36']
    if baby:
        moss = ['#5c7230', '#6c8138', '#778b3e', '#829448', '#71863c']
    skin = ['#aa9758', '#b29d5d', '#ac9859', '#b8a361']
    leg = ['#6f602e', '#756532', '#5f5228', '#5a4f26']
    stem = ['#34481b', '#405520', '#4c6026']
    for part in parts:
        for cube in part['cubes']:
            key = (cube['material'], tuple(cube['size']))
            if cube['material'] == 'eye':
                cube['uv'] = [120,120]
                for py in range(120*PIXELS,128*PIXELS):
                    for px in range(120*PIXELS,128*PIXELS):
                        image.putpixel((px,py),(36,31,21,255))
                continue
            if key in used:
                cube['uv'] = used[key]
                continue
            w, h, d = cube['size']
            netw, neth = math.ceil(2*(w+d)), math.ceil(h+d)
            if x+netw > SIZE:
                x, y, row = 0, y+row+2, 0
            assert y+neth <= SIZE, 'Texture atlas overflow'
            cube['uv'] = [x, y]
            used[key] = cube['uv']
            x += netw+2
            row = max(row, neth)
            mat = cube['material']
            for direction, (u,v,fw,fh) in faces(cube).items():
                for py in range(math.ceil(fh*PIXELS)):
                    for px in range(math.ceil(fw*PIXELS)):
                        # Coarse contiguous patches instead of speckled random noise.
                        patch = int((px//3)*7 + (py//3)*3 + (px//2)%2 + u//3 + v//2) % 5
                        color = moss[patch]
                        if mat.startswith('moss_'):
                            base = {'moss_light': '#647b2e', 'moss_mid': '#526629', 'moss_dark': '#455721'}[mat]
                            rgb = bytes.fromhex(base[1:])
                            delta = [0,3,-2,4,1][patch] + (6 if baby else 0)
                            color = '#'+''.join(f'{max(0,min(255,q+delta)):02x}' for q in rgb)
                        elif mat == 'stem':
                            color = stem[patch % 3]
                        elif mat == 'leg':
                            color = leg[(0 if py < fh*PIXELS*0.5 else 2) + int(px >= fw*PIXELS*0.5)]
                        elif mat in ('face', 'shell'):
                            if mat == 'face':
                                color = skin[(int(px//(2*PIXELS))+int(py//(2*PIXELS))*2)%4]
                                if py/PIXELS < 1.5:
                                    color = ['#82713d','#8a7842','#927f47'][patch%3]
                            else:
                                top = direction == 'up'
                                boundary = 3.5 + (int(px//(2*PIXELS) + u) % 2)*0.5
                                if not top and (py/PIXELS >= boundary or direction == 'down'):
                                    color = ['#726337','#7d6c3c','#887443','#695d33'][patch%4]
                        elif mat == 'berry':
                            palette = ['#ffce44', '#fff78c', '#ff950b', '#ff6810']
                            if baby:
                                palette = ['#f3cb56', '#fff098', '#e9b437', '#b99132']
                            # A pale core framed by orange, rather than alternating stripes.
                            middle = 0.30 <= (py+0.5)/(fh*PIXELS) <= 0.68
                            index = (1 if 0 < px < math.ceil(fw*PIXELS)-1 else 0) if middle else (3 if px == 0 else 2)
                            color = palette[index]
                        image.putpixel((int(u*PIXELS+px), int(v*PIXELS+py)), tuple(bytes.fromhex(color[1:]))+(255,))
                        if mat == 'berry':
                            glow.putpixel((int(u*PIXELS+px), int(v*PIXELS+py)), image.getpixel((int(u*PIXELS+px), int(v*PIXELS+py))))
    suffix = '_baby' if baby else ''
    TEXTURES.mkdir(parents=True, exist_ok=True)
    image.save(TEXTURES / f'glowhopper{suffix}.png')
    glow.save(TEXTURES / f'glowhopper{suffix}_glow.png')
    return image


def java_layer(parts, baby):
    def f(n):
        return f'{float(n):.2f}F'
    method = 'createBabyLayer' if baby else 'createAdultLayer'
    lines = [f'    public static LayerDefinition {method}() {{',
             '        MeshDefinition mesh = new MeshDefinition();',
             '        PartDefinition root = mesh.getRoot();']
    for part in parts:
        parent = part['parent'] or 'root'
        name = part['name']
        lines += [f'        PartDefinition {name} = {parent}.addOrReplaceChild("{name}",',
                  '            CubeListBuilder.create()']
        for cube in part['cubes']:
            u, v = cube['uv']
            args = ', '.join(f(n) for n in cube['xyz'] + cube['size'])
            lines += [f'                .texOffs({u}, {v}).addBox({args}, CubeDeformation.NONE, 0.5F, 0.5F)']
        lines[-1] += ','
        pivot = ', '.join(f(n) for n in part['pivot'])
        lines += [f'            PartPose.offset({pivot}));']
    lines += [f'        return LayerDefinition.create(mesh, {SIZE*PIXELS}, {SIZE*PIXELS});', '    }']
    return '\n'.join(lines)


def stable_id(name):
    return str(uuid.uuid5(uuid.NAMESPACE_URL, 'wildwoven/glowhopper/'+name))


def blockbench(parts, image, baby):
    positions = {}
    groups = {}
    elements = []
    def bb(v):
        return [v[0], 24-v[1], -v[2]]
    for part in parts:
        name = part['name']
        parent_origin = positions.get(part['parent'], [0,0,0])
        origin = [a+b for a,b in zip(parent_origin, part['pivot'])]
        positions[name] = origin
        group = dict(name=name, uuid=stable_id(name), origin=bb(origin), rotation=[0,0,0],
                     export=True, isOpen=True, children=[])
        groups[name] = group
        for i,cube in enumerate(part['cubes']):
            lo = [a+b for a,b in zip(origin, cube['xyz'])]
            hi = [a+b for a,b in zip(lo, cube['size'])]
            a, b = bb(lo), bb(hi)
            uid = stable_id(name+'/'+str(i))
            cube_faces = {}
            # z is reflected for Blockbench's positive-y editor convention.
            for direction, (u,v,w,h) in faces(cube).items():
                dest = dict(north='south', south='north').get(direction, direction)
                uv = [u,v,u+w,v+h]
                if direction == 'down':
                    uv = [u+w,v+h,u,v]
                cube_faces[dest] = dict(uv=[q*PIXELS for q in uv], texture=0)
            elements.append(dict(name=cube['name'], uuid=uid, type='cube',
                                 **{'from':[min(q,r) for q,r in zip(a,b)],
                                    'to':[max(q,r) for q,r in zip(a,b)]},
                                 origin=bb(origin), rotation=[0,0,0], rescale=False,
                                 autouv=0, box_uv=False, uv_offset=cube['uv'], faces=cube_faces))
            group['children'].append(uid)
    outliner = []
    for part in parts:
        (groups[part['parent']]['children'] if part['parent'] else outliner).append(groups[part['name']])
    buffer = io.BytesIO()
    image.save(buffer, format='PNG')
    animations = []
    for clip in ('walk', 'rest', 'head_carry', 'jump', 'eat'):
        animators = {}
        for part in parts:
            name = part['name']
            frames = []
            if clip == 'walk' and name.endswith('_leg'):
                opposite = name in ('left_front_leg', 'right_hind_leg')
                for step in range(9):
                    t = step/8
                    phase = t*2*math.pi + (math.pi if opposite else 0)
                    angle = math.cos(phase)*math.degrees(0.48 if baby else 0.55)
                    frames.append(dict(channel='rotation', time=t, interpolation='linear',
                                       data_points=[dict(x=str(angle), y='0', z='0')], uuid=str(uuid.uuid4())))
                    angle_rad=math.radians(angle)
                    clearance=max(0,(2.1 if baby else 4)*(math.cos(angle_rad)-1)+(0.91 if baby else 1.3)*abs(math.sin(angle_rad)))
                    lift = max(0, math.sin(phase))*(0.35 if baby else 0.55)+clearance
                    frames.append(dict(channel='position', time=t, interpolation='linear',
                                       data_points=[dict(x='0',y=str(lift),z='0')], uuid=str(uuid.uuid4())))
            elif clip == 'walk' and name == 'body':
                for step in range(9):
                    t = step/8
                    phase = t*2*math.pi
                    frames.append(dict(channel='position', time=t, interpolation='linear',
                                       data_points=[dict(x='0',y=str((1-math.cos(phase*2))*0.09),z='0')],uuid=str(uuid.uuid4())))
                    frames.append(dict(channel='rotation', time=t, interpolation='linear',
                                       data_points=[dict(x='0',y='0',z=str(-math.degrees(math.sin(phase)*0.025)))],uuid=str(uuid.uuid4())))
            elif clip in ('rest', 'head_carry'):
                carried = clip == 'head_carry'
                if name == 'body':
                    drop = (1.4 if carried else 1.0) if baby else (3.0 if carried else 2.6)
                    frames.append(dict(channel='position',time=0,interpolation='linear',
                                       data_points=[dict(x='0',y=str(-drop),z='0')],uuid=str(uuid.uuid4())))
                elif name.endswith('_leg'):
                    spread = 1.50 if carried else 1.45
                    zangle = -math.degrees(spread) if name.startswith('right') else math.degrees(spread)
                    xangle = math.degrees(-0.08 if '_front_' in name else 0.08)
                    frames.append(dict(channel='rotation',time=0,interpolation='linear',
                                       data_points=[dict(x=str(xangle),y='0',z=str(zangle))],uuid=str(uuid.uuid4())))
                    half_width=half_depth=0.91 if baby else 1.3
                    leg_y = 24-(2.1 if baby else 4)*math.cos(spread)*math.cos(0.08)-half_width*math.sin(spread)-half_depth*math.cos(spread)*math.sin(0.08)
                    frames.append(dict(channel='position',time=0,interpolation='linear',
                                       data_points=[dict(x='0',y=str(part['pivot'][1]-leg_y),z='0')],uuid=str(uuid.uuid4())))
            elif clip == 'jump' and name.endswith('_leg'):
                angle = math.degrees(-0.55 if '_front_' in name else 0.48)
                for t, a in [(0,0), (0.2,angle), (0.6,angle), (0.8,0)]:
                    frames.append(dict(channel='rotation',time=t,interpolation='linear',
                                       data_points=[dict(x=str(a),y='0',z='0')],uuid=str(uuid.uuid4())))
            elif clip == 'eat' and name == 'head':
                for step in range(5):
                    frames.append(dict(channel='rotation',time=step*0.125,interpolation='linear',
                                       data_points=[dict(x=str(math.degrees(0.12+(0.05 if step%2 else -0.05))),y='0',z='0')],uuid=str(uuid.uuid4())))
            if frames:
                for frame in frames:
                    frame['uuid']=stable_id(f'{baby}/{clip}/{name}/{frame["channel"]}/{frame["time"]}')
                animators[stable_id(part['name'])] = dict(name=name,type='bone',keyframes=frames)
        animations.append(dict(uuid=stable_id(clip),name=clip,loop='loop' if clip in ('walk','eat') else 'hold',
                               length=1 if clip=='walk' else 0.8 if clip=='jump' else 0.5,
                               snapping=20,animators=animators))
    result = dict(meta=dict(format_version='4.10', model_format='free', box_uv=False),
                  name='glowhopper_baby' if baby else 'glowhopper',
                  resolution=dict(width=SIZE*PIXELS,height=SIZE*PIXELS), elements=elements, outliner=outliner,
                  textures=[dict(name='glowhopper_baby.png' if baby else 'glowhopper.png',
                                 uuid=stable_id('texture'),id='0',width=SIZE*PIXELS,height=SIZE*PIXELS,
                                 uv_width=SIZE*PIXELS,uv_height=SIZE*PIXELS,render_mode='default',
                                 source='data:image/png;base64,'+base64.b64encode(buffer.getvalue()).decode())],
                  animations=animations)
    (HERE / ('glowhopper_baby.bbmodel' if baby else 'glowhopper.bbmodel')).write_text(
        json.dumps(result, ensure_ascii=False, indent=2), encoding='utf-8')


def main():
    layers = []
    for baby in (False, True):
        parts = mesh(baby)
        image = atlas(parts, baby)
        layers.append(java_layer(parts, baby))
        blockbench(parts, image, baby)
        (HERE / ('baby_mesh.json' if baby else 'adult_mesh.json')).write_text(json.dumps(parts, indent=2), encoding='utf-8')
    text = JAVA.read_text(encoding='utf-8-sig')
    text = re.sub(r'    // BEGIN GENERATED GEOMETRY.*?    // END GENERATED GEOMETRY',
                  '    // BEGIN GENERATED GEOMETRY -- art/creatures/glowhopper/build_assets.py\n'
                  + '\n\n'.join(layers) + '\n    // END GENERATED GEOMETRY', text, flags=re.S)
    JAVA.write_text(text, encoding='utf-8')
    print('Generated adult + baby geometry, four atlases, and two editable Blockbench projects.')


if __name__ == '__main__':
    main()
