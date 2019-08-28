package cc.antho.ae.gameloop;

import cc.antho.ae.time.TimeProvider;
import lombok.Getter;

public abstract class GameLoopI {

	@Getter private final TimeProvider provider;
	@Getter private double delta = 0D, fixedDelta;
	@Getter private double time = 0D, fixedTime = 0D;
	private double accumulator = 0D;

	@Getter private boolean running = false;
	@Getter private long frames = 0L;
	@Getter private long ticks = 0L, fixedTicks = 0L;

	public GameLoopI(TimeProvider provider, double fixedDelta) {

		this.provider = provider;
		this.fixedDelta = fixedDelta;

	}

	public abstract void init();

	public abstract void fixedTick();

	public abstract void tick();

	public abstract void render();

	public abstract void destroy();

	/**
	 * currentState * alpha + previousState * ( 1.0 - alpha );
	 */
	public double getAlpha() {

		return accumulator / fixedDelta;

	}

	public void start() {

		if (running) return;
		running = true;

		init();

		double lastTime = provider.getTime();
		double newTime;

		while (running) {

			int currentSteps = 0;

			newTime = provider.getTime();
			delta = newTime - lastTime;
			lastTime = newTime;

			tick();
			time += delta;
			ticks++;

			accumulator += delta;

			while (accumulator >= fixedDelta) {

				fixedTick();
				accumulator -= fixedDelta;
				fixedTime += fixedDelta;
				fixedTicks++;

				currentSteps++;
				if (currentSteps >= 10) break;

			}

			render();
			frames++;

		}

		destroy();

	}

	public void stop() {

		running = false;

	}

}