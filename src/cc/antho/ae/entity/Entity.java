package cc.antho.ae.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Entity {

	private final Map<Class<? extends Component>, Component> components = new HashMap<>();
	@Getter @Setter private String tag = "untagged";
	@Getter @Setter private boolean enabled = true;

	public Entity() {

		addComponent(new Transform());

	}

	public void addComponent(Component component) {

		Class<? extends Component> clazz = component.getClass();

		if (components.containsKey(clazz)) return;

		component.entity = this;
		components.put(clazz, component);

		component.init();

	}

	public <T extends Component> T getComponent(Class<T> clazz) {

		return clazz.cast(components.getOrDefault(clazz, null));

	}

	public void removeComponent(Class<? extends Component> clazz) {

		Component c = components.remove(clazz);

		if (c != null) {

			c.destroy();
			c.entity = null;

		}

	}

	public void tick() {

		for (Component c : components.values())
			c.tick();

	}

	public void render() {

		for (Component c : components.values())
			c.render();

	}

}
