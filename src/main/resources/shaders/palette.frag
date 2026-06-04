// palette.frag
// Reduces the image to a limited colour palette by quantising each channel,
// then optionally applies a Bayer 4x4 ordered dither to smooth the transitions.
//
// Uniforms:
//   levels      — number of colour steps per channel (e.g. 4.0 = 4-bit look)
//   ditherScale — strength of the dither pattern (0.0 = none, 1.0 = full)
//   resolution  — screen/texture size in pixels (needed for dither coordinates)

uniform sampler2D texture;
uniform float levels;
uniform float ditherScale;
uniform vec2 resolution;

// 4x4 Bayer ordered dither matrix, normalised to [0,1)
// Indexed as bayer[x + y*4] where x,y are in [0,3].
// Uses float arithmetic (mod) to stay compatible with GLSL 1.20 (no bitwise ops).
float bayerLookup(float idx) {
    if (idx <  1.0) return  0.0 / 16.0;
    if (idx <  2.0) return  8.0 / 16.0;
    if (idx <  3.0) return  2.0 / 16.0;
    if (idx <  4.0) return 10.0 / 16.0;
    if (idx <  5.0) return 12.0 / 16.0;
    if (idx <  6.0) return  4.0 / 16.0;
    if (idx <  7.0) return 14.0 / 16.0;
    if (idx <  8.0) return  6.0 / 16.0;
    if (idx <  9.0) return  3.0 / 16.0;
    if (idx < 10.0) return 11.0 / 16.0;
    if (idx < 11.0) return  1.0 / 16.0;
    if (idx < 12.0) return  9.0 / 16.0;
    if (idx < 13.0) return 15.0 / 16.0;
    if (idx < 14.0) return  7.0 / 16.0;
    if (idx < 15.0) return 13.0 / 16.0;
    return 5.0 / 16.0;
}

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec4 col = texture2D(texture, uv) * gl_Color;

    // Dither threshold for this pixel
    float px = floor(mod(uv.x * resolution.x, 4.0));
    float py = floor(mod(uv.y * resolution.y, 4.0));
    float threshold = (bayerLookup(px + py * 4.0) - 0.5) * ditherScale / levels;

    // Quantise each channel
    col.r = floor((col.r + threshold) * levels + 0.5) / levels;
    col.g = floor((col.g + threshold) * levels + 0.5) / levels;
    col.b = floor((col.b + threshold) * levels + 0.5) / levels;

    gl_FragColor = vec4(clamp(col.rgb, 0.0, 1.0), col.a);
}
