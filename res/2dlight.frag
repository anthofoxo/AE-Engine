#version 330 core

// Input texture coordinates
in vec2 v_texture;

// Output color, to be used with additive blending,
// Note: This shader never outputs alpha data
layout(location = 0) out vec4 out_color;

//  General shader settings
// -------------------------

// The texture in which to sample the data from
uniform sampler2D u_texture;

// Aspect ratio of the framebuffer,
// this allows the shader to keep the light radius circular
uniform float u_aspect;

// How often should we sample the image,
// the smaller the value, the more accurate the output is
// at the cost of performance
uniform float u_stepSize;

// The filter color is what the shader will test for collision against
uniform vec4 u_filterColor;

//  Grain settings
// ----------------

// Used to animate grain, this may be left at 0.0
uniform float u_grain;

// How much grain should we apply,
// generally no more than 1.0 should be needed.
// Grain is only applied if the scale is larger than 0.0
uniform float u_grainScale;

//  General light settings
// ------------------------

// The position of the light
uniform vec2 u_lightPosition;

// The color of the light
uniform vec3 u_lightColor;

// The size of the light source,
// 0.0 disables the light,
// 1.0 is the size of the framebuffer width or height (dependent on aspect ratio)
uniform float u_lightSize;

//  Spot light settings
// ---------------------

// Angle of -1.0 disables spot lights,
// angle of 1.0 is only the light directly lining up with the light angle,
uniform float u_angle;

// direction the spot light should travel in,
// does not have to be normalized
uniform vec2 u_direction;

// Hash function to simulate noise
float rand(vec2 co){

	return fract(sin(dot(co.xy + u_grain, vec2(12.9898, 78.233))) * 43758.5453);

}

void main() {

	// The current fragment
	vec2 position = v_texture;

	// The direction from this fragment to the light point
	vec2 direction = u_lightPosition - v_texture;

	// Calculate the distance to the light point
	float maxDistance = length(direction);

	// Calculate the distance to the light point, this adjusts for non-square images
	float maxDistanceNormalized;
	if(u_aspect > 1.0) maxDistanceNormalized = sqrt((direction.x * u_aspect) * (direction.x * u_aspect) + direction.y * direction.y);
	else maxDistanceNormalized = sqrt(direction.x * direction.x + (direction.y / u_aspect) * (direction.y / u_aspect));

	// After calculating the distances, we normalize our direction
	direction = normalize(direction);

	bool inShadow = false;

	// Determine if we are in shadow before ray marching
	// If the normalized distance is greater than the distance light can reach, we can safely assume we are in shadow
	if(maxDistanceNormalized > u_lightSize) inShadow = true;

	// Spot lights, we take a dot product of the target direction and the current light beam
	if(!inShadow && u_angle > -1.0 && -dot(direction, normalize(u_direction)) < u_angle) inShadow = true;

	// After determining spot lights we set the direction to match the step size
	direction *= u_stepSize;

	// If the last test caused us to be put into shadow, we can skip the ray marching
	if(!inShadow)

		// Our starting distance is 0 and as we sample the image,
		// we increase the distance traveled and the position we test against using the direction
		for(float currentDistance = 0.0; currentDistance < maxDistance;) {
			// Sample the image at the current point,
			// if we hit a point that matches our filter,
			// then light cannot reach this point,
			// we can safely break from the loop
			if(texture(u_texture, position) == u_filterColor) {
				inShadow = true;
				break;
			}

			// Calculate the random offset from the grain settings
			float random = 1.0;
			if(u_grainScale > 0.0) random = rand(position) * u_grainScale + 1.0;

			// Step the current ray, using the random offset to hide fixed sample sizes
			currentDistance += u_stepSize * random;
			position += direction * random;

		}

	// If we were in shadow then set the pixel color to be the shadow color
	if(inShadow) out_color = vec4(vec3(0.0), 1.0);
	// Otherwise we are exposed to light
	else {
		// We normalize our bias to be between 0 and 1
		float bias = maxDistanceNormalized / u_lightSize;

		// We calculate the intensity of the light as a mixture of the shadow and light color using our bias
		out_color = vec4(mix(u_lightColor, vec3(0.0), bias * bias), 1.0);
	}

	// Overlay original texture
	// At the end of rendering lights, overlay the original image,
	// do this in a different shader

}
