package cc.antho.ae.gui;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.events.window.EventWindowMouseMoved;
import cc.antho.ae.events.window.EventWindowMousePress;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public final class UIContainer extends UIComponent {

	@Getter @Setter private UILayoutManager layout;

	@Getter(value = AccessLevel.PACKAGE) private final List<UIComponent> componentKeys = new ArrayList<>();
	@Getter(value = AccessLevel.PACKAGE) private final List<Object> componentValues = new ArrayList<>();

	public void add(UIComponent component, Object constraints) {

		if (componentKeys.contains(component)) return;

		component.context(context);
		componentKeys.add(component);
		componentValues.add(constraints);

	}

	public void remove(UIComponent component) {

		if (!componentKeys.contains(component)) return;

		component.context(null);

		int index = componentKeys.indexOf(component);

		componentKeys.remove(index);
		componentValues.remove(index);

	}

	protected void onEventWindowMouseMoved(EventWindowMouseMoved event) {

		for (UIComponent component : componentKeys)
			component._onEventWindowMouseMoved(event);

	}

	protected void onEventWindowMousePress(EventWindowMousePress event) {

		for (UIComponent component : componentKeys)
			component._onEventWindowMousePress(event);

	}

	void context(UIContext context) {

		super.context(context);

		for (UIComponent component : componentKeys)
			component.context(context);

	}

	public void removeAll() {

		for (UIComponent component : componentKeys)
			component.context(null);

		componentKeys.clear();
		componentValues.clear();

	}

	public void render() {

		if (layout != null) layout.layout(this);

		for (UIComponent component : componentKeys)
			component._render();

	}

	public List<UIComponent> contents() {

		return componentKeys;

	}

}
