package cc.antho.ae.engine;

import cc.antho.ae.time.HiResTimeProvider;
import cc.antho.ae.time.TimeProvider;
import cc.antho.eventsystem.EventLayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AEEngineStartProps {

	private TimeProvider provider;
	private EventLayer layer;
	private int sources;

	public AEEngineStartProps(TimeProvider provider, EventLayer layer, int sources) {

		this.provider = provider == null ? new HiResTimeProvider() : provider;
		this.layer = layer == null ? new EventLayer() : layer;
		this.sources = sources <= 0 ? 16 : sources;

	}

}
