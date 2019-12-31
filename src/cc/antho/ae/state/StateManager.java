package cc.antho.ae.state;

import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class StateManager<Owner> {

	@Getter private final Owner owner;
	@Getter private State<Owner> state;

	public StateManager(Owner owner) {

		this.owner = owner;

	}

	public void set(State<Owner> state) {

		if (this.state != null) {

			this.state.destroy();
			this.state.setOwner(null);
			this.state.setManager(null);

		}

		Logger.info("Setting state to: " + state);
		this.state = state;

		if (this.state != null) {

			this.state.setOwner(owner);
			this.state.setManager(this);
			this.state.init();

		}

	}

	public void tick() {

		if (state != null) state.tick();

	}

	public void fixedTick() {

		if (state != null) state.fixedTick();

	}

	public void render() {

		if (state != null) state.render();

	}

}
