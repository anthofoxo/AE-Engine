package cc.antho.ae.scene;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class SceneManager {

	@Getter private List<Scene> scenes = new ArrayList<>();

	public void addScene(Scene scene) {

		if (scene == null) return;
		if (scenes.contains(scene)) return;

		Logger.info("Adding scene: " + scene);

		scenes.add(scene);
		scene.setManager(this);
		scene.init();

	}

	public void removeScene(Scene scene) {

		if (scene == null) return;
		if (!scenes.contains(scene)) return;

		Logger.info("Removing scene: " + scene);

		scene.destroy();
		scene.setManager(null);
		scenes.remove(scene);

	}

	public void fixedTick() {

		for (int i = scenes.size() - 1; i >= 0; i--)
			scenes.get(i).fixedTick();

	}

	public void tick() {

		for (int i = scenes.size() - 1; i >= 0; i--)
			scenes.get(i).tick();

	}

	public void render() {

		for (int i = scenes.size() - 1; i >= 0; i--)
			scenes.get(i).render();

	}

}
