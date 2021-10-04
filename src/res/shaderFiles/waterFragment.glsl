#version 400 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;

out vec4 out_Color;

uniform float moveFactor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;

const float waveStrength = 0.02f;

void main(void) {

	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0f + 0.5f;
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

	vec2 distortion1 = (texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2f - 1f) * waveStrength;
	vec2 distortion2 = (texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y + moveFactor)).rg * 2f - 1f) * waveStrength;
	vec2 totalDistortion = distortion1 + distortion2;

	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

	vec4 reflectColour = texture(reflectionTexture, reflectTexCoords);
	vec4 refractionColour = texture(refractionTexture, refractTexCoords);

	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, vec3(0.0, 1.0, 0.0));

	out_Color = mix(reflectColour, refractionColour, refractiveFactor);

}