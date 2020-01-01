package cc.antho.ae.renderer.gl;

import static org.lwjgl.opengl.GL20.*;

import cc.antho.common.Destroyable;
import lombok.Getter;

public final class GLShaderSource implements Destroyable {

	@Getter private int handle;

	GLShaderSource(String source, int type) {

		handle = glCreateShader(type);

		glShaderSource(handle, source);
		glCompileShader(handle);

		GLShaderUtil.checkError(false, handle, GL_COMPILE_STATUS);

	}

	public void destroy() {

		glDeleteShader(handle);

	}

}
