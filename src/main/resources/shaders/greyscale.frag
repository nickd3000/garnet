uniform sampler2D texture;

void main() {
    vec4 texColor = texture2D(texture, gl_TexCoord[0].st);
    float luma = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
    gl_FragColor = vec4(luma, luma, luma, texColor.a);
}
