package cc.antho.ae.renderer.framebuffer;

import static org.lwjgl.opengl.GL30.*;

import cc.antho.ae.common.Bindable;
import cc.antho.ae.common.Destroyable;
import lombok.Getter;

public final class GLRenderbuffer implements Bindable, Destroyable {

	@Getter private int handle;

	GLRenderbuffer() {

		handle = glGenRenderbuffers();
		bind();

	}

	public void storage(int internalFormat, int width, int height) {

		bind();
		glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);

	}

	public void storage(int samples, int internalFormat, int width, int height) {

		bind();
		glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, internalFormat, width, height);

	}

	public void bind() {

		glBindRenderbuffer(GL_RENDERBUFFER, handle);

	}

	public void destroy() {

		glDeleteRenderbuffers(handle);

	}

}
