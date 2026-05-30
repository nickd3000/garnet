uniform sampler2D texture;
uniform vec2 texelSize;   // 1.0/width, 1.0/height — set each frame

void main() {
    vec2 uv = gl_TexCoord[0].st;

    // 5x5 box blur accumulated as additive glow
    vec4 glow = vec4(0.0);
    int radius = 6;
    float samples = 0.0;
    for (int x = -radius; x <= radius; x++) {
        for (int y = -radius; y <= radius; y++) {
            glow += texture2D(texture, uv + vec2(float(x), float(y)) * texelSize);
            samples += 1.0;
        }
    }
    glow /= samples;

    // Boost brightness so the halo glows visibly
    glow.rgb *= 2.5;
    glow.a = glow.a * 0.85;

    gl_FragColor = glow;
}
