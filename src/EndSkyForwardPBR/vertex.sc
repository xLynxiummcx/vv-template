$input a_color0, a_position, a_texcoord0
$output v_worldPos, v_clipPosition, v_texcoord0 
uniform mat4 UV0Transform;

#include <bgfx_shader.sh>

void main() { 

 vec4 world = mul(u_model[0],vec4(a_position, 1.0));
   vec4 vertexpos = mul(u_viewProj,vec4(world.xyz, 1.0));
    v_clipPosition = vertexpos.xyz;
    v_texcoord0 = mul((UV0Transform,vec4(a_texcoord0, 0.0, 1.0))).xy;
    v_worldPos = world.xyz;
    gl_Position = vertexpos;
}
