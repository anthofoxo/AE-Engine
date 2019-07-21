package cc.antho.ae.gui;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import cc.antho.ae.common.Util;
import cc.antho.ae.events.window.EventWindowKeyChar;
import cc.antho.ae.events.window.EventWindowKeyPress;
import cc.antho.ae.events.window.EventWindowKeyRelease;
import cc.antho.ae.events.window.EventWindowKeyRepeat;
import cc.antho.ae.events.window.EventWindowMousePress;
import cc.antho.ae.input.InputManager;
import cc.antho.eventsystem.EventCallback;
import cc.antho.eventsystem.EventDispatcher;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventListener;
import lombok.AccessLevel;
import lombok.Getter;

public class UIMaster implements EventListener {

	@Getter private long handle;
	@Getter(value = AccessLevel.PACKAGE) private float width, height;
	@Getter(value = AccessLevel.PACKAGE) private double delta;
	@Getter(value = AccessLevel.PACKAGE) private InputManager input;

	private final List<UIComponent> elements = new ArrayList<>();

	private Map<String, ByteBuffer> fonts = new HashMap<>();

	public UIMaster() {

		handle = nvgCreate(0/* NVG_ANTIALIAS */);

		EventDispatcher.registerEventListener(this);

	}

	@EventHandler
	public void onWindowKeyRepeatEvent(EventWindowKeyRepeat event) {

		for (int i = 0; i < elements.size(); i++)
			elements.get(i).onKeyRepeat(event.getKey());

	}

	@EventHandler
	public void onWindowKeyPressEvent(EventWindowKeyPress event) {

		for (int i = 0; i < elements.size(); i++)
			elements.get(i).onKeyPress(event.getKey());

	}

	@EventHandler
	public void onWindowKeyReleaseEvent(EventWindowKeyRelease event) {

		for (int i = 0; i < elements.size(); i++)
			elements.get(i).onKeyRelease(event.getKey());

	}

	@EventHandler
	public void onWindowKeyCharEvent(EventWindowKeyChar event) {

		for (int i = 0; i < elements.size(); i++)
			elements.get(i).onChar(event.getKey());

	}

	@EventHandler
	public void onWindowMousePressEvent(EventWindowMousePress event) {

		if (event.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT) return;

		for (int i = 0; i < elements.size(); i++) {

			UIComponent component = elements.get(i);

			if (component.getBounds().contains(input.getRawCursorPosition().x, input.getRawCursorPosition().y)) {

				for (EventCallback callback : component.getClickCallbacks())
					callback.callback(UIMaster.this);

			}

		}

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

	public void add(UIComponent... elements) {

		for (UIComponent element : elements)
			add(element);

	}

	public void remove(UIComponent... elements) {

		for (UIComponent element : elements)
			remove(element);

	}

	public void add(UIComponent element) {

		if (element == null) return;
		if (elements.contains(element)) return;

		elements.add(element);

	}

	public void remove(UIComponent element) {

		if (element == null) return;
		if (!elements.contains(element)) return;

		elements.remove(element);

	}

	public void tick(InputManager input, double delta) {

		this.input = input;
		this.delta = delta;

		for (int i = 0; i < elements.size(); i++)
			if (elements.get(i).isVisible()) elements.get(i).tick(this);

	}

	public void render(float width, float height) {

		this.width = width;
		this.height = height;

		nvgBeginFrame(handle, width, height, 1);

		for (int i = 0; i < elements.size(); i++)
			if (elements.get(i).isVisible()) elements.get(i).render(this);

		nvgEndFrame(handle);

	}

	public void destroy() {

		EventDispatcher.deregisterEventListener(this);

		nvgDelete(handle);

	}

	public void clearFocus() {

		for (int i = 0; i < elements.size(); i++)
			elements.get(i).setFocused(false);

	}

}
