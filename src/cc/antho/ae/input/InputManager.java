package cc.antho.ae.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import cc.antho.ae.events.window.EventWindowKeyPress;
import cc.antho.ae.events.window.EventWindowKeyRelease;
import cc.antho.ae.events.window.EventWindowMouseMoved;
import cc.antho.ae.events.window.EventWindowMousePress;
import cc.antho.ae.events.window.EventWindowMouseRelease;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventListener;
import lombok.Getter;

public class InputManager implements EventListener {

	@Getter private Vector2f lastRawCursorPosition = new Vector2f();
	@Getter private Vector2f rawCursorPosition = new Vector2f();
	@Getter private Vector2f diffCursorPosition = new Vector2f();

	@Getter private List<Control> controls = new ArrayList<>();

	public void update() {

		diffCursorPosition.set(rawCursorPosition).sub(lastRawCursorPosition);
		lastRawCursorPosition.set(rawCursorPosition);

		for (Control control : controls)
			control.last = control.isDown();

		glfwPollEvents();

	}

	@EventHandler
	public void onEventWindowKeyPress(EventWindowKeyPress event) {

		int key = event.getKey();

		for (Control control : controls)
			if (control.getTriggers().containsKey(key)) control.getTriggers().put(key, true);

	}

	@EventHandler
	public void onEventWindowKeyRelease(EventWindowKeyRelease event) {

		int key = event.getKey();

		for (Control control : controls)
			if (control.getTriggers().containsKey(key)) control.getTriggers().put(key, false);

	}

	@EventHandler
	public void onEventWindowMousePress(EventWindowMousePress event) {

		int button = event.getButton();

		for (Control control : controls)
			if (control.getTriggers().containsKey(button)) control.getTriggers().put(button, true);

	}

	@EventHandler
	public void onEventWindowMouseRelease(EventWindowMouseRelease event) {

		int button = event.getButton();

		for (Control control : controls)
			if (control.getTriggers().containsKey(button)) control.getTriggers().put(button, false);

	}

	@EventHandler
	public void onEventWindowMouseMoved(EventWindowMouseMoved event) {

		rawCursorPosition.x = event.getX();
		rawCursorPosition.y = event.getY();

	}

}
