#ifdef GL_ES
precision mediump float;
#endif

uniform vec3 u_glowColour;
uniform float u_glowIntensity;

out vec4 pixel;

void main() {
    pixel = vec4(u_glowColour * u_glowIntensity, 1.0);
}