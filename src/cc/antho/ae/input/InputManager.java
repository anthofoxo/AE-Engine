package cc.antho.ae.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	@Getter private Set<Integer> buttons = new HashSet<>();
	@Getter private Set<Integer> lastButtons = new HashSet<>();

	@Getter private Vector2f lastRawCursorPosition = new Vector2f();
	@Getter private Vector2f rawCursorPosition = new Vector2f();
	@Getter private Vector2f diffCursorPosition = new Vector2f();

	@Getter private List<Control> controls = new ArrayList<>();

	public void update() {

		diffCursorPosition.set(rawCursorPosition).sub(lastRawCursorPosition);
		lastRawCursorPosition.set(rawCursorPosition);
		lastButtons.clear();
		lastButtons.addAll(buttons);

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

		buttons.add(event.getButton());

	}

	@EventHandler
	public void onEventWindowMouseRelease(EventWindowMouseRelease event) {

		buttons.remove(event.getButton());

	}

	@EventHandler
	public void onEventWindowMouseMoved(EventWindowMouseMoved event) {

		rawCursorPosition.x = event.getX();
		rawCursorPosition.y = event.getY();

	}

}
