#version 150

#moj_import <matrix.glsl>

uniform float GameTime;

in mat4 modelViewMat;
in mat4 projMat;
in vec4 position;

out vec4 fragColor;

void main()
{
    vec2 uv = (gl_FragCoord.xy-vec2(440.0,0.0))/1080.0;

    vec3 lightDirection = normalize(vec3(1.0,1.0,1.0));

    vec3 worldCoord = (vec4(gl_FragCoord.xyz,1.0) * modelViewMat).xyz;

    vec3 col = vec3(uv.x,uv.y,0.0);
    vec3 origin = vec3(0.5,0.5,0.0);
    float radius = 0.25;
    if (
    distance(origin, vec3(uv,0.0)) <= radius)
    {
        col = vec3(1.0);
    }
    col = normalize(worldCoord);

    fragColor = vec4(col,1.0);
}
