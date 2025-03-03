#version 150

#moj_import <matrix.glsl>
#moj_import <hii-doctor4t:useful.glsl>

uniform float GameTime;
uniform vec2 ScreenSize;
uniform vec3 CameraPos;
uniform vec3 CameraRot;
uniform float CameraFov;

uniform sampler2D TestTex;
uniform sampler2D EarthTex;

vec3 worldOrigin = vec3(0.5, 72.0, 0.5);
const float near = 0.01;
const float far = 1000.0;

in mat4 modelViewMat;
in mat4 projMat;
in vec3 blockPosition;

out vec4 fragColor;

float gridTextureGradBox(in vec2 p, in vec2 ddx, in vec2 ddy)
{
    const float N = 10.0;
    vec2 w = max(abs(ddx), abs(ddy)) + 0.01;
    vec2 a = p + 0.5*w;
    vec2 b = p - 0.5*w;
    vec2 i = (floor(a)+min(fract(a)*N,1.0)-
    floor(b)-min(fract(b)*N,1.0))/(N*w);
    return (1.0-i.x)*(1.0-i.y);
}

struct Sphere
{
    vec3 position;
    float radius;
    vec3 normal;
    vec3 color;
};

struct LightSource
{
    vec3 position;
    float radius;
    vec3 color;
    float intensity;
};

Sphere moveSphere(in Sphere sph, in vec3 off)
{
    Sphere sphn = sph;
    sphn.position += off;
    return sphn;
}

LightSource sphereAsLightSource(in Sphere sph)
{
    LightSource outLight = LightSource(sph.position, sph.radius, sph.color, 1.0);
    return outLight;
}

LightSource moveLightSource(in LightSource lig, in vec3 off)
{
    LightSource lign = lig;
    lign.position += off;
    return lign;
}

float iSphere(in vec3 rd, in Sphere sph)
{
    vec3 oc = -sph.position;
    float b = dot(oc, rd);
    float c = dot(oc, oc) - sph.radius*sph.radius;
    float h = b*b - c;
    if(h<0.0) return -1.0;
    if (c<0.0) return -(-b - sqrt(h));
    return -b - sqrt(h);
}

float iLightSource(in vec3 rd, in LightSource lig)
{
    vec3 oc = -lig.position;
    float b = dot(oc, rd);
    float c = dot(oc, oc) - lig.radius*lig.radius;
    float h = (b*b - c);
    if(h<0.0) return -1.0;
    if (c<0.0) return -(-b - sqrt(h));
    return (-b - sqrt(h));
}

float ssSphere(in vec3 ro, in vec3 rd, in Sphere sph)
{
    vec3 oc = sph.position - ro;
    float b = dot(oc, rd);

    float res = 1.0;
    if (b > 0.0)
    {
        float h = dot(oc,oc) - b*b - sph.radius*sph.radius;
        res = smoothstep(0.0, 1.0, 12.0*h/b);
    }
    return res;
}

