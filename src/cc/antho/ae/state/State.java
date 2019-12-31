package cc.antho.ae.state;

import cc.antho.ae.common.Destroyable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class State<Owner> implements Destroyable {

	@Getter @Setter(value = AccessLevel.PACKAGE) private Owner owner;
	@Getter @Setter(value = AccessLevel.PACKAGE) private StateManager<Owner> manager;

	public abstract void init();

	public abstract void tick();

	public abstract void fixedTick();

	public abstract void render();

	public abstract void destroy();

}
