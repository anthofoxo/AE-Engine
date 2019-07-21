package cc.antho.ae.gl.shader;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import cc.antho.ae.gl.color.Color;
import lombok.Getter;

public final class ShaderProgram {

	public static final ShaderProgram DEFAULT = new ShaderProgram(0);

	@Getter private int handle;

	private final Map<String, Integer> uniforms = new HashMap<>();
	private final float[] m44 = new float[16];

	public ShaderProgram(int handle) {

		this.handle = handle;

	}

	public ShaderProgram(ShaderSource... sources) {

		this(glCreateProgram());

		for (ShaderSource source : sources)
			glAttachShader(handle, source.getHandle());

		glLinkProgram(handle);
		ShaderUtil.checkError(true, handle, GL_LINK_STATUS);

		glValidateProgram(handle);
		ShaderUtil.checkError(true, handle, GL_VALIDATE_STATUS);

		for (ShaderSource source : sources)
			glDetachShader(handle, source.getHandle());

	}

	public void uniform1f(String name, float v0) {

		glUniform1f(getUniformLocation(name), v0);

	}
	
	public void uniform1i(String name, int v0) {

		glUniform1i(getUniformLocation(name), v0);

	}

	public void uniform4f(String name, Color v0) {

		glUniform4f(getUniformLocation(name), v0.r, v0.g, v0.b, v0.a);

	}

	public void uniform4f(String name, Vector4f v0) {

		glUniform4f(getUniformLocation(name), v0.x, v0.y, v0.z, v0.w);

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

	public void destory() {

		glDeleteProgram(handle);

	}

}
