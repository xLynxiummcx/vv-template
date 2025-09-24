$input v_color0, v_fog, v_lightmapUV, v_texcoord0, v_worldPos

#include <bgfx_shader.sh>

SAMPLER2D_AUTOREG(s_LightMapTexture);
SAMPLER2D_AUTOREG(s_MatTexture);
SAMPLER2D_AUTOREG(s_SeasonsTexture);

uniform vec4 FogColor;

void main(){

    vec4 diffuse = texture2D(s_MatTexture, v_texcoord0);

    vec3 blockLight = texture2D(s_LightMapTexture, min(vec2(v_lightmapUV.x, 0.09375), 1.0)).xyz;
    vec3 skyLight = texture2D(s_LightMapTexture, min(vec2(v_lightmapUV.y, 0.03125), 1.0)).xyz;
    vec3 lightmap = saturate(blockLight + skyLight);

    #ifdef GEOMETRY_PREPASS_ALPHA_TEST_PASS
        const float ALPHA_THRESHOLD = 0.5;
        if (diffuse.a < ALPHA_THRESHOLD) {
            discard;
        }
        #endif
        
    diffuse.rgb *= lightmap;

    #if defined(SEASONS) && !defined(TRANSPARENT) && !defined(TRANSPARENT_PBR)
        diffuse.rgb *= mix(vec3(1.0, 1.0, 1.0), texture2D(s_SeasonsTexture, v_color0.xy).rgb * 2.0, v_color0.b);
        diffuse.rgb *= v_color0.a;
        diffuse.a = 1.0;
    #else
        diffuse *= v_color0;
    #endif

    #if defined(GEOMETRY_PREPASS_ALPHA_TEST_PASS) || defined(GEOMETRY_PREPASS_PASS)
        
        vec3 worldPosition = v_worldPos;
        vec3 prevWorldPosition = v_worldPos - u_prevWorldPosOffset.xyz;

        vec4 screenSpacePos = mul(u_viewProj, vec4(worldPosition, 1.0));
        screenSpacePos /= screenSpacePos.w;
        screenSpacePos = screenSpacePos * 0.5 + 0.5;

        vec4 prevScreenSpacePos = mul(u_prevViewProj, vec4(prevWorldPosition, 1.0));
        prevScreenSpacePos /= prevScreenSpacePos.w;
        prevScreenSpacePos = prevScreenSpacePos * 0.5 + 0.5;

        gl_FragData[1] = vec4(0.0, 0.0, screenSpacePos.xy - prevScreenSpacePos.xy);
        gl_FragData[2] = vec4(0.0, 0.0, 0.0, 1.0);

    #endif

        gl_FragData[0].rgb = mix(diffuse.rgb, FogColor.rgb, v_fog.a);
        gl_FragData[0].a = diffuse.a;
}
