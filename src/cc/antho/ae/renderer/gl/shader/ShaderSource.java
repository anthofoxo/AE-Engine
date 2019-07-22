package cc.antho.ae.renderer.gl.shader;

import static org.lwjgl.opengl.GL20.*;

import lombok.Getter;

public final class ShaderSource {

	@Getter private int handle;

	public ShaderSource(String source, int type) {

		handle = glCreateShader(type);

		glShaderSource(handle, source);
		glCompileShader(handle);

		ShaderUtil.checkError(false, handle, GL_COMPILE_STATUS);

	}

	public void destroy() {

		glDeleteShader(handle);

	}

}
