package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;

import java.nio.IntBuffer;

import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.system.MemoryStack;

import cc.antho.ae.renderer.gl.GLFramebuffer;
import cc.antho.ae.renderer.gl.GLRenderer;
import cc.antho.ae.renderer.gl.GLTexture2D;
import lombok.Getter;
import lombok.Setter;

public class GuiOpenGLPanel extends GuiComponent {

	private Context context;
	private int nvgHandle;

	@Setter private Callback callback;

	private float lastWidth, lastHeight;

	public GuiOpenGLPanel(GLRenderer renderer) {

		context = new Context(renderer);

	}

	@Override
	public void renderComponent(GuiContext ctx) {

		if (callback == null) return;

		try (MemoryStack stack = stackPush()) {

			IntBuffer viewport = stack.mallocInt(4);
			glGetIntegerv(GL_VIEWPORT, viewport);

			context.update();
			callback.render(context);
			context.finish();

			glViewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));

		}

		try (MemoryStack stack = stackPush()) {

			if (lastWidth != w || lastHeight != h) {

				// TODO if (nvgHandle != 0) nvgDeleteImage(ctx.handle, nvgHandle);
				nvgHandle = nvglCreateImageFromHandle(ctx.handle, context.colorBuffer.getHandle(), (int) context.getWidth(), (int) context.getHeight(), NVG_IMAGE_FLIPY);

				lastWidth = w;
				lastHeight = h;

			}

			NVGPaint imagePaint = nvgImagePattern(ctx.handle, x, y, w, h, 0, nvgHandle, 1, NVGPaint.callocStack(stack));
			nvgBeginPath(ctx.handle);
			nvgRect(ctx.handle, x, y, w, h);
			nvgFillPaint(ctx.handle, imagePaint);
			nvgFill(ctx.handle);

		}

	}

	public class Context {

		private GLRenderer mainRenderer;
		@Getter private GLRenderer renderer;
		@Getter private float width, height;
		@Getter private float mouseX, mouseY;

		GLTexture2D colorBuffer;
		private GLTexture2D depthBuffer;

		public Context(GLRenderer mainRenderer) {

			this.mainRenderer = mainRenderer;

			GLFramebuffer framebuffer = mainRenderer.genFramebuffer();
			renderer = new GLRenderer(framebuffer);

			colorBuffer = renderer.genTexture2D();
			depthBuffer = renderer.genTexture2D();

			updateBuffers();

			framebuffer.attach(colorBuffer, GL_COLOR_ATTACHMENT0);
			framebuffer.attach(depthBuffer, GL_DEPTH_ATTACHMENT);
			framebuffer.validate();

		}

		public void finish() {

			mainRenderer.getDefaultFramebuffer().bindAll();

		}

		public void updateBuffers() {

			width = w;
			height = h;

			colorBuffer.bind(0);
			colorBuffer.storage((int) width, (int) height, GL_RGBA8, GL_RGBA, GL_UNSIGNED_BYTE, null);
			depthBuffer.bind(0);
			depthBuffer.storage((int) width, (int) height, GL_DEPTH_COMPONENT24, GL_DEPTH_COMPONENT, GL_FLOAT, null);

			renderer.getDefaultTexture2D().bind(0);

		}

		public void update() {

			updateBuffers();

			mouseX = GuiOpenGLPanel.this.mouseX;
			mouseY = GuiOpenGLPanel.this.mouseY;

			glViewport(0, 0, (int) width, (int) height);
			renderer.getDefaultFramebuffer().bindAll();

		}

	}

	public interface Callback {

		public abstract void render(Context context);

	}

}
