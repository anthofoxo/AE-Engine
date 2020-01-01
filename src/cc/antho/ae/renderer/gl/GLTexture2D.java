package cc.antho.ae.renderer.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import cc.antho.common.Destroyable;

public class GLTexture2D extends GLTexture implements Destroyable {

	GLTexture2D(int handle) {

		super(GL_TEXTURE_2D, handle);

	}

	GLTexture2D() {

		this(glGenTextures());
		bind(0);

		minLinearMipLinear();
		magLinear();
		clamp();

	}

	public void maxLevel(int level) {

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, level);

	}

	public void repeat() {

		glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(target, GL_TEXTURE_WRAP_R, GL_REPEAT);

	}

	public void minNear() {

		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

	}

	public void minLinear() {

		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

	}

	public void minLinearMipLinear() {

		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);

	}

	public void minLinearMipNear() {

		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);

	}

	public void minNearMipLinear() {

		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);

	}

	public void minNearMipNear() {

		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);

	}

	public void magNear() {

		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

	}

	public void magLinear() {

		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	}

	public void clamp() {

		glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(target, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

	}

	public void bind(int index) {

		glActiveTexture(GL_TEXTURE0 + index);
		glBindTexture(target, handle);

	}

	public void storage(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

		for (int y = height - 1; y >= 0; y--)
			for (int x = 0; x < width; x++) {

				int pixel = image.getRGB(x, y);
				int alpha = (pixel & 0xFF000000) >> 24;
				int red = (pixel & 0x00FF0000) >> 16;
				int green = (pixel & 0x0000FF00) >> 8;
				int blue = pixel & 0x000000FF;

				pixels.put((byte) red);
				pixels.put((byte) green);
				pixels.put((byte) blue);
				pixels.put((byte) alpha);

			}

		pixels.flip();

		storage(width, height, pixels);

	}

	public void storage(int width, int height, int internalFormat, int format, int type, ByteBuffer pixels) {

		glTexImage2D(target, 0, internalFormat, width, height, 0, format, type, pixels);
		glGenerateMipmap(target);

	}

	public void storage(int width, int height, ByteBuffer pixels) {

		glTexImage2D(target, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		glGenerateMipmap(target);

	}

	public void destroy() {

		glDeleteTextures(handle);

	}

}
