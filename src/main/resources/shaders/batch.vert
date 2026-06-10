// Default buffered sprite/shape batch vertex shader.
// CPU-side vertices are already transformed to top-left-origin screen pixels.
attribute vec2 a_position;
attribute vec2 a_texCoord;
attribute vec4 a_color;
attribute float a_materialFlags;

uniform vec2 u_screenSize;

varying vec2 v_texCoord;
varying vec4 v_color;
varying float v_materialFlags;

void main() {
    vec2 ndc = vec2(
        (a_position.x / u_screenSize.x) * 2.0 - 1.0,
        1.0 - (a_position.y / u_screenSize.y) * 2.0
    );

    gl_Position = vec4(ndc, 0.0, 1.0);
    v_texCoord = a_texCoord;
    v_color = a_color;
    v_materialFlags = a_materialFlags;
}
