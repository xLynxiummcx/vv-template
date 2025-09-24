$input v_clipPosition, v_color0, v_texcoord0, v_worldPos

#include <bgfx_shader.sh>

uniform vec4 PreExposureEnabled;
uniform vec4 BlockBaseAmbientLightColorIntensity;
uniform vec4 SkyAmbientLightColorIntensity;
uniform vec4 DiffuseSpecularEmissiveAmbientTermToggles;
uniform vec4 VolumeScatteringEnabledAndPointLightVolumetricsEnabled;
uniform vec4 VolumeNearFar;
uniform vec4 VolumeDimensions;
uniform vec4 SunDir;
uniform vec4 SkyProbeUVFadeParameters; 

SAMPLER2D_AUTOREG(s_PreviousFrameAverageLuminance);
SAMPLER2D_AUTOREG(s_MatTexture);
SAMPLER2DARRAY_AUTOREG(s_ScatteringBuffer);

void main()
{
    vec4 outColor;
    highp vec4 matColor = texture2D(s_MatTexture, v_texcoord0);


    outColor = matColor;
    gl_FragColor = outColor;
}