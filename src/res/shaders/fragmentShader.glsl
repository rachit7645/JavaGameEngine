#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D modelTexture;
uniform vec3 lightColour[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectortoCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i = 0; i < 4; i++) {
        float distance = length(toLightVector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        vec3 unitLightVector = normalize(toLightVector[i]);

        float nDot1 = dot(unitNormal, unitLightVector);
        float brightness = max(nDot1, 0.0f);

        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

        float specularFactor = dot(reflectedLightDirection, unitVectortoCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);
        totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attFactor;
        totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attFactor;
    }
    vec4 textureColor = texture(modelTexture, pass_textureCoords);

    totalDiffuse = max(totalDiffuse, vec3(0.2));

    if (textureColor.a < 0.5) {
        discard;
    }

    outColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);

}