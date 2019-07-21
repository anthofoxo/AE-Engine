package cc.antho.ae.common;

import cc.antho.eventsystem.EventCallback;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Timer {

	@Setter private double start;
	@Setter private double left;
	@Setter private boolean repeats;
	private EventCallback callback;

	public Timer(double timeout, boolean repeats, EventCallback callback) {

		start = timeout;
		left = timeout;
		this.repeats = repeats;
		this.callback = callback;

	}

}
