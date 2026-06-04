// filmgrain.frag
// Adds animated random noise over the whole frame, simulating film grain or
// a dirty/worn screen.
//
// Uniforms:
//   time      — elapsed time in seconds (drives animation)
//   strength  — grain intensity (0.0 = none, 1.0 = very heavy)

uniform sampler2D texture;
uniform float time;
uniform float strength;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7)) + time * 7.3) * 43758.5453);
}

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec4 col = texture2D(texture, uv) * gl_Color;

    float grain = (hash(uv) - 0.5) * strength;
    col.rgb = clamp(col.rgb + grain, 0.0, 1.0);

    gl_FragColor = col;
}
