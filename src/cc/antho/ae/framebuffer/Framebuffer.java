package cc.antho.ae.framebuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import cc.antho.ae.renderer.Destroyable;
import cc.antho.ae.renderer.gl.texture.Texture2D;
import lombok.Getter;

public final class Framebuffer implements Destroyable {

	public static Framebuffer DEFAULT = new Framebuffer(0);

	private Framebuffer(int id) {

		this.handle = id;

	}

	@Getter private int handle;

	public Framebuffer() {

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

	public void attach(Texture2D texture, int attachment) {

		glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, texture.getHandle(), 0);

	}

	public void attach(Renderbuffer renderbuffer, int attachment) {

		glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderbuffer.getHandle());

	}

	public void destroy() {

		glDeleteFramebuffers(handle);

	}

	public void validate() {

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) throw new IllegalStateException("Incomplete framebuffer");

	}

	public void blit(Framebuffer dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {

		bindRead();
		(dest == null ? DEFAULT : dest).bindDraw();

		glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);

	}

}
