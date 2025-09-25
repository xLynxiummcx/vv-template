#include <bgfx_compute.sh>
#include <bgfx_shader.sh>


uniform vec4 VolumeDimensions; // x: width, y: height, z: depth
uniform vec4 VolumeNearFar;    // x: near, y: far

IMAGE2D_ARRAY_RO_AUTOREG(s_LightingBuffer, rgba16f);
IMAGE2D_ARRAY_WR_AUTOREG(s_ScatteringBuffer, rgba16f);

NUM_THREADS(8, 8, 1)
void main()
{
 uvec3 id = gl_GlobalInvocationID;
    int x = int(id.x);
    int y = int(id.y);
    int z = 0;
imageStore(s_ScatteringBuffer, ivec3(x, y, z), vec4_splat(0.0));
      
}