"""Orthographic QA renders of the generated runtime cuboids and their real pixel atlas."""
import copy
import json
import math
from pathlib import Path
import numpy as np
from PIL import Image, ImageDraw, ImageFont
from build_assets import HERE, TEXTURES, faces, PIXELS


def rotation(x=0,y=0,z=0):
    cx,sx,cy,sy,cz,sz = math.cos(x),math.sin(x),math.cos(y),math.sin(y),math.cos(z),math.sin(z)
    return np.array([[cz,-sz,0],[sz,cz,0],[0,0,1]]) @ np.array([[cy,0,sy],[0,1,0],[-sy,0,cy]]) @ np.array([[1,0,0],[0,cx,-sx],[0,sx,cx]])


def render(baby=False, view=(0.65,-0.35,-1.6), pose='idle', phase=0, size=(320,280), scale=14,
           head_rotation=(0,0,0), model_yaw=0, body_pitch=0):
    parts = json.loads((HERE / ('baby_mesh.json' if baby else 'adult_mesh.json')).read_text())
    texture = Image.open(TEXTURES / ('glowhopper_baby.png' if baby else 'glowhopper.png')).convert('RGB')
    eye = np.array(view, dtype=float)
    eye /= np.linalg.norm(eye)
    right = np.cross([0,-1,0],eye); right /= np.linalg.norm(right)
    up = np.cross(eye,right)
    center = np.array([0,16,0])
    def project(v):
        v = v-center
        return (size[0]/2 + v @ right*scale, size[1]*0.48 - v @ up*scale)
    transforms = {}
    pixels = []
    for part in parts:
        name=part['name']
        pivot = np.array(part['pivot'],dtype=float)
        rot = np.eye(3)
        if name=='head':
            rot=rotation(*head_rotation)
        if name=='body':
            rot=rotation(x=body_pitch)
        if name=='body' and pose=='walk':
            pivot[1] -= (1-math.cos(phase*2))*0.09
            rot=rotation(x=body_pitch,z=math.sin(phase)*0.025)
        if name=='body' and pose=='eat':
            rot=rotation(x=body_pitch+0.06+math.sin(phase*1.8)*0.03)
        if pose in ('rest','carry'):
            carried=pose=='carry'
            if name=='body':
                pivot[1] += (1.4 if carried else 1.0) if baby else (3.0 if carried else 2.6)
            if name.endswith('_leg'):
                spread=1.50 if carried else 1.45
                rot=rotation(x=-0.08 if '_front_' in name else 0.08,
                             z=spread if name.startswith('right') else -spread)
                half_width=half_depth=0.91 if baby else 1.3
                pivot[1]=24-(2.1 if baby else 4)*math.cos(spread)*math.cos(0.08)-half_width*math.sin(spread)-half_depth*math.cos(spread)*math.sin(0.08)
        if name.endswith('_leg') and pose=='walk':
            opposite=name in ('left_front_leg','right_hind_leg')
            p=phase+(math.pi if opposite else 0)
            rot=rotation(x=math.cos(p)*(0.48 if baby else 0.55))
            swing=math.cos(p)*(0.48 if baby else 0.55)
            pivot[1]-=max(0,(2.1 if baby else 4)*(math.cos(swing)-1)+(0.91 if baby else 1.3)*abs(math.sin(swing)))
            pivot[1]-=max(0, math.sin(p))*(0.35 if baby else 0.55)
        if pose=='jump':
            if name=='body':
                rot=rotation(x=-0.1)
            if name.endswith('_leg'):
                rot=rotation(x=-0.55 if '_front_' in name else 0.48)
        if part['parent']:
            pr,pt=transforms[part['parent']]
            transforms[name]=(pr@rot, pt+pr@pivot)
        else:
            global_rotation=rotation(y=model_yaw)
            transforms[name]=(global_rotation@rot,global_rotation@pivot)
        r,t=transforms[name]
        for cube in part['cubes']:
            x,y,z=cube['xyz']; w,h,d=cube['size']
            planes={
                'north':([x,y,z],[1,0,0],[0,1,0],[0,0,-1]),
                'south':([x+w,y,z+d],[-1,0,0],[0,1,0],[0,0,1]),
                'west':([x,y,z+d],[0,0,-1],[0,1,0],[-1,0,0]),
                'east':([x+w,y,z],[0,0,1],[0,1,0],[1,0,0]),
                'up':([x,y,z],[1,0,0],[0,0,1],[0,-1,0]),
                'down':([x,y+h,z+d],[1,0,0],[0,0,-1],[0,1,0])}
            for face,(u,v,fw,fh) in faces(cube).items():
                origin,a,b,n = map(np.array,planes[face])
                if (r@n)@eye <=0:
                    continue
                normal=r@n
                light=np.array([-0.4,-1,-0.5]); light/=np.linalg.norm(light)
                shade=0.68+0.32*max(0,normal@light)
                if cube['material']=='berry':
                    shade=1
                for py in range(math.ceil(fh*PIXELS)):
                    for px in range(math.ceil(fw*PIXELS)):
                        verts=[r@(origin+a*min(fw,(px+dx)/PIXELS)+b*min(fh,(py+dy)/PIXELS))+t for dx,dy in [(0,0),(1,0),(1,1),(0,1)]]
                        color=tuple(int(k*shade) for k in texture.getpixel((int(u*PIXELS+px),int(v*PIXELS+py))))
                        depth=np.mean(verts,axis=0)@eye
                        pixels.append((depth,[(*project(q),q@eye) for q in verts],color))
    result=Image.new('RGB',size,'#e9e5dc')
    draw=ImageDraw.Draw(result)
    ground=project(np.array([0,24,0]))
    draw.ellipse((ground[0]-scale*5,ground[1]-scale*1.2,ground[0]+scale*5,ground[1]+scale*1.2),fill='#ccc8bd')
    canvas=np.array(result)
    depth_buffer=np.full((size[1],size[0]),-np.inf)
    for _,verts,color in pixels:
        for indices in ((0,1,2),(0,2,3)):
            tri=np.array([verts[k] for k in indices])
            xmin=max(0,int(np.floor(tri[:,0].min()))); xmax=min(size[0]-1,int(np.ceil(tri[:,0].max())))
            ymin=max(0,int(np.floor(tri[:,1].min()))); ymax=min(size[1]-1,int(np.ceil(tri[:,1].max())))
            if xmin>xmax or ymin>ymax: continue
            a,b,c=tri
            denom=(b[1]-c[1])*(a[0]-c[0])+(c[0]-b[0])*(a[1]-c[1])
            if abs(denom)<1e-8: continue
            yy,xx=np.mgrid[ymin:ymax+1,xmin:xmax+1]; xx=xx+0.5; yy=yy+0.5
            wa=((b[1]-c[1])*(xx-c[0])+(c[0]-b[0])*(yy-c[1]))/denom
            wb=((c[1]-a[1])*(xx-c[0])+(a[0]-c[0])*(yy-c[1]))/denom
            wc=1-wa-wb
            depth=wa*a[2]+wb*b[2]+wc*c[2]
            tile=depth_buffer[ymin:ymax+1,xmin:xmax+1]
            mask=(wa>=-1e-6)&(wb>=-1e-6)&(wc>=-1e-6)&(depth>tile)
            tile[mask]=depth[mask]
            canvas[ymin:ymax+1,xmin:xmax+1][mask]=color
    return Image.fromarray(canvas)


