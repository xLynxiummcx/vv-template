$input a_color0, a_position, a_texcoord0
$output v_clipPosition, v_color0, v_texcoord0, v_worldPos

uniform highp vec4 ViewportScale;
uniform mat4 CubemapRotation;
uniform vec4 SubPixelOffset;

#include <bgfx_shader.sh>

void main()
{
    vec4 worldPos = mul(u_model[0], mul(CubemapRotation, vec4(a_position, 1.0)));

    mat4 proj = u_proj;
    proj[2].x += SubPixelOffset.x;
    proj[2].y -= SubPixelOffset.y;

    vec4 clipPos = mul(proj, mul(u_view, worldPos));

    v_clipPosition = clipPos;
    v_color0      = a_color0;
    v_texcoord0   = a_texcoord0;
    v_worldPos    = worldPos.xyz;

    // Final vertex position
    vec4 vertexpos = mul(u_viewProj ,vec4(worldPos.xyz, 1.0));
       
    gl_Position = vertexpos;
}