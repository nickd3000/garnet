// wave.frag
// Applies a rippling wave distortion to a sprite or full-screen texture.
// UV coordinates are offset by a sine wave, giving a wobbly water-like effect.
//
// Uniforms:
//   time      — elapsed time in seconds (drives the animation)
//   amplitude — strength of the distortion in UV space (try 0.01–0.03)
//   frequency — number of wave cycles across the texture (try 8.0–20.0)

uniform sampler2D texture;
uniform float time;
uniform float amplitude;
uniform float frequency;

void main() {
    vec2 uv = gl_TexCoord[0].st;

    // Horizontal wave based on vertical position + time
    uv.x += sin(uv.y * frequency + time * 3.0) * amplitude;
    // Vertical wave based on horizontal position + time (offset phase)
    uv.y += sin(uv.x * frequency + time * 2.5 + 1.2) * amplitude;

    gl_FragColor = texture2D(texture, uv) * gl_Color;
}
