package cc.antho.ae.engine;

import java.util.ArrayList;
import java.util.List;

import cc.antho.abstractwindow.Window;
import cc.antho.abstractwindow.event.window.EventWindowClose;
import cc.antho.abstractwindow.event.window.focus.EventWindowFocus;
import cc.antho.abstractwindow.event.window.input.keyboard.EventWindowKeyboardChar;
import cc.antho.abstractwindow.event.window.input.keyboard.key.EventWindowKeyboardKey;
import cc.antho.abstractwindow.event.window.input.mouse.EventWindowMouseMoved;
import cc.antho.abstractwindow.event.window.input.mouse.EventWindowMouseScrolled;
import cc.antho.abstractwindow.event.window.input.mouse.button.EventWindowMouseButton;
import cc.antho.abstractwindow.event.window.resize.EventWindowResized;
import cc.antho.ae.audio.AudioManager;
import cc.antho.ae.common.Timer;
import cc.antho.ae.gameloop.FrameCounter;
import cc.antho.ae.gameloop.GameLoopI;
import cc.antho.ae.input.InputManager;
import cc.antho.ae.log.Logger;
import cc.antho.ae.renderer.gl.GLRenderer;
import cc.antho.ae.state.StateManager;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventLayer;
import cc.antho.eventsystem.EventListener;
import cc.antho.eventsystem.EventPriority;
import lombok.Getter;
import lombok.Setter;
import lwjgui.LWJGUI;

public final class AEEngine extends GameLoopI implements EventListener {

	@Getter private StateManager manager = new StateManager();
	@Getter private FrameCounter counter = new FrameCounter();

	@Getter @Setter private Window window;

	private List<Runnable> deferred = new ArrayList<>();
	private List<Timer> timers = new ArrayList<>();

	@Getter private InputManager inputManager;
	@Getter private AudioManager audioManager;
	@Getter private EventLayer layer;
	@Getter private GLRenderer renderer = new GLRenderer();

	@Getter private lwjgui.scene.Window lwjgui;

	private AEEngineStartProps props;

	public AEEngine(AEEngineStartProps props) {

		super(props.getProvider(), 1D / 100D);

		this.props = props;
		layer = props.getLayer();

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwWindowCloseCallback(EventWindowClose event) {

		lwjgui.glfwWindowCloseCallback(event.window.getHandle());
		if (event.window == window) stop();

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwCursorPosCallback(EventWindowMouseMoved event) {

		lwjgui.glfwCursorPosCallback(event.window.getHandle(), event.x, event.y);

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwCharCallback(EventWindowKeyboardChar event) {

		lwjgui.glfwCharCallback(event.window.getHandle(), event.codepoint);

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwKeyCallback(EventWindowKeyboardKey event) {

		lwjgui.glfwKeyCallback(event.window.getHandle(), event.key, event.scancode, event.action, event.mods);

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwMouseButtonCallback(EventWindowMouseButton event) {

		lwjgui.glfwMouseButtonCallback(event.window.getHandle(), event.button, event.action, event.mods);

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwWindowFocusCallback(EventWindowFocus event) {

		lwjgui.glfwWindowFocusCallback(event.window.getHandle(), event.focused);

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwWindowSizeCallback(EventWindowResized event) {

		lwjgui.glfwWindowSizeCallback(event.window.getHandle(), event.w, event.h);

	}

	@EventHandler(priority = EventPriority.HIGH)
	private void glfwScrollCallback(EventWindowMouseScrolled event) {

		lwjgui.glfwScrollCallback(event.window.getHandle(), event.x, event.y);

	}

	public void init() {

		inputManager = new InputManager();
		audioManager = new AudioManager(props.getSources());

		Logger.info("Starting GameLoop");
		counter.mark();

		layer.registerEventListener(this);
		layer.registerEventListener(inputManager);

		// Initialize lwjgui for this window
		lwjgui = LWJGUI.initialize(this.window.getHandle(), false);
		lwjgui.setWindowAutoClear(false);
		lwjgui.setWindowAutoDraw(false);
		lwjgui.setCanUserClose(false);
		lwjgui.setAutoDestroy(false);

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

		LWJGUI.render();

		window.swapBuffers();
		counter.addFrame();

	}

	public void destroy() {

		layer.deregisterEventListener(this);
		layer.deregisterEventListener(inputManager);

		manager.setState(null);

		window.destroy();

		audioManager.destroy();

		Logger.info("Stopping GameLoop");

	}

}
