package cc.antho.ae.renderer.framebuffer;

import static org.lwjgl.opengl.GL30.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cc.antho.ae.renderer.gl.GLTexture2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

public final class GLFramebuffer {

	@AllArgsConstructor
	private static final class AttachmentStorage {

		private int attachment;
		private ResizePolicy policy;

	}

	@Getter private final int handle;

	private Map<GLRenderbuffer, AttachmentStorage> renderbuffers = new HashMap<>();
	private Map<GLTexture2D, AttachmentStorage> textures = new HashMap<>();

	public GLFramebuffer(int handle) {

		this.handle = handle;

	}

	public GLFramebuffer() {

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

	public void attach(GLTexture2D texture, int attachment, ResizePolicy policy) {

		if (policy == null) policy = ResizePolicy.POLICY_IGNORE;
		textures.put(texture, new AttachmentStorage(attachment, policy));
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, texture.getTarget(), texture.getHandle(), 0);

	}

	public void attach(GLRenderbuffer renderbuffer, int attachment, ResizePolicy policy) {

		if (policy == null) policy = ResizePolicy.POLICY_IGNORE;
		renderbuffers.put(renderbuffer, new AttachmentStorage(attachment, policy));
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderbuffer.getHandle());

	}

	public void detach(GLTexture2D texture) {

		AttachmentStorage storage = textures.remove(texture);
		if (storage == null) return;
		glFramebufferTexture2D(GL_FRAMEBUFFER, storage.attachment, texture.getTarget(), 0, 0);

	}

	public void detach(GLRenderbuffer renderbuffer) {

		AttachmentStorage storage = renderbuffers.remove(renderbuffer);
		if (storage == null) return;
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, storage.attachment, GL_RENDERBUFFER, 0);

	}

	public void destroy() {

		glDeleteFramebuffers(handle);

	}

	public void validate() {

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) throw new IllegalStateException("Incomplete framebuffer");

	}

	public void blit(GLFramebuffer dest, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {

		this.bindRead();
		dest.bindDraw();
		glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);

	}

}
