package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import cc.antho.abstractwindow.event.window.input.mouse.EventWindowMouseMoved;
import cc.antho.ae.common.Destroyable;
import cc.antho.ae.common.Util;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventListener;
import lombok.Getter;

public class GuiContext implements Destroyable, EventListener {

	long handle;
	private ByteBuffer fontBytes;
	@Getter private String defaultFont = "default";
	public GuiComponent container = new GuiEmpty();

	public void add(GuiComponent component) {

		container.add(component);

	}

	public void remove(GuiComponent component) {

		container.remove(component);

	}

	public void clear() {

		container.clear();
		container.layout = null;

	}

	public GuiContext() {

		handle = nvgCreate(0);
		nvgGlobalCompositeBlendFunc(handle, NVG_SRC_ALPHA, NVG_ONE_MINUS_SRC_ALPHA);

		try {

			byte[] rawFontBytes = Util.loadByteArray(Util.getStream("/font/Roboto-Regular.ttf"));
			fontBytes = BufferUtils.createByteBuffer(rawFontBytes.length);
			fontBytes.put(rawFontBytes);
			fontBytes.flip();
			nvgCreateFontMem(handle, "default", fontBytes, 0);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public void render(int windowWidth, int windowHeight) {

		glViewport(0, 0, windowWidth, windowHeight);
		nvgBeginFrame(handle, windowWidth, windowHeight, 1f);

		container.x = 0;
		container.y = 0;
		container.w = windowWidth;
		container.h = windowHeight;
		container.render(this);

		nvgEndFrame(handle);

	}

	public void destroy() {

		nvgDelete(handle);

	}

	@EventHandler
	private void onMouseMove(EventWindowMouseMoved event) {

		container.onMouseMove(event);

	}

}
