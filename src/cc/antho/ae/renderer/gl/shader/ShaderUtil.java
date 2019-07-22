package cc.antho.ae.renderer.gl.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;

import cc.antho.ae.common.Util;
import cc.antho.ae.log.Logger;

public final class ShaderUtil {

	static void checkError(boolean program, int handle, int type) {

		int value;

		if (program) value = glGetProgrami(handle, type);
		else value = glGetShaderi(handle, type);

		if (value == GL_FALSE) {

			String error;

			if (program) error = glGetProgramInfoLog(handle);
			else error = glGetShaderInfoLog(handle);

			Util.openErrorDialog(new RuntimeException(error));
			Logger.error(error);

		}

	}

	public static ShaderProgram createProgramDirect(String vss, String fss) {

		ShaderSource vs = new ShaderSource(vss, GL_VERTEX_SHADER);
		ShaderSource fs = new ShaderSource(fss, GL_FRAGMENT_SHADER);

		ShaderProgram program = new ShaderProgram(vs, fs);

		vs.destroy();
		fs.destroy();

		return program;

	}

	public static ShaderProgram createProgram(String vss, String fss) throws IOException {

		ShaderSource vs = new ShaderSource(Util.loadResourceToString(vss), GL_VERTEX_SHADER);
		ShaderSource fs = new ShaderSource(Util.loadResourceToString(fss), GL_FRAGMENT_SHADER);

		ShaderProgram program = new ShaderProgram(vs, fs);

		vs.destroy();
		fs.destroy();

		return program;

	}

}
