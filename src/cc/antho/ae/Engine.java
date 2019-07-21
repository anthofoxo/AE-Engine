package cc.antho.ae;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.audio.AudioManager;
import cc.antho.ae.common.Timer;
import cc.antho.ae.events.window.EventWindowClosed;
import cc.antho.ae.gameloop.FrameCounter;
import cc.antho.ae.gameloop.GameLoopVariable;
import cc.antho.ae.gui.UIMaster;
import cc.antho.ae.input.InputManager;
import cc.antho.ae.log.Logger;
import cc.antho.ae.state.StateManager;
import cc.antho.ae.time.TimeProvider;
import cc.antho.ae.window.Window;
import cc.antho.eventsystem.EventCallback;
import cc.antho.eventsystem.EventDispatcher;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventListener;
import lombok.Getter;
import lombok.Setter;

public final class Engine extends GameLoopVariable implements EventListener {

	@Getter private StateManager manager = new StateManager();
	private FrameCounter counter = new FrameCounter();

	@Getter @Setter private Window window;

	private List<EventCallback> deferred = new ArrayList<>();
	private List<Timer> timers = new ArrayList<>();

	@Getter private UIMaster uiMaster;
	@Getter private InputManager inputManager;
	@Getter private AudioManager audioManager;

	@EventHandler
	public void onEventWindowClosed(EventWindowClosed event) {

		if (event.getWindow().equals(window)) stop();

	}

	public Engine(TimeProvider provider) {

		super(provider);

	}

	public void init() {

		uiMaster = new UIMaster();
		inputManager = new InputManager();
		audioManager = new AudioManager(16);

		Logger.info("Starting GameLoop");
		counter.mark();

		EventDispatcher.registerEventListener(this);
		EventDispatcher.registerEventListener(inputManager);

	}

	public void beginTimer(Timer timer) {

		if (timers.contains(timer)) return;
		timers.add(timer);

	}

	public void defer(EventCallback callback) {

		deferred.add(callback);

	}

	public void cancelTimers() {

		timers.clear();

	}

	public void tick() {

		while (!deferred.isEmpty())
			deferred.remove(0).callback();

		for (int i = timers.size() - 1; i >= 0; i--) {

			Timer timer = timers.get(i);
			timer.setTimeout(timer.getTimeout() - getDelta());

			if (timer.getTimeout() <= 0D) {

				timer.getCallback().callback();
				timers.remove(i);

			}

		}

		inputManager.update();

		manager.tick();

		uiMaster.tick(inputManager, getDelta());

		counter.addTick();
		counter.check();

	}

	public void render() {

		manager.render();

		uiMaster.render(window.getWidth(), window.getHeight());

		glFinish();
		window.swapBuffers();
		counter.addFrame();

	}

	public void destroy() {

		EventDispatcher.deregisterEventListener(this);
		EventDispatcher.deregisterEventListener(inputManager);

		manager.setState(null);

		uiMaster.destroy();

		window.destroy();

		audioManager.destroy();

		Logger.info("Stopping GameLoop");

	}

}
