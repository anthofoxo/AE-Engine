package cc.antho.ae.state;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class State {

	@Getter @Setter(value = AccessLevel.PACKAGE) private StateManager manager;

	@Getter private final SceneManager scenes = new SceneManager();

	public abstract void init();

	public abstract void fixedTick();

	public abstract void tick();

	public abstract void render();

	public abstract void destroy();

}
