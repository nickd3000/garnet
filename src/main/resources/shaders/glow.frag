// glow.frag
// Adds a soft additive halo around the opaque pixels of a sprite by sampling
// a ring of neighbours and accumulating their alpha contributions.
//
// Uniforms:
//   texelSize — (1/texW, 1/texH) — one texel in UV space
//   glowColor — colour of the halo (RGBA)
//   glowRadius — sampling radius in texels (e.g. 3.0)

uniform sampler2D texture;
uniform vec2 texelSize;
uniform vec4 glowColor;
uniform float glowRadius;

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec4 col = texture2D(texture, uv) * gl_Color;

    // Accumulate alpha from neighbours in a circular pattern
    float glow = 0.0;
    float samples = 0.0;
    float r = glowRadius;
    for (float dx = -r; dx <= r; dx += 1.0) {
        for (float dy = -r; dy <= r; dy += 1.0) {
            float dist = sqrt(dx * dx + dy * dy);
            if (dist > r) continue;
            float weight = 1.0 - dist / r;
            glow += texture2D(texture, uv + vec2(dx, dy) * texelSize).a * weight;
            samples += weight;
        }
    }
    glow = clamp(glow / samples, 0.0, 1.0);

    // Only apply glow to transparent pixels; opaque pixels keep their colour
    if (col.a < 0.01) {
        gl_FragColor = vec4(glowColor.rgb, glowColor.a * glow);
    } else {
        gl_FragColor = col;
    }
}
