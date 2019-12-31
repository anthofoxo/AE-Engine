package cc.antho.ae.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.lwjgl.opengl.GL;

import cc.antho.abstractwindow.GLContext;
import cc.antho.abstractwindow.GlfwWindow;
import cc.antho.abstractwindow.Window;
import cc.antho.abstractwindow.event.window.EventWindowClose;
import cc.antho.ae.common.Util;
import cc.antho.ae.engine.AeEngine;
import cc.antho.ae.engine.AeEngineStartProps;
import cc.antho.ae.log.Logger;
import cc.antho.ae.log.LoggerImpl;
import cc.antho.ae.state.State;
import cc.antho.ae.time.GlfwTimeProvider;
import cc.antho.eventsystem.EventHandler;
import cc.antho.eventsystem.EventLayer;
import cc.antho.eventsystem.EventListener;

public class AeLauncher {

	public static void main(String[] args) {

		try {

			boot(args[0]);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private static void boot(String path) throws FileNotFoundException, IOException {

		Logger.logger = new LoggerImpl();
		File root = new File(path);

		Config config = new Config(new File(root.getAbsolutePath() + "/config.json").getAbsolutePath());

		EventLayer layer = new EventLayer();
		GLContext context = new GLContext(3, 3, true, true);

		GlfwWindow.initGlfw(layer);

		Window window = new GlfwWindow(context, layer, config.width, config.height, config.title);
		AeEngineStartProps props = new AeEngineStartProps(new GlfwTimeProvider(), layer, config.sources);
		AeEngine engine = new AeEngine(props);
		engine.setWindow(window);

		engine.defer(() -> {

			GL.createCapabilities();

			engine.getManager().set(new BaseState(root));

		});

		layer.registerEventListener(new EventListener() {

			@EventHandler
			private void onWindowClose(EventWindowClose event) {

				engine.stop();

			}

		});

		engine.start();

		GlfwWindow.destroyGlfw();

	}

}
