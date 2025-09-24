$input v_texcoord0
#include <bgfx_shader.sh>
#include <config.h>

SAMPLER2D_AUTOREG(s_ColorTexture);
SAMPLER2D_AUTOREG(s_AverageLuminance);
SAMPLER2D_AUTOREG(s_PreExposureLuminance);
SAMPLER2D_AUTOREG(s_RasterizedColor);

vec3 RRTAndODTFit(vec3 v) {
    vec3 a = v * (v + 0.0245786) - 0.000090537;
    vec3 b = v * (0.983729 * v + 0.4329510) + 0.238081;
    return a / b;
}

vec3 ACESFittedTonemap(vec3 color) {
    return RRTAndODTFit(color);
}

vec3 AdjustSaturation(vec3 color, float saturation) {
    float avg = (color.r + color.g + color.b) / 3.0;
    return mix(vec3_splat(avg), color, saturation);
}

vec3 SampleChromatic(vec2 uv, float offset) {
    vec2 dir = uv - 0.5;
    vec3 col;
    col.r = texture2D(s_ColorTexture, uv + dir * offset).r;
    col.g = texture2D(s_ColorTexture, uv).g;
    col.b = texture2D(s_ColorTexture, uv - dir * offset).b;
    return clamp(col, 0.0, 1.0);
}

float gaussian(float x, float sigma) {
    return exp(-(x*x) / (2.0 * sigma * sigma));
}

vec3 RadialBlur(vec2 uv, float radius, int samples, float sigma) {
    vec3 sum = vec3_splat(0.0);
    float weightSum = 0.0;

    for (int i = 0; i < samples; ++i) {
        float a = (float(i) / float(samples)) * 6.2831853; // full circle
        vec2 dir = vec2(cos(a), sin(a));

        for (int j = -2; j <= 2; ++j) {
            float dist = float(j) * radius;
            float w = gaussian(dist, sigma);
            sum += SampleChromatic(uv + dir * dist, 0.008) * w;
            weightSum += w;
        }
    }

    return sum / max(weightSum, 0.0001); 
}

float ComputeAutoExposure(vec2 uv) {
    float avgLum = texture2D(s_AverageLuminance, vec2_splat(0.5)).r;
    avgLum = max(avgLum, 0.0001);

    float targetLum = 0.18;
    float targetExposure = targetLum / avgLum;

    float prevExposure = texture2D(s_PreExposureLuminance, vec2_splat(0.5)).r;

    float adaptationSpeed = 0.005;
    float exposureValue = mix(prevExposure, targetExposure, adaptationSpeed);

    return clamp(exposureValue, 0.9, 4.0);
}

float AutoBlurStrength(vec2 uv) {
    float avgLum = texture2D(s_AverageLuminance, vec2_splat(0.5)).r;
    avgLum = max(avgLum, 0.0001);

    float targetLum = 0.18;
    float targetBlur = targetLum / avgLum;

    float prevBlur = texture2D(s_PreExposureLuminance, vec2_splat(0.5)).r;

    float adaptationSpeed = 0.05; // slower adaptation
    float depthStrength = mix(prevBlur, targetBlur, adaptationSpeed);

    return clamp(depthStrength, 0.00015, 0.0035);
}

void main() {
    vec2 uv = v_texcoord0.xy;
    vec2 center = vec2_splat(0.5);
    float dist = distance(uv, center);

    float blurAmount = smoothstep(0.1, 0.5, dist);
    float blurStrength = AutoBlurStrength(uv);
    float blurRadius = blurAmount * blurStrength;
    vec3 fullscene = RadialBlur(uv, blurRadius, 15, 0.5);

    float exposureValue = ComputeAutoExposure(uv);
    fullscene *= exposureValue;

    vec3 color = ACESFittedTonemap(fullscene);
    #ifdef LYNX_SATURATION
    color = AdjustSaturation(color, 1.3);
    #endif

    float vignette = pow(1.0 - smoothstep(0.25, 0.8, dist), 1.8);
    #ifdef LYNX_VIGNETTE
    color *= vignette;
#endif

    color = pow(color, vec3_splat(1.0 / 2.2));

    gl_FragColor = vec4(color, 1.0);
}