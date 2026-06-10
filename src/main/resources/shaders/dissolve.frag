// dissolve.frag
// Makes a sprite dissolve away pixel-by-pixel using a pseudo-random noise pattern.
//
// Uniforms:
//   threshold — how much of the sprite is dissolved (0.0 = fully visible, 1.0 = fully gone)
//   edgeWidth — width of the glowing burn edge as a fraction of the dissolve range (e.g. 0.05)
//   edgeColor — colour of the burn edge (RGBA)

uniform sampler2D texture;
uniform vec2 regionOffset;
uniform vec2 regionScale;
uniform float threshold;
uniform float edgeWidth;
uniform vec4 edgeColor;

// Simple hash-based noise in [0,1) from a 2D coordinate
float noise(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

// Smooth noise by bilinear interpolation of 4 hash samples
float smoothNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    float a = noise(i);
    float b = noise(i + vec2(1.0, 0.0));
    float c = noise(i + vec2(0.0, 1.0));
    float d = noise(i + vec2(1.0, 1.0));
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec2 localUv = (uv - regionOffset) / regionScale;
    vec4 col = texture2D(texture, uv) * gl_Color;

    // Multi-octave noise for a more organic dissolve pattern
    float n = smoothNoise(localUv * 8.0) * 0.5
            + smoothNoise(localUv * 16.0) * 0.3
            + smoothNoise(localUv * 32.0) * 0.2;

    // Discard pixels below the threshold
    if (n < threshold) discard;

    // Burn edge: pixels just above the threshold glow with edgeColor
    float edgeTop = threshold + edgeWidth;
    if (n < edgeTop) {
        float t = (n - threshold) / edgeWidth;
        col = mix(edgeColor, col, t);
    }

    gl_FragColor = col;
}
