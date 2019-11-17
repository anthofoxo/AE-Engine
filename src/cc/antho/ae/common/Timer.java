package cc.antho.ae.common;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Timer {

	@Setter private double start;
	@Setter private double left;
	@Setter private boolean repeats;
	private Runnable callback;

	public Timer(double timeout, boolean repeats, Runnable callback) {

		start = timeout;
		left = timeout;
		this.repeats = repeats;
		this.callback = callback;

	}

}
