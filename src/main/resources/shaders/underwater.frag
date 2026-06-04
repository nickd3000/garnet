// underwater.frag
// Simulates an underwater view with slow wave distortion, a blue-green tint,
// and a gentle brightness oscillation (caustic shimmer).
//
// Uniforms:
//   time       — elapsed time in seconds (drives animation)
//   waveAmp    — distortion amplitude in UV space (e.g. 0.008)
//   waveSpeed  — how fast the waves move (e.g. 1.0)
//   tintStr    — strength of the blue-green tint (0.0 = none, 1.0 = full)

uniform sampler2D texture;
uniform float time;
uniform float waveAmp;
uniform float waveSpeed;
uniform float tintStr;

void main() {
    vec2 uv = gl_TexCoord[0].st;

    // Slow dual-axis sine wave distortion
    float t = time * waveSpeed;
    uv.x += sin(uv.y * 8.0 + t * 1.3) * waveAmp;
    uv.y += sin(uv.x * 6.0 + t * 0.9) * waveAmp * 0.6;

    vec4 col = texture2D(texture, uv) * gl_Color;

    // Blue-green tint
    vec3 tint = vec3(0.15, 0.55, 0.75);
    col.rgb = mix(col.rgb, col.rgb * tint * 1.4, tintStr);

    // Caustic shimmer: subtle brightness pulse
    float shimmer = 1.0 + 0.06 * sin(uv.x * 20.0 + t * 2.1) * sin(uv.y * 15.0 + t * 1.7);
    col.rgb = clamp(col.rgb * shimmer, 0.0, 1.0);

    gl_FragColor = col;
}
