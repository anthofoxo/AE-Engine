package cc.antho.ae.renderer.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;

import cc.antho.ae.common.Util;
import cc.antho.ae.renderer.color.Color;

public final class GLRenderer {

	public GLRenderbuffer genRenderbuffer() {

		return new GLRenderbuffer();

	}

	public GLFramebuffer genFramebuffer() {

		return new GLFramebuffer();

	}

	public GLFramebuffer getDefaultFramebuffer() {

		return new GLFramebuffer(0);

	}

	public GLTexture2D genTexture2D() {

		return new GLTexture2D();

	}

	public GLQuery genQuery(int type) {

		return new GLQuery(type);

	}

	public GLShaderSource genShaderSource(String source, int type) {

		return new GLShaderSource(source, type);

	}

	public GLShaderProgram genShaderProgram(GLShaderSource... sources) {

		return new GLShaderProgram(sources);

	}

	public GLShaderProgram createProgramDirect(String vss, String fss) {

		GLShaderSource vs = genShaderSource(vss, GL_VERTEX_SHADER);
		GLShaderSource fs = genShaderSource(fss, GL_FRAGMENT_SHADER);

		GLShaderProgram program = genShaderProgram(vs, fs);

		vs.destroy();
		fs.destroy();

		return program;

	}

	public GLShaderProgram createProgram(String vss, String fss) throws IOException {

		return createProgramDirect(Util.loadResourceToString(vss), Util.loadResourceToString(fss));

	}

	////

	public void clearColor(Color color) {

		clearColor(color.r, color.g, color.b, color.a);

	}

	public void colorMask(boolean flag) {

		colorMask(flag, flag, flag, flag);

	}

	public void clearColor(float r, float g, float b, float a) {

		glClearColor(r, g, b, a);

	}

	public void clear(int mask) {

		glClear(mask);

	}

	public void depthMask(boolean flag) {

		glDepthMask(flag);

	}

	public void colorMask(boolean r, boolean g, boolean b, boolean a) {

		glColorMask(r, g, b, a);

	}

}
