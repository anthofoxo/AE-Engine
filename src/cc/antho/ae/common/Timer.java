package cc.antho.ae.common;

import cc.antho.eventsystem.EventCallback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Timer {

	@Setter private double timeout;
	private EventCallback callback;

}
