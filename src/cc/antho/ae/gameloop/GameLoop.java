package cc.antho.ae.gameloop;

import cc.antho.ae.time.TimeProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Deprecated
public abstract class GameLoop {

	@Getter protected final TimeProvider provider;
	@Getter protected double delta, time;
	@Getter protected boolean running;
	@Getter protected long frames, ticks;

	public abstract void start();

	public void stop() {

		running = false;

	}

	public abstract void init();

	public abstract void tick();

	public abstract void render();

	public abstract void destroy();

}