def main():
    dest=HERE / 'previews'; dest.mkdir(exist_ok=True)
    render(view=(0.23,-0.06,-1),size=(828,828),scale=45).save(dest/'adult_detail.png')
    font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',20)
    small=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',16)
    sheet=Image.new('RGB',(1280,950),'#e9e5dc')
    d=ImageDraw.Draw(sheet)
    d.text((24,14),'灯莓兽 · 实际模型检查（成人 / 幼体）',font=font,fill='#292a21')
    views=[((0,0,-1),'正面'),((1,0,0),'侧面'),((0,0,1),'背面'),((0.65,-0.35,-1.6),'透视')]
    for row,baby in enumerate((False,True)):
        for col,(view,label) in enumerate(views):
            img=render(baby,view,size=(320,270),scale=14)
            sheet.paste(img,(col*320,60+row*300))
            d.text((col*320+20,315+row*300),('幼体' if baby else '成人')+' · '+label,font=small,fill='#3d4030')
    for col,(pose,label) in enumerate([('rest','成人 · 趴卧'),('carry','成人 · 头顶趴姿'),('walk','成人 · 行走'),('jump','成人 · 跳跃')]):
        sheet.paste(render(False,pose=pose,phase=0.8,size=(320,260),scale=13),(col*320,655))
        d.text((col*320+20,916),label,font=small,fill='#3d4030')
    sheet.save(dest/'model_sheet.png')
    frames=[]
    for i in range(32):
        frame=Image.new('RGB',(760,340),'#e9e5dc')
        for col,baby in enumerate((False,True)):
            frame.paste(render(baby,pose='walk',phase=i/32*2*math.pi,size=(380,300),scale=16),(col*380,25))
        draw=ImageDraw.Draw(frame)
        draw.text((20,8),'成人 · 行走',font=font,fill='#292a21')
        draw.text((400,8),'幼体 · 行走',font=font,fill='#292a21')
        frames.append(frame)
    frames[0].save(dest/'walk.gif',save_all=True,append_images=frames[1:],duration=40,loop=0)
    print('Rendered model_sheet.png and walk.gif from runtime geometry and textures.')


if __name__=='__main__':
    main()
