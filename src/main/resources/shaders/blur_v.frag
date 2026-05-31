uniform sampler2D texture;
uniform vec2 texelSize;   // vec2(1.0/width, 1.0/height)
uniform float blurRadius; // tap spacing multiplier (1.0 = normal, 0.0 = no blur)

// 9-tap vertical Gaussian blur (sigma ≈ 2)
// Weights are the normalised row of Pascal's triangle: 1 8 28 56 70 56 28 8 1
void main() {
    vec2 uv = gl_TexCoord[0].st;
    float dy = texelSize.y * blurRadius;

    vec4 col = vec4(0.0);
    col += texture2D(texture, uv + vec2(0.0, -4.0 * dy)) * (1.0  / 256.0);
    col += texture2D(texture, uv + vec2(0.0, -3.0 * dy)) * (8.0  / 256.0);
    col += texture2D(texture, uv + vec2(0.0, -2.0 * dy)) * (28.0 / 256.0);
    col += texture2D(texture, uv + vec2(0.0, -1.0 * dy)) * (56.0 / 256.0);
    col += texture2D(texture, uv                        ) * (70.0 / 256.0);
    col += texture2D(texture, uv + vec2(0.0,  1.0 * dy)) * (56.0 / 256.0);
    col += texture2D(texture, uv + vec2(0.0,  2.0 * dy)) * (28.0 / 256.0);
    col += texture2D(texture, uv + vec2(0.0,  3.0 * dy)) * (8.0  / 256.0);
    col += texture2D(texture, uv + vec2(0.0,  4.0 * dy)) * (1.0  / 256.0);

    gl_FragColor = vec4(col.rgb, 1.0);
}
