#version 150

mat4 perspective(float fov, float aspect, float near, float far) {
    float tanHalfFov = tan(radians(fov) / 2.0);
    mat4 result = mat4(0.0);
    result[0][0] = 1.0 / (aspect * tanHalfFov);
    result[1][1] = 1.0 / tanHalfFov;
    result[2][2] = -(far + near) / (far - near);
    result[2][3] = -1.0;
    result[3][2] = -(2.0 * far * near) / (far - near);
    return result;
}
mat4 lookAt(vec3 eye, vec3 center, vec3 up) {
    vec3 f = normalize(center - eye);
    vec3 s = normalize(cross(f, up));
    vec3 u = cross(s, f);

    mat4 result = mat4(1.0);
    result[0][0] = s.x;
    result[1][0] = s.y;
    result[2][0] = s.z;
    result[0][1] = u.x;
    result[1][1] = u.y;
    result[2][1] = u.z;
    result[0][2] = -f.x;
    result[1][2] = -f.y;
    result[2][2] = -f.z;
    result[3][0] = -dot(s, eye);
    result[3][1] = -dot(u, eye);
    result[3][2] = dot(f, eye);
    return result;
}

vec2 normalToUV(vec3 normal)
{
    float u = 0.5 + atan(normal.z, normal.x) / (2.0 * 3.14159265358979323846);
    float v = 0.5 - asin(normal.y) / 3.14159265358979323846;
    return vec2(u, v);
}