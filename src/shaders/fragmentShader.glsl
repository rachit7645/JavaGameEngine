#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D modelTexture;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.2f);
    vec3 diffuse = brightness * lightColour;

    vec3 unitVectortoCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectortoCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

    vec4 textureColor = texture(modelTexture, pass_textureCoords);

    if(textureColor.a < 0.5) {
        discard;
    }

    outColor = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);

}