package cc.antho.ae.gameloop;

import cc.antho.ae.time.TimeProvider;

@Deprecated
public abstract class GameLoopVariable extends GameLoop {

	public GameLoopVariable(TimeProvider provider) {

		super(provider);

	}

	public void start() {

		if (running) return;
		running = true;

		double lastTime = provider.getTime();
		double newTime;

		init();

		while (running) {

			newTime = provider.getTime();
			delta = newTime - lastTime;
			lastTime = newTime;

			tick();

			time += delta;
			ticks++;

			render();
			frames++;

		}

		destroy();

	}

}