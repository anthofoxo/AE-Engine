package cc.antho.ae.events;

import cc.antho.eventsystem.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventUpsFpsUpdate extends Event {

	private final int ups, fps;

}
