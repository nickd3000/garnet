// hueshift.frag
// Rotates the hue of every pixel by a given angle, leaving saturation and
// brightness unchanged.  Useful for team-colour recolouring or palette cycling.
//
// Uniforms:
//   hue — hue rotation in degrees (0.0 = no change, 180.0 = complementary colour)

uniform sampler2D texture;
uniform float hue;

// Convert RGB -> HSV
vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

// Convert HSV -> RGB
vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec4 col = texture2D(texture, uv) * gl_Color;

    vec3 hsv = rgb2hsv(col.rgb);
    hsv.x = fract(hsv.x + hue / 360.0);   // rotate hue, wrap at 1.0
    col.rgb = hsv2rgb(hsv);

    gl_FragColor = vec4(col.rgb, col.a);
}
