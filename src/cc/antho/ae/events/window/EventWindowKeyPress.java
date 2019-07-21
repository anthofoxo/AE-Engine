package cc.antho.ae.events.window;

import cc.antho.ae.window.Window;
import cc.antho.eventsystem.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventWindowKeyPress extends Event {

	private final Window window;
	private final int key;

}
