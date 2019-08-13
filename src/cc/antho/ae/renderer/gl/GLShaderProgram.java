package cc.antho.ae.renderer.gl;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import cc.antho.ae.renderer.color.Color;
import lombok.Getter;

public final class GLShaderProgram implements Destroyable {

	@Getter private int handle;

	private final Map<String, Integer> uniforms = new HashMap<>();
	private final float[] m44 = new float[16];

	GLShaderProgram(GLShaderSource... sources) {

		this.handle = glCreateProgram();

		for (GLShaderSource source : sources)
			glAttachShader(handle, source.getHandle());

		glLinkProgram(handle);
		GLShaderUtil.checkError(true, handle, GL_LINK_STATUS);

		glValidateProgram(handle);
		GLShaderUtil.checkError(true, handle, GL_VALIDATE_STATUS);

		for (GLShaderSource source : sources)
			glDetachShader(handle, source.getHandle());

	}

	public void uniform1i(String name, int v0) {

		glUniform1i(getUniformLocation(name), v0);

	}

	public void uniform1f(String name, float v0) {

		glUniform1f(getUniformLocation(name), v0);

	}

	public void uniform2i(String name, int v0, int v1) {

		glUniform2i(getUniformLocation(name), v0, v1);

	}

	public void uniform2f(String name, float v0, float v1) {

		glUniform2f(getUniformLocation(name), v0, v1);

	}

	public void uniform3i(String name, int v0, int v1, int v2) {

		glUniform3i(getUniformLocation(name), v0, v1, v2);

	}

	public void uniform3f(String name, float v0, float v1, float v2) {

		glUniform3f(getUniformLocation(name), v0, v1, v2);

	}

	public void uniform4i(String name, int v0, int v1, int v2, int v3) {

		glUniform4i(getUniformLocation(name), v0, v1, v2, v3);

	}

	public void uniform4f(String name, float v0, float v1, float v2, float v3) {

		glUniform4f(getUniformLocation(name), v0, v1, v2, v3);

	}

	public void uniform4f(String name, Color v0) {

		glUniform4f(getUniformLocation(name), v0.r, v0.g, v0.b, v0.a);

	}

	public void uniform4f(String name, Vector4f v0) {

		glUniform4f(getUniformLocation(name), v0.x, v0.y, v0.z, v0.w);

	}

	public void uniform3f(String name, Vector3f v0) {

		uniform3f(name, v0.x, v0.y, v0.z);

	}

	public void uniform3f(String name, Color v0) {

		uniform3f(name, v0.r, v0.g, v0.b);

	}

	public void uniformMat4f(String name, Matrix4f v0) {

		v0.get(m44);
		glUniformMatrix4fv(getUniformLocation(name), false, m44);

	}

	public int getUniformLocation(String name) {

		if (uniforms.containsKey(name)) return uniforms.get(name);

		int location = glGetUniformLocation(handle, name);

		uniforms.put(name, location);

		return location;

	}

	public void bind() {

		glUseProgram(handle);

	}

	public void destroy() {

		glDeleteProgram(handle);

	}

}
