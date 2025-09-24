$input a_color0, a_position, a_texcoord0, a_texcoord1
#ifdef INSTANCING
$input i_data1, i_data2, i_data3
#endif
$output v_color0, v_fog, v_lightmapUV, v_texcoord0, v_worldPos

#include <bgfx_shader.sh>

uniform vec4 FogAndDistanceControl;
uniform vec4 FogColor;
uniform vec4 RenderChunkFogAlpha;
uniform vec4 SubPixelOffset;
uniform vec4 ViewPositionAndTime;


vec4 jitterVertexPosition(vec3 worldPosition) {
    mat4 offsetProj = u_proj;
    #if BGFX_SHADER_LANGUAGE_GLSL
    offsetProj[2][0] += SubPixelOffset.x;
    offsetProj[2][1] -= SubPixelOffset.y;
    #else
    offsetProj[0][2] += SubPixelOffset.x;
    offsetProj[1][2] -= SubPixelOffset.y;
    #endif
    return mul(offsetProj, mul(u_view, vec4(worldPosition, 1.0f)));
}

float calculateFogIntensityFadedVanilla(float cameraDepth, float maxDistance, float fogStart, float fogEnd, float fogAlpha) {
    float distance = cameraDepth / maxDistance;
    distance += fogAlpha;
    return clamp((distance - fogStart) / (fogEnd - fogStart) * 5.0, 0.0, 1.0);
}

#ifdef RENDER_AS_BILLBOARDS
void transformAsBillboardVertex(vec3 worldPos, inout vec4 color0, inout vec4 position){
    color0 = vec4(1.0, 1.0, 1.0, 1.0);
    worldPos += vec3(0.5, 0.5, 0.5);
    vec3 forward = normalize(worldPos - ViewPositionAndTime.xyz);
    vec3 right = normalize(cross(vec3(0.0, 1.0, 0.0), forward));
    vec3 up = cross(forward, right);
    vec3 offsets = color0.xyz;
    worldPos -= up * (offsets.z - 0.5) + right * (offsets.x - 0.5);
    position = mul(u_viewProj, vec4(worldPos, 1.0));
}
#endif

void main(){

    vec4 color0 = a_color0;
    
    vec3 worldPos;
    vec4 position;

    worldPos = mul(u_model[0], vec4(a_position, 1.0)).xyz;
    
    #ifdef INSTANCING
    mat4 model;
    model[0] = vec4(i_data1.x, i_data2.x, i_data3.x, 0);
    model[1] = vec4(i_data1.y, i_data2.y, i_data3.y, 0);
    model[2] = vec4(i_data1.z, i_data2.z, i_data3.z, 0);
    model[3] = vec4(i_data1.w, i_data2.w, i_data3.w, 1);
    worldPos = instMul(model, vec4(a_position, 1.0)).xyz;
    #endif

    #ifdef RENDER_AS_BILLBOARDS
    transformAsBillboardVertex(worldPos, color0, position);
    #endif

    float cameraDepth = length(ViewPositionAndTime.xyz - worldPos);
    float fogIntensity = calculateFogIntensityFadedVanilla(cameraDepth, FogAndDistanceControl.z, FogAndDistanceControl.x, FogAndDistanceControl.y, RenderChunkFogAlpha.x);

    v_color0 = color0;
    v_fog = vec4(FogColor.rgb, fogIntensity);
    v_lightmapUV = a_texcoord1;
    v_texcoord0 = a_texcoord0;
    v_worldPos = worldPos;
    position = jitterVertexPosition(worldPos);

    gl_Position = position;
}