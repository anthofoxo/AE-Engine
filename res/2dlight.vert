#version 330 core

layout(location = 0) in vec2 in_position;

out vec2 v_texture;

void main() {

	gl_Position = vec4(in_position, 0.0, 1.0);
	v_texture = (in_position + 1.0) / 2.0;

}
