package cc.antho.ae.gameloop;

import cc.antho.ae.time.TimeProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class GameLoop {

	@Getter @Setter private double delta = 1D / 250D;
	@Getter private final TimeProvider provider;

	@Getter private boolean running = false;

	@Getter private double globalTime;
	@Getter private long tickCount = 0;
	@Getter private long frameCount = 0;

	private double accumulator = 0D;

	/**
	 * currentState * alpha + previousState * ( 1.0 - alpha );
	 */
	public double getAlpha() {

		return accumulator / delta;

	}

	public void start() {

		if (running) return;
		running = true;

		double currentTime = provider.getTime();

		init();

		while (running) {

			int currentSteps = 0;

			double newTime = provider.getTime();
			double frameTime = newTime - currentTime;
			currentTime = newTime;

			accumulator += frameTime;

			while (accumulator >= delta) {

				tick();
				accumulator -= delta;
				globalTime += delta;
				tickCount++;

				currentSteps++;

				if (currentSteps >= 10) break;

			}

			render();
			frameCount++;

		}

		destroy();

	}

	public void stop() {

		running = false;

	}

	public abstract void init();

	public abstract void tick();

	public abstract void render();

	public abstract void destroy();

}