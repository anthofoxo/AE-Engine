package cc.antho.ae.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityGroup {

	private List<Entity> entities = new ArrayList<>();

	public void addEntity(Entity entity) {

		if (entities.contains(entity)) return;

		entities.add(entity);

	}

	public void removeEntity(Entity entity) {

		if (!entities.contains(entity)) return;

		entities.remove(entity);

	}

	public List<Entity> getEntities() {

		return entities;

	}

	public void tick() {

		for (Entity e : entities)
			e.tick();

	}

	public void render() {

		for (Entity e : entities)
			e.render();

	}

}
