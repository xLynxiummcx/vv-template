$input v_worldPos, v_clipPosition, v_texcoord0 
#include <bgfx_shader.sh>

uniform highp vec4 Time;
uniform highp vec4 PreExposureEnabled;

SAMPLER2D_AUTOREG(s_PreviousFrameAverageLuminance);
SAMPLER2D_AUTOREG(s_SkyTexture);



void main() {

    float time = Time.w;
    vec3 viewDir = normalize(v_worldPos);
    vec2 uv = v_texcoord0;
    vec4 diffuse = texture2D(s_SkyTexture, uv);

vec3 col = diffuse.rgb;

     if (PreExposureEnabled.x > 0.0)
    {
        col = col * ((0.180000007152557373046875 / texture(s_PreviousFrameAverageLuminance, vec2(0.5)).x) + 9.9999997473787516355514526367188e-05);
    }
   
  gl_FragColor =vec4(col, 1.0);
}


