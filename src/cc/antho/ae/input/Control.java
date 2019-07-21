package cc.antho.ae.input;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public final class Control {

	@Getter private Map<Integer, Boolean> triggers = new HashMap<>();
	@Getter boolean last = false;

	public Control(int... triggers) {

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
