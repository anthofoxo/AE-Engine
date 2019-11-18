package cc.antho.ae.editor;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

import cc.antho.abstractwindow.GLContext;
import cc.antho.abstractwindow.GlfwWindow;
import cc.antho.ae.engine.AEEngine;
import cc.antho.ae.engine.AEEngineStartProps;
import cc.antho.ae.log.Logger;
import cc.antho.ae.log.LoggerImpl;
import cc.antho.ae.state.State;
import cc.antho.ae.time.GLFWTimeProvider;
import cc.antho.eventsystem.EventLayer;
import lwjgui.paint.Color;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.ColorPicker;
import lwjgui.scene.control.Menu;
import lwjgui.scene.control.MenuBar;
import lwjgui.scene.control.MenuItem;
import lwjgui.scene.control.SeparatorMenuItem;
import lwjgui.scene.control.SplitPane;
import lwjgui.scene.control.ToolBar;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.VBox;

public class Editor {

	private AEEngine engine;

	public static void main(String[] args) {

		new Editor();

	}

	private Editor() {

		System.setProperty("java.awt.headless", Boolean.TRUE.toString());
		Logger.logger = new LoggerImpl();

		EventLayer layer = new EventLayer();
		GlfwWindow.initGlfw(layer);

		AEEngineStartProps props = new AEEngineStartProps(new GLFWTimeProvider(), layer, 16);
		engine = new AEEngine(props);

		GlfwWindow window = new GlfwWindow(new GLContext(3, 3, true, true), layer, 1280, 720, "AE Engine");
		engine.setWindow(window);
		GL.createCapabilities();

		engine.defer(() -> engine.getManager().setState(new StateInstance()));

		engine.start();

		GlfwWindow.destroyGlfw();

	}

	private float r = .7f, g = .8f, b = .9f;

	private class StateInstance extends State {

		public void init() {

			VBox background = new VBox();
			MenuBar menuBar = new MenuBar();

			{

				Menu file = new Menu("File");
				file.getItems().add(new MenuItem("New"));
				file.getItems().add(new MenuItem("Open"));
				file.getItems().add(new MenuItem("Save"));
				file.getItems().add(new SeparatorMenuItem());
				file.getItems().add(new MenuItem("Exit"));
				menuBar.getItems().add(file);

			}
			background.getChildren().add(menuBar);

			ColorPicker picker = new ColorPicker(new Color(r, g, b));
			picker.setOnAction(e -> {

				r = picker.getColor().getRed() / 255f;
				g = picker.getColor().getGreen() / 255f;
				b = picker.getColor().getBlue() / 255f;

			});

			// Tool Bar
			ToolBar toolBar = new ToolBar();
			toolBar.getItems().add(picker);
			toolBar.getItems().add(new Button("New Asset"));
			toolBar.getItems().add(new Button("New Asset Instance"));

			background.getChildren().add(toolBar);

			// Tab Pane
			SplitPane tabPane = new SplitPane();
			tabPane.setFillToParentHeight(true);
			tabPane.setFillToParentWidth(true);
			background.getChildren().add(tabPane);

			tabPane.getItems().add(new VBox());

			OpenGLPane gears = new OpenGLPane();
			gears.setFillToParentHeight(true);
			gears.setFillToParentWidth(true);

			tabPane.getItems().add(gears);

			tabPane.getItems().add(new VBox());

			// Set the scene
			engine.getLwjgui().getScene().setRoot(background);

			gears.setRendererCallback(context -> {

				// float mx = Maths.map((float) context.getMouseX(), (float) gears.getX(),
				// (float) gears.getX() + (float) gears.getWidth(), 0, 1);
				// float my = Maths.map((float) context.getMouseY(), (float) gears.getY(),
				// (float) gears.getY() + (float) gears.getHeight(), 1, 0);

				glClearColor(0, 0, 0, 1);
				glClear(GL_COLOR_BUFFER_BIT);

			});

		}

		public void tick() {

		}

		public void fixedTick() {

		}

		public void render() {

			glClear(GL_COLOR_BUFFER_BIT);

		}

		public void destroy() {

		}

	}

}
