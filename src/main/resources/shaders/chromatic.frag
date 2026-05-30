// chromatic.frag
// Splits the red, green and blue channels apart along the horizontal axis,
// simulating the chromatic aberration seen in cheap lenses or lo-fi screens.
//
// Uniforms:
//   shift — horizontal UV offset between channels (try 0.003–0.01)

uniform sampler2D texture;
uniform float shift;

void main() {
    vec2 uv = gl_TexCoord[0].st;

    float r = texture2D(texture, uv + vec2(-shift, 0.0)).r;
    float g = texture2D(texture, uv).g;
    float b = texture2D(texture, uv + vec2( shift, 0.0)).b;
    float a = texture2D(texture, uv).a;

    gl_FragColor = vec4(r, g, b, a) * gl_Color;
}
