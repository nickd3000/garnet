// colourgrade.frag
// Applies a colour-grading preset to the whole frame, remapping colours to
// create a specific mood or look.
//
// Uniforms:
//   style  — preset index:
//              0 = sepia (warm brown tones)
//              1 = cold  (desaturated blue tint)
//              2 = warm  (boosted reds/yellows, sunset feel)
//              3 = night (dark green-tinted, night-vision style)
//   blend  — mix factor between original and graded colour (0.0 = original, 1.0 = full grade)

uniform sampler2D texture;
uniform float style;
uniform float blend;

vec3 gradeSepia(vec3 c) {
    float lum = dot(c, vec3(0.299, 0.587, 0.114));
    return vec3(lum * 1.2, lum * 1.0, lum * 0.7);
}

vec3 gradeCold(vec3 c) {
    float lum = dot(c, vec3(0.299, 0.587, 0.114));
    vec3 grey = vec3(lum);
    vec3 tinted = grey + vec3(-0.05, 0.0, 0.15);
    return mix(c, tinted, 0.6);
}

vec3 gradeWarm(vec3 c) {
    c.r = clamp(c.r * 1.15, 0.0, 1.0);
    c.g = clamp(c.g * 1.05, 0.0, 1.0);
    c.b = clamp(c.b * 0.80, 0.0, 1.0);
    return c;
}

vec3 gradeNight(vec3 c) {
    float lum = dot(c, vec3(0.299, 0.587, 0.114));
    return vec3(0.0, lum * 0.85, lum * 0.3) * 0.8;
}

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec4 col = texture2D(texture, uv) * gl_Color;

    vec3 graded;
    if (style < 0.5) {
        graded = gradeSepia(col.rgb);
    } else if (style < 1.5) {
        graded = gradeCold(col.rgb);
    } else if (style < 2.5) {
        graded = gradeWarm(col.rgb);
    } else {
        graded = gradeNight(col.rgb);
    }

    gl_FragColor = vec4(mix(col.rgb, graded, blend), col.a);
}
