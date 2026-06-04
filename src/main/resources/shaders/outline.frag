// outline.frag
// Draws a solid colour outline around the opaque silhouette of a sprite.
// Samples the 4 cardinal neighbours; if any neighbour is transparent while
// the current pixel is also transparent, the pixel becomes the outline colour.
//
// Uniforms:
//   texelSize   — vec2(1/textureWidth, 1/textureHeight)
//   outlineColor — vec4 RGBA colour of the outline (default: red)

uniform sampler2D texture;
uniform vec2 texelSize;
uniform vec4 outlineColor;

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec4 col = texture2D(texture, uv);

    if (col.a > 0.1) {
        // Opaque pixel — render normally
        gl_FragColor = col * gl_Color;
    } else {
        // Transparent pixel — check if any neighbour is opaque
        float n = texture2D(texture, uv + vec2(0.0,  texelSize.y)).a;
        float s = texture2D(texture, uv + vec2(0.0, -texelSize.y)).a;
        float e = texture2D(texture, uv + vec2( texelSize.x, 0.0)).a;
        float w = texture2D(texture, uv + vec2(-texelSize.x, 0.0)).a;

        if (max(max(n, s), max(e, w)) > 0.1) {
            gl_FragColor = outlineColor;
        } else {
            gl_FragColor = vec4(0.0);
        }
    }
}
