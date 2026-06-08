// Default buffered sprite/shape batch fragment shader.
// material flag 1: textured, material flag 2: color override/silhouette.
uniform sampler2D u_texture;

varying vec2 v_texCoord;
varying vec4 v_color;
varying float v_materialFlags;

void main() {
    bool textured = mod(v_materialFlags, 2.0) >= 1.0;
    bool colorOverride = mod(floor(v_materialFlags / 2.0), 2.0) >= 1.0;

    if (!textured) {
        gl_FragColor = v_color;
        return;
    }

    vec4 texColor = texture2D(u_texture, v_texCoord);
    if (colorOverride) {
        gl_FragColor = vec4(v_color.rgb, texColor.a * v_color.a);
    } else {
        gl_FragColor = texColor * v_color;
    }
}
