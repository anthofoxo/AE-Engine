package cc.antho.ae.guii;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;

import cc.antho.ae.common.Util;
import cc.antho.ae.events.window.EventWindowMouseMoved;
import cc.antho.ae.events.window.EventWindowMousePress;
import cc.antho.ae.renderer.color.Color;
import cc.antho.ae.renderer.gl.Destroyable;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventLayer;
import cc.antho.eventsystem.EventListener;
import lombok.Getter;

public final class UIContext implements Destroyable, EventListener {

	public final long handle;
	@Getter private UIContainer container;
	private Map<String, ByteBuffer> fonts = new HashMap<>();
	private EventLayer layer;

	public UIContext(EventLayer layer, boolean antialias) {

		handle = nvgCreate(antialias ? NVG_ANTIALIAS : 0);
		this.layer = layer;
		layer.registerEventListener(this);

		resetContainer();

	}

	public NVGColor color(Color color) {

		NVGColor nvgColor = NVGColor.create();
		nvgColor.r(color.r);
		nvgColor.g(color.g);
		nvgColor.b(color.b);
		nvgColor.a(color.a);

		return nvgColor;
	}

	public void setDefaultFont(String file) throws FileNotFoundException, IOException {

		registerFont("default", file);

	}

	public void registerFont(String name, String file) throws FileNotFoundException, IOException {

		byte[] bytes = Util.loadByteArray(Util.getStream(file, true));
		ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
		buffer.put(bytes);
		buffer.flip();

		nvgCreateFontMem(handle, name, buffer, 0);

		fonts.put(name, buffer);

	}

	public void render(Vector2f size, float ratio) {

		render(size.x, size.y, ratio);

	}

	public void render(float width, float height, float ratio) {

		nvgBeginFrame(handle, width, height, ratio);
		container.getSize().set(width, height);
		container._render();
		nvgEndFrame(handle);

	}

	@EventHandler
	private void onEventWindowMouseMoved(EventWindowMouseMoved event) {

		container._onEventWindowMouseMoved(event);

	}

	@EventHandler
	private void onEventWindowMousePress(EventWindowMousePress event) {

		container._onEventWindowMousePress(event);

	}

	public void destroy() {

		layer.deregisterEventListener(this);

		nvgDelete(handle);

		container.removeAll();
		fonts.clear();

	}

	public void resetContainer() {

		if (container != null) {

			container.removeAll();
			container.context(null);

		}

		container = new UIContainer(null);
		container.context(this);

	}

}
