package cc.antho.ae.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import cc.antho.abstractwindow.event.window.input.keyboard.key.EventWindowKeyboardKeyPressed;
import cc.antho.abstractwindow.event.window.input.keyboard.key.EventWindowKeyboardKeyReleased;
import cc.antho.abstractwindow.event.window.input.mouse.EventWindowMouseMoved;
import cc.antho.abstractwindow.event.window.input.mouse.button.EventWindowMouseButtonPressed;
import cc.antho.abstractwindow.event.window.input.mouse.button.EventWindowMouseButtonReleased;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventListener;
import lombok.Getter;

public class InputManager implements EventListener {

	@Getter private Vector2f lastRawCursorPosition = new Vector2f();
	@Getter private Vector2f rawCursorPosition = new Vector2f();
	@Getter private Vector2f diffCursorPosition = new Vector2f();

	@Getter private List<Control> controls = new ArrayList<>();

	@Getter private Control mouseLeft = new MouseControl(GLFW_MOUSE_BUTTON_1);
	@Getter private Control mouseRight = new MouseControl(GLFW_MOUSE_BUTTON_2);

	public InputManager() {

		controls.add(mouseLeft);
		controls.add(mouseRight);

	}

	public void update() {

		diffCursorPosition.set(rawCursorPosition).sub(lastRawCursorPosition);
		lastRawCursorPosition.set(rawCursorPosition);

		for (Control control : controls)
			control.last = control.isDown();

		glfwPollEvents();

	}

	@EventHandler
	public void onEventWindowKeyPress(EventWindowKeyboardKeyPressed event) {

		int key = event.key;

		for (Control control : controls)
			if (control.getTriggers().containsKey(key)) control.getTriggers().put(key, true);

	}

	@EventHandler
	public void onEventWindowKeyRelease(EventWindowKeyboardKeyReleased event) {

		int key = event.key;

		for (Control control : controls)
			if (control.getTriggers().containsKey(key)) control.getTriggers().put(key, false);

	}

	@EventHandler
	public void onEventWindowMousePress(EventWindowMouseButtonPressed event) {

		int button = event.button;

		for (Control control : controls)
			if (control.getTriggers().containsKey(button)) control.getTriggers().put(button, true);

	}

	@EventHandler
	public void onEventWindowMouseRelease(EventWindowMouseButtonReleased event) {

		int button = event.button;

		for (Control control : controls)
			if (control.getTriggers().containsKey(button)) control.getTriggers().put(button, false);

	}

	@EventHandler
	public void onEventWindowMouseMoved(EventWindowMouseMoved event) {

		rawCursorPosition.x = event.x;
		rawCursorPosition.y = event.y;

	}

}
