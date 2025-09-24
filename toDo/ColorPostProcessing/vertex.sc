$input a_position, a_texcoord0
$output v_texcoord0 
uniform highp vec4 ViewportScale;

#include <bgfx_shader.sh>

void main() { 

     vec2 _210 = a_texcoord0 * ViewportScale.xy;
    v_texcoord0 = vec4(_210.x, _210.y, a_texcoord0.x, a_texcoord0.y);
    gl_Position = vec4((a_position.xy * 2.0) - vec2_splat(1.0), 0.0, 1.0);

}
