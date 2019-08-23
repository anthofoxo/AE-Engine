package cc.antho.ae.gui;

import java.util.HashMap;
import java.util.Map;

import cc.antho.ae.gui.layout.UILayoutManager;
import cc.antho.ae.input.InputManager;
import cc.antho.eventsystem.EventCallback;
import lombok.Getter;

@Deprecated
public class UIContainer extends UIComponent {

	@Getter private Map<UIComponent, Object> components = new HashMap<>();

	private UILayoutManager layout;

	public UIContainer(UILayoutManager layout) {

		setLayout(layout);

		getClickCallbacks().add(objs -> {

			InputManager input = ((UIMaster) objs[0]).getInput();

			for (UIComponent component : components.keySet()) {

				if (component.getBounds().contains(input.getRawCursorPosition().x, input.getRawCursorPosition().y)) {

					for (EventCallback callback : component.getClickCallbacks())
						callback.callback(objs);

				}

			}

		});

	}

	public void onKeyPress(int key) {

		for (UIComponent component : components.keySet())
			if (component != null) component.onKeyPress(key);

	}

	public void onKeyRelease(int key) {

		for (UIComponent component : components.keySet())
			if (component != null) component.onKeyRelease(key);

	}

	public void onKeyRepeat(int key) {

		for (UIComponent component : components.keySet())
			if (component != null) component.onKeyRepeat(key);

	}

	public void onChar(char key) {

		for (UIComponent component : components.keySet())
			if (component != null) component.onChar(key);

	}

	public void setFocused(boolean focused) {

		this.focused = false;

		for (UIComponent component : components.keySet())
			if (component != null) component.setFocused(false);

	}

	public void tick(UIMaster owner) {

		layout();

		for (UIComponent component : components.keySet())
			if (component != null) component.tick(owner);

	}

	public void render(UIMaster owner) {

		super.render(owner);

		for (UIComponent component : components.keySet())
			if (component != null) component.render(owner);

	}

	public void setLayout(UILayoutManager layout) {

		this.layout = layout;

	}

	public void layout() {

		if (layout != null) layout.layout(this);

	}

	public void add(UIComponent component, Object constraints) {

		components.put(component, constraints);

	}

}
