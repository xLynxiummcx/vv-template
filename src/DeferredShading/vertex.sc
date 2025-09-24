$input a_position, a_texcoord0
$output v_texcoord0,v_projPosition 
uniform highp vec4 ViewportScale;

#include <bgfx_shader.sh>

void main() { 

  vec4 pos = vec4(a_position, 1.0);

vec2 clipPos = (worldPos.xy * 2.0) - vec2(1.0);


vec2 remappedPos = (a_position.xy * 2.0) - vec2(1.0);

vec2 scaledUV = a_texcoord0 * ViewportScale.xy;

v_projPosition = vec3(remappedPos, a_position.z);

v_texcoord0 = vec4(scaledUV, a_texcoord0);

gl_Position = vec4(clipPos, pos.z, pos.w);
}
