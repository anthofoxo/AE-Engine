package cc.antho.ae.state;

import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class StateManager {

	@Getter private State scene;

	public void setState(State scene) {

		if (this.scene != null) {

			this.scene.destroy();
			this.scene.setManager(null);

		}

		Logger.info("Setting state to: " + scene);
		this.scene = scene;

		if (this.scene != null) {

			this.scene.setManager(this);
			this.scene.init();

		}

	}

	public void tick() {

		if (scene != null) scene.tick();

	}

	public void render() {

		if (scene != null) scene.render();

	}

}
