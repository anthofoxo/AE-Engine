#version 330 core

in vec2 v_texture;
in vec3 v_normal;

layout(location = 0) out vec4 out_color;

uniform vec3 u_color;
uniform sampler2D u_albedo;

void main() {

	vec4 color = texture(u_albedo, v_texture);

	if(color.a < 0.5) discard;

	vec3 toLight = vec3(0.0, 1.0, 0.0);
	float product = max(dot(toLight, normalize(v_normal)), 0.2);

	out_color = vec4(color.rgb/* * product*/, 1.0);

}
