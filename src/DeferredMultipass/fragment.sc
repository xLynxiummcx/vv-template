$input v_texcoord0, v_projPosition
#include <bgfx_shader.sh>


void main() {
    vec2 uv = v_texcoord0.xy;
    vec3 color = vec3_splat(0.0);  
    gl_FragColor = vec4(color, 1.0);
}