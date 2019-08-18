package cc.antho.ae.input;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class Control {

	@Getter private Map<Integer, Boolean> triggers = new HashMap<>();
	@Getter boolean last = false;
	@Getter boolean mouse;

	public Control(boolean mouse, int... triggers) {

		this.mouse = mouse;

		for (int i : triggers)
			this.triggers.put(i, false);

	}

	public boolean wasPressed() {

		return isDown() && !last;

	}

	public boolean isDown() {

		for (Boolean b : triggers.values())
			if (b) return true;

		return false;

	}

}
