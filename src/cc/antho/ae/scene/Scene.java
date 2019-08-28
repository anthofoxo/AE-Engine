package cc.antho.ae.scene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Scene {

	@Getter @Setter(value = AccessLevel.PACKAGE) private SceneManager manager;

	public abstract void init();

	public abstract void fixedTick();

	public abstract void tick();

	public abstract void render();

	public abstract void destroy();

}
