uniform sampler2D texture;
uniform vec2 resolution;   // screen width, height in pixels

void main() {
    vec2 uv = gl_TexCoord[0].st;

    // ── 1. Screen curvature (barrel distortion) ──────────────────────────
    vec2 curved = uv * 2.0 - 1.0;
    vec2 offset = curved.yx * curved.yx * 0.07;
    curved += curved * offset;
    vec2 distUV = curved * 0.5 + 0.5;

    // Vignette / black border outside the curved screen area
    if (distUV.x < 0.0 || distUV.x > 1.0 || distUV.y < 0.0 || distUV.y > 1.0) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        return;
    }

    // ── 2. Sampling ─────────────────────────────────────────────────────
    vec4 col = texture2D(texture, distUV);

    // ── 3. Scanlines ─────────────────────────────────────────────────────
    // Scanlines are based on the internal resolution (y-axis)
    float scanline = sin(distUV.y * resolution.y * 3.14159 * 2.0) * 0.5 + 0.5;
    scanline = pow(scanline, 0.4);          // sharpen/soften the dark bands
    col.rgb *= mix(0.7, 1.0, scanline);    // darken every other line

    // ── 4. Horizontal colour bleeding (chromatic aberration) ─────────────
    float bleed = 1.0 / resolution.x;
    float r = texture2D(texture, distUV + vec2(-bleed, 0.0)).r;
    float b = texture2D(texture, distUV + vec2( bleed, 0.0)).b;
    col.r = mix(col.r, r, 0.45);
    col.b = mix(col.b, b, 0.45);

    // ── 5. Vignette (edge darkening) ─────────────────────────────────────
    vec2 vig = distUV * (1.0 - distUV.yx);
    float vignette = pow(vig.x * vig.y * 18.0, 0.35);
    col.rgb *= vignette;

    // ── 6. Slight brightness boost to compensate for all the darkening ───
    col.rgb *= 1.25;

    gl_FragColor = vec4(col.rgb, 1.0);
}
