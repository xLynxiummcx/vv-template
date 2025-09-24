$input v_texcoord0
#include <bgfx_shader.sh>

SAMPLER2D_AUTOREG(s_ColorTexture);
SAMPLER2D_AUTOREG(s_AverageLuminance);
SAMPLER2D_AUTOREG(s_PreExposureLuminance);
SAMPLER2D_AUTOREG(s_RasterizedColor);


void main() {
    vec2 uv = v_texcoord0.xy;
    vec3 color = texture2D(s_ColorTexture,uv).RGB; gl_FragColor = vec4(color, 1.0);
}