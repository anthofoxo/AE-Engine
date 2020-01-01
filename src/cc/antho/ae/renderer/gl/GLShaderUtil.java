package cc.antho.ae.renderer.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import cc.antho.common.log.Logger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GLShaderUtil {

	static void checkError(boolean program, int handle, int type) {

		int value;

		if (program) value = glGetProgrami(handle, type);
		else value = glGetShaderi(handle, type);

		if (value == GL_FALSE) {

			String error;

			if (program) error = glGetProgramInfoLog(handle);
			else error = glGetShaderInfoLog(handle);

			Logger.error(error);

		}

	}

}
