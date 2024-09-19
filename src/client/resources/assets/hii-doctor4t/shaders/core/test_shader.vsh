#version 150

#moj_import <projection.glsl>

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out mat4 modelViewMat;
out mat4 projMat;
out vec4 position;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    modelViewMat = ModelViewMat;
    projMat = ProjMat;

    position = vec4(Position, 1.0);
}
