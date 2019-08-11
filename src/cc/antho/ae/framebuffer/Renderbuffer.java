package cc.antho.ae.framebuffer;

import static org.lwjgl.opengl.GL30.*;

import cc.antho.ae.renderer.Destroyable;
import lombok.Getter;

public final class Renderbuffer implements Destroyable {

	public static Renderbuffer DEFAULT = new Renderbuffer(0);

	private Renderbuffer(int id) {

		this.handle = id;

	}

	@Getter private int handle;

	public Renderbuffer() {

		handle = glGenRenderbuffers();
		bind();

	}

	public void storage(int internalFormat, int width, int height) {

		bind();
		glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);

	}

	public void storageMultisample(int samples, int internalFormat, int width, int height) {

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
