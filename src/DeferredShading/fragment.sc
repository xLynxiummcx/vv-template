$input v_texcoord0
#include <bgfx_shader.sh>

uniform highp vec4 PreExposureEnabled;
SAMPLER2D_AUTOREG(s_PreviousFrameAverageLuminance);
uniform highp vec4 AtmosphericScatteringToggles;
uniform highp vec4 VolumeScatteringEnabledAndPointLightVolumetricsEnabled;
uniform highp vec4 VolumeNearFar;
SAMPLER2DARRAY_AUTOREG(s_ScatteringBuffer);
uniform highp vec4 VolumeDimensions;
uniform highp vec4 FogAndDistanceControl;
uniform highp vec4 RenderChunkFogAlpha;
uniform highp vec4 FogColor;
uniform highp vec4 SunDir;
uniform highp vec4 MoonDir;
uniform highp vec4 SunColor;
uniform highp vec4 MoonColor;
uniform highp vec4 SkyZenithColor;
uniform highp vec4 SkyHorizonColor;
uniform highp vec4 FogSkyBlend;
uniform highp vec4 AtmosphericScattering;
uniform highp vec4 DiffuseSpecularEmissiveAmbientTermToggles;
uniform highp vec4 BlockBaseAmbientLightColorIntensity;
uniform highp vec4 SkyAmbientLightColorIntensity;
uniform highp vec4 CameraLightIntensity;
uniform highp vec4 AmbientLightParams;
uniform highp vec4 EmissiveMultiplierAndDesaturationAndCloudPCFAndContribution;
uniform highp vec4 QuantizationParameters;
uniform highp vec4 DirectionalLightSkyLightHeuristicToggles;
uniform highp vec4 DirectionalShadowModeAndCloudShadowToggleAndPointLightToggleAndShadowToggle;
uniform highp vec4 DirectionalLightSourceShadowDirection;
uniform highp vec4 NdLFloor;
uniform highp vec4 SubsurfaceScatteringContributionAndDiffuseWrapValueAndFalloffScale;
uniform highp vec4 DirectionalLightSourceWorldSpaceDirection;
uniform highp vec4 DirectionalLightSourceDiffuseColorAndIlluminance;
uniform highp vec4 DirectionalLightToggleAndMaxDistanceAndMaxCascadesPerLight;
uniform highp vec4 ShadowFilterOffsetAndRangeFarAndMapSizeAndNormalOffsetStrength;
uniform highp vec4 CloudShadowsVisible;
uniform highp mat4 CloudShadowProj;
uniform highp vec4 CascadesPerSet;
SAMPLER2DARRAY_AUTOREG(s_ShadowCascades);
uniform highp vec4 CascadesParameters[8];
uniform highp vec4 FirstPersonPlayerShadowsEnabledAndResolutionAndFilterWidthAndTextureDimensions;
uniform highp mat4 PlayerShadowProj;
uniform highp mat4 CascadesShadowInvProj[8];
uniform highp mat4 CascadesShadowProj[8];
uniform highp vec4 CausticsParameters;
uniform highp vec4 WorldOrigin;
SAMPLER2DARRAY_AUTOREG(s_CausticsTexture);
uniform highp vec4 CausticsTextureParameters;
SAMPLER2D_AUTOREG(s_SceneDepth);
uniform highp vec4 SubPixelOffset;
uniform highp vec4 QuantizationPrecisionRoundingParameters;
SAMPLER2D_AUTOREG(s_ColorMetalnessSubsurface);
SAMPLER2D_AUTOREG(s_EmissiveAmbientLinearRoughness);
uniform highp vec4 CameraAmbientSamples;
uniform highp vec4 GameplayWorldStatus;
uniform highp vec4 SkySamplesConfig;
SAMPLER3D_AUTOREG(s_SkyAmbientSamples);
SAMPLER2D_AUTOREG(s_Normal);


void main() {
    vec2 uv = v_texcoord0.xy;
    vec3 color = texture2D(s_ColorMetalnessSubsurface,uv).rgb;  
    gl_FragColor = vec4(color, 1.0);
}