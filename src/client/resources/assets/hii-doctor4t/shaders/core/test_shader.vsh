#version 150

#moj_import <projection.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out mat4 modelViewMat;
out mat4 projMat;
out vec3 blockPosition;

void main()
{
    vec4 viewPos = ModelViewMat * vec4(Position, 1.0);
    vec4 clipPos = ProjMat * viewPos;

    gl_Position = clipPos;

    modelViewMat = ModelViewMat;
    projMat = ProjMat;
    blockPosition = Position;
}