void main()
{
    vec2 uv = ((gl_FragCoord.xy - vec2((ScreenSize.x-ScreenSize.y)/2.0,0.0)) / min(ScreenSize.x,ScreenSize.y)) * 2.0 - 1.0;

    vec2 p = (-ScreenSize.xy + 2.0*gl_FragCoord.xy) / ScreenSize.y;

    float fov = 100.0/CameraFov;
    float fog = 24.0;
    float fogFalloff = 2.0;

    vec3 ro = vec3(3.0*CameraRot.x, 3.0*-CameraRot.y, 3.0*CameraRot.z);
    vec3 ta = vec3(0.0, 0.0, 0.0);
    vec3 ww = normalize(ta - ro);
    vec3 uu = normalize(cross(ww,vec3(0.0,1.0,0.0)));
    vec3 vv = normalize(cross(uu,ww));
    vec3 rd = normalize(p.x*uu + p.y*vv + fov*ww);
    mat4 cam = mat4(
    uu.x, uu.y, uu.z, 0.0,
    vv.x, vv.y, vv.z, 0.0,
    ww.x, ww.y, ww.z, 0.0,
    -dot(uu,ro), -dot(vv,ro), -dot(ww,ro), 1.0);

    vec3 offset = (vec3(-(CameraPos-worldOrigin).x,(CameraPos-worldOrigin).y,-(CameraPos-worldOrigin).z))*0.85;

    Sphere spheres[3] = Sphere[3](
    Sphere(vec3(-2.0, 1.0, 8.0), 1.1, vec3(0.0), vec3(1.0)),
    Sphere(vec3(3.0, 1.5, 1.0), 1.2, vec3(0.0), vec3(1.0)),
    Sphere(vec3(1.0, -1.0, 1.0), 1.3, vec3(0.0), vec3(1.0)));

    LightSource lightSources[2] = LightSource[2](
    LightSource(vec3(2.0, 1.4, -1.0), 1.0, vec3(1.0, 0.75, 0.25), 1.0),
    LightSource(vec3(-40000.0, 24000.0, 0.0), 1000.0, vec3(1.0, 1.0, 1.0), 1.0));

    float objectDepths[spheres.length()+lightSources.length()];

    float tmin = 100000.0;
    float objectTmin = 10000000.0;
    float lightSourceTmin = 100000.0;
    vec3 nor = vec3(0.0);
    vec3 pos = vec3(0.0);

    vec3 sur = vec3(1.0);
    vec3 sphereCol = vec3(0.0);
    vec3 lightMask = vec3(0.0);
    vec3 sphereBounds = vec3(0.0);
    vec3 lightSourceCol = vec3(0.0);

    vec3 debugCol = vec3(0.0);

    float depthMap = 0.0;
    float maxViewDistance = 500000.0;

    vec3 lightDistribution = vec3(0.0);
    float globalLightDistanceMul = 8.0;

    float h = (-(CameraPos-worldOrigin).y-2.0)/rd.y;
    if(h>0.0 && h<tmin)
    {
        tmin = h;
        pos = h*rd + offset;
        nor = vec3(0.0,1.0,0.0);
        sur = vec3(1.0)*gridTextureGradBox(pos.xz, dFdx(pos.xz), dFdy(pos.xz));
    }

    vec3 debugGrid = min(1.0-sur,0.005)*(1.0-min(pow(length((pos.xz+(CameraPos-worldOrigin).xz)/fog),1.0/fogFalloff),1.0));

    vec3 col = vec3(0.0);
    vec3 lightPosition = lightSources[0].position;

    for(int i=0; i<spheres.length(); i++)
    {
        float surface = (length(rd+normalize(offset-spheres[i].position)))*distance(offset,spheres[i].position);
        surface = 1.0-surface;
        surface = max(0.0,min(surface,1.0));

        float d = (maxViewDistance-distance(offset,spheres[i].position))/maxViewDistance;
        objectDepths[i] = d;

        float h = iSphere(rd, moveSphere(spheres[i], -offset));
        if(h>0.0 && h<objectTmin)
        {
            objectTmin = h;
            pos = h*rd + offset;
            spheres[i].normal = normalize(pos-spheres[i].position);
            sphereCol = spheres[i].color;
            lightMask = vec3(1.0);
            sphereBounds = vec3(1.0);

            if (d > depthMap) depthMap = d;

            vec2 mappingNormal = normalToUV(spheres[i].normal);
            sphereCol *= texture(TestTex, mappingNormal, 1.0).rgb;
            sphereCol *= exp(-0.25*(max(0.0,objectTmin-3.0)));
            //sphereCol = spheres[i].normal;

            vec3 lig = lightPosition-spheres[i].position;
            float ndl = clamp(dot(spheres[i].normal,normalize(lig)), 0.0, 1.0);
            float antiNdl = clamp(dot(spheres[i].normal,normalize(lig))+0.05, 0.0, 1.0);

            float lightDistance = pow(distance(spheres[i].position,lightPosition),1.0/(lightSources[0].radius*globalLightDistanceMul));

            sphereCol *= vec3(1.0)*ndl/lightDistance;
            lightMask *= vec3(1.0)*antiNdl/lightDistance;
        }
    }

    lightDistribution = ((1.0-sphereBounds)+(0.125+vec3(pow(lightMask.x, 1.0))));

    for(int i=0; i<lightSources.length(); i++)
    {
        float falloff = 16.0*lightSources[i].intensity;
        float bloom = (length(rd+normalize(offset-lightSources[i].position)))*distance(offset,lightSources[i].position)-sqrt(lightSources[i].radius);
        bloom /= lightSources[i].radius;
        bloom /= falloff*2.0;
        bloom = 1.0-bloom;
        bloom = max(0.0,min(bloom,1.0));
        bloom = pow(bloom, falloff);

        float d = (maxViewDistance-distance(offset,lightSources[i].position))/maxViewDistance;
        objectDepths[spheres.length()+1+i] = d;

        float h = iLightSource(rd, moveLightSource(lightSources[i], -offset));
        if (h>0.0 && h<objectTmin)
        {
            bloom = 1.0;

            objectTmin = h;
            pos = h*rd + offset;

            lightDistribution = vec3(2.0);
            //lightSourceCol = vec3(1.0)*lightSources[i].color;

            debugGrid = d >= depthMap ? vec3(0.0) : debugGrid;
            sphereCol = d >= depthMap ? vec3(0.0) : sphereCol;
        }

        if (d*bloom > depthMap) depthMap = d*bloom;

        if (i == 0)
        {
            float depthBloom = clamp(depthMap,0.0,1.0)*bloom*d;
            debugGrid *= (1.0-depthBloom);
            sphereCol *= (1.0-depthBloom);
            lightSourceCol += vec3(1.0)*depthBloom*lightSources[i].color;
        }
        //lightSourceCol += vec3(depthBloom);

        //debugCol += clamp(depthMap,0.0,1.0);
    }

    for(int i=0; i<spheres.length(); i++)
    {
        vec3 lig = lightPosition-spheres[i].position;
        float sha = 1.0;
        sha *= ssSphere(pos, normalize(lig), spheres[i]);

        float lightDistance = pow(distance(spheres[i].position,lightPosition),1.0/(lightSources[0].radius*globalLightDistanceMul));

        sphereCol *= sha*vec3(1.0)/lightDistance;
    }

    debugGrid *= (1.0-sphereBounds);
    col += debugGrid;

    col += sphereCol;

    //lightSourceCol = 1.0 - exp(-lightSourceCol);
    lightSourceCol *= lightDistribution;
    col += lightSourceCol;

    col = debugCol == vec3(0.0) ? col : debugCol;

    col = pow(col, vec3(0.45));

    fragColor = vec4(col, 1.0);
}