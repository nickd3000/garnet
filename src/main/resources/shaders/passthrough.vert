// Compatibility vertex shader for Garnet's buffered renderer.
// It adapts batch attributes to the legacy fragment shader inputs.
attribute vec2 a_position;
attribute vec2 a_texCoord;
attribute vec4 a_color;

uniform vec2 u_screenSize;

void main() {
    vec2 ndc = vec2(
        (a_position.x / u_screenSize.x) * 2.0 - 1.0,
        1.0 - (a_position.y / u_screenSize.y) * 2.0
    );

    gl_Position = vec4(ndc, 0.0, 1.0);
    gl_TexCoord[0] = vec4(a_texCoord, 0.0, 1.0);
    gl_FrontColor = a_color;
}
