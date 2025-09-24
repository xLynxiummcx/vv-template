$input v_color0, v_fog, v_lightmapUV, v_texcoord0, v_worldPos

#include <bgfx_shader.sh>

SAMPLER2D_AUTOREG(s_LightMapTexture);
SAMPLER2D_AUTOREG(s_MatTexture);
SAMPLER2D_AUTOREG(s_SeasonsTexture);

uniform vec4 FogColor;

void main(){

    vec4 diffuse = texture2D(s_MatTexture, v_texcoord0);

   
    #ifdef GEOMETRYALPHA
        const float ALPHA_THRESHOLD = 0.5;
        if (diffuse.a < ALPHA_THRESHOLD) {
            discard;
        }
        #endif
        
    //diffuse.rgb *= v_lightmapUV;

        diffuse *= v_color0;

#if defined(GEOMETRYALPHA) || defined(GEOMETRYPREEPASS)
        
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
