package cc.antho.ae.gameloop;

import cc.antho.ae.time.TimeProvider;

@Deprecated
public abstract class GameLoopFixed extends GameLoop {

	private double accumulator = 0D;

	public GameLoopFixed(TimeProvider provider) {

		super(provider);
		delta = 1D / 250D;

	}

	/**
	 * currentState * alpha + previousState * ( 1.0 - alpha );
	 */
	public double getAlpha() {

		return accumulator / delta;

	}

	public void start() {

		if (running) return;
		running = true;

		double lastTime = provider.getTime();
		double newTime;

		init();

		while (running) {

			int currentSteps = 0;

			newTime = provider.getTime();
			double frameTime = newTime - lastTime;
			lastTime = newTime;

			accumulator += frameTime;

			while (accumulator >= delta) {

				tick();
				accumulator -= delta;
				time += delta;
				ticks++;

				currentSteps++;

				if (currentSteps >= 10) break;

			}

			render();
			frames++;

		}

		destroy();

	}

}