package cc.antho.ae.engine;

import java.util.ArrayList;
import java.util.List;

import cc.antho.abstractwindow.Window;
import cc.antho.ae.audio.AudioManager;
import cc.antho.ae.common.Timer;
import cc.antho.ae.gameloop.FrameCounter;
import cc.antho.ae.gameloop.GameLoopI;
import cc.antho.ae.input.InputManager;
import cc.antho.ae.log.Logger;
import cc.antho.ae.renderer.gl.GLRenderer;
import cc.antho.ae.state.StateManager;
import cc.antho.eventsystem.EventLayer;
import cc.antho.eventsystem.EventListener;
import lombok.Getter;
import lombok.Setter;

public final class AeEngine extends GameLoopI implements EventListener {

	@Getter private StateManager<AeEngine> manager = new StateManager<>(this);
	@Getter private FrameCounter counter = new FrameCounter();

	@Getter @Setter private Window window;

	private List<Runnable> deferred = new ArrayList<>();
	private List<Timer> timers = new ArrayList<>();

	@Getter private InputManager inputManager;
	@Getter private AudioManager audioManager;
	@Getter private EventLayer layer;
	@Getter private GLRenderer renderer = new GLRenderer();

	private AeEngineStartProps props;

	public AeEngine(AeEngineStartProps props) {

		super(props.getProvider(), 1D / 100D);

		this.props = props;
		layer = props.getLayer();

	}

	public void init() {

		inputManager = new InputManager();
		audioManager = new AudioManager(props.getSources());

		Logger.info("Starting GameLoop");
		counter.mark();

		layer.registerEventListener(this);
		layer.registerEventListener(inputManager);

	}

	public void beginTimer(Timer timer) {

		if (timers.contains(timer)) return;
		timers.add(timer);

	}

	public void defer(Runnable callback) {

		deferred.add(callback);

	}

	public void cancelTimers() {

		timers.clear();

	}

	public void fixedTick() {

		manager.fixedTick();

	}

	public void tick() {

		while (!deferred.isEmpty())
			deferred.remove(0).run();

		for (int i = timers.size() - 1; i >= 0; i--) {

			Timer timer = timers.get(i);
			timer.setLeft(timer.getLeft() - getDelta());

			if (timer.getLeft() <= 0D) {

				timer.getCallback().run();

				if (timer.isRepeats()) timer.setLeft(timer.getLeft() + timer.getStart());
				else timers.remove(i);

			}

		}

		inputManager.update();

		manager.tick();

		counter.addTick();
		counter.check(layer);

	}

	public void render() {

		manager.render();

		window.swapBuffers();
		counter.addFrame();

	}

	public void destroy() {

		layer.deregisterEventListener(this);
		layer.deregisterEventListener(inputManager);

		manager.set(null);

		window.destroy();

		audioManager.destroy();

		Logger.info("Stopping GameLoop");

	}

}
