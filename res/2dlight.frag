#version 330 core

in vec2 v_texture;

layout(location = 0) out vec4 out_color;

uniform vec2 u_lightPos;

uniform sampler2D u_texture;
uniform float u_aspect;
uniform vec3 lightColor;
uniform float atten;

const vec4 filterColor = vec4(1.0, 1.0, 1.0, 1.0);
const vec3 shadowColor = vec3(0.0, 0.0, 0.0);
const float stepSize = 0.002;

void main() {

	// The current fragment
	vec2 position = v_texture;

	// The direction from this fragment to the light point
	vec2 direction = u_lightPos - v_texture;

	// Calculate the distance to the light point
	float maxDistance = length(direction);

	// Calculate the distance to the light point, this adjusts for nonsquare images
	float maxDistanceNormalized;
	if(u_aspect > 1.0) maxDistanceNormalized = sqrt((direction.x * u_aspect) * (direction.x * u_aspect) + direction.y * direction.y);
	else maxDistanceNormalized = sqrt(direction.x * direction.x + (direction.y / u_aspect) * (direction.y / u_aspect));

	// After calculating the distances, we normalize our direction to sync up with our step size
	direction = normalize(direction) * stepSize;

	bool inShadow = false;

	// Determine if we are in shadow before ray marching
	// If the normalized distance is greater than the distance light can reach, we can safely assume we are in shadow
	if(maxDistanceNormalized > atten) inShadow = true;

	// If the last test caused us to be put into shadow, we can skip the ray marching
	if(!inShadow)

		// Our starting distance is 0 and as we sample the image,
		// we increase the distance traveled and the position we test against using the direction
		for(float currentDistance = 0.0; currentDistance < maxDistance;	currentDistance += stepSize, position += direction)
			// Sample the image at the current point,
			// if we hit a point that matches our filter,
			// then light cannot reach this point,
			// we can safely break from the loop
			if(texture(u_texture, position) == filterColor) {
				inShadow = true;
				break;
			}

	// If we were in shadow then set the pixel color to be the shadow color
	if(inShadow) out_color = vec4(shadowColor, 1.0);
	// Otherwise we are exposed to light
	else {
		// We normalize our bias to be between 0 and 1
		float bias = maxDistanceNormalized / atten;

		// We calculate the intensity of the light as a mixture of the shadow and light color using our bias
		out_color = vec4(mix(lightColor, shadowColor, bias * bias), 1.0);
	}

	// This simply overlays the original image back onto,
	// ideally this would be done outside of this shader
	//vec4 originalColor = texture(u_texture, v_texture);
	//out_color = mix(out_color, originalColor, originalColor.r);

}
