package cc.antho.ae.renderer.gl.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class Texture2D extends Texture {

	public Texture2D(int handle) {

		super(GL_TEXTURE_2D, handle);

	}

	public Texture2D() {

		this(glGenTextures());
		bind(0);

		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		clamp();

	}

	public void repeat() {

		glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_REPEAT);

	}

	public void clamp() {

		glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

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

	public void storage(int width, int height, ByteBuffer pixels) {

		glTexImage2D(target, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		glGenerateMipmap(target);

	}

	public void destroy() {

		glDeleteTextures(handle);

	}

}
