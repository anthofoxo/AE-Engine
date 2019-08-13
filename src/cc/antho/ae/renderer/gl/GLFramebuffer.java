package cc.antho.ae.renderer.gl;

import static org.lwjgl.opengl.GL30.*;

import lombok.Getter;

public final class GLFramebuffer implements Destroyable {

	@Getter private int handle;

	GLFramebuffer(int id) {

		this.handle = id;

	}

	GLFramebuffer() {

		handle = glGenFramebuffers();
		bindAll();

	}

	public void bindAll() {

		glBindFramebuffer(GL_FRAMEBUFFER, handle);

	}

	public void bindRead() {

		glBindFramebuffer(GL_READ_FRAMEBUFFER, handle);

	}

	public void bindDraw() {

		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, handle);

	}

	public void attach(GLTexture2D texture, int attachment) {

		glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, texture.getTarget(), texture.getHandle(), 0);

	}

	public void attach(GLRenderbuffer renderbuffer, int attachment) {

		glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderbuffer.getHandle());

	}

	public void destroy() {

		glDeleteFramebuffers(handle);

	}

	public void validate() {

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) throw new IllegalStateException("Incomplete framebuffer");

	}

	public void blit(GLFramebuffer dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {

		bindRead();
		glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);

	}

}
