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

    // ── 2. Pixel / phosphor grid ─────────────────────────────────────────
    // Snap to nearest "CRT pixel" (3x3 screen pixels per CRT dot)
    float pixelSize = 3.0;
    vec2 pixelUV = floor(distUV * resolution / pixelSize) * pixelSize / resolution;
    vec4 col = texture2D(texture, pixelUV);

    // Sub-pixel RGB stripe mask (R | G | B repeating every 3 screen pixels)
    float subX = mod(floor(distUV.x * resolution.x), 3.0);
    vec3 mask = vec3(0.0);
    if      (subX < 1.0) mask = vec3(1.0, 0.2, 0.2);
    else if (subX < 2.0) mask = vec3(0.2, 1.0, 0.2);
    else                 mask = vec3(0.2, 0.2, 1.0);
    col.rgb *= mix(vec3(1.0), mask, 0.35);

    // ── 3. Scanlines ─────────────────────────────────────────────────────
    float scanline = sin(distUV.y * resolution.y * 3.14159) * 0.5 + 0.5;
    scanline = pow(scanline, 0.6);          // soften the dark bands
    col.rgb *= mix(0.55, 1.0, scanline);    // darken every other line

    // ── 4. Horizontal colour bleeding (chromatic aberration) ─────────────
    float bleed = 1.5 / resolution.x;
    float r = texture2D(texture, pixelUV + vec2(-bleed, 0.0)).r;
    float b = texture2D(texture, pixelUV + vec2( bleed, 0.0)).b;
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
