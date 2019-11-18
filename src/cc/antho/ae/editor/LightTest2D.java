package cc.antho.ae.editor;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import org.lwjgl.opengl.GL;

import cc.antho.abstractwindow.GLContext;
import cc.antho.abstractwindow.GlfwWindow;
import cc.antho.ae.common.Util;
import cc.antho.ae.engine.AEEngine;
import cc.antho.ae.engine.AEEngineStartProps;
import cc.antho.ae.log.Logger;
import cc.antho.ae.log.LoggerImpl;
import cc.antho.ae.math.Maths;
import cc.antho.ae.math.RNG;
import cc.antho.ae.renderer.gl.GLShaderProgram;
import cc.antho.ae.renderer.gl.GLTexture2D;
import cc.antho.ae.renderer.gl.model.Dataset;
import cc.antho.ae.renderer.gl.model.RawModel;
import cc.antho.ae.state.State;
import cc.antho.ae.time.GLFWTimeProvider;
import cc.antho.eventsystem.EventLayer;
import lwjgui.LWJGUIUtil;
import lwjgui.geometry.Insets;
import lwjgui.geometry.Pos;
import lwjgui.paint.Color;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.CheckBox;
import lwjgui.scene.control.ColorPicker;
import lwjgui.scene.control.ComboBox;
import lwjgui.scene.control.Menu;
import lwjgui.scene.control.MenuBar;
import lwjgui.scene.control.MenuItem;
import lwjgui.scene.control.ProgressBar;
import lwjgui.scene.control.RadioButton;
import lwjgui.scene.control.SearchField;
import lwjgui.scene.control.SeparatorMenuItem;
import lwjgui.scene.control.SplitPane;
import lwjgui.scene.control.Tab;
import lwjgui.scene.control.TabPane;
import lwjgui.scene.control.TextArea;
import lwjgui.scene.control.ToggleGroup;
import lwjgui.scene.control.ToolBar;
import lwjgui.scene.layout.HBox;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.VBox;
import lwjgui.scene.shape.Circle;
import lwjgui.scene.shape.Rectangle;
import lwjgui.scene.shape.Shape;
import lwjgui.theme.Theme;

public class LightTest2D {

	private AEEngine engine;

	public static void main(String[] args) {

		new LightTest2D();

	}

	private LightTest2D() {

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

	private class StateInstance extends State {

		GLShaderProgram lightShader;
		GLShaderProgram overlayShader;
		RawModel screenQuad;
		GLTexture2D texture;

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

			// Tool Bar
			ToolBar toolBar = new ToolBar();
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
			engine.getLwjguiWindow().getScene().setRoot(background);

			try {

				texture = engine.getRenderer().genTexture2D();
				texture.storage(Util.loadResourceToImage("/objects.png"));
				texture.minNear();
				texture.magNear();

			} catch (IOException e) {

				e.printStackTrace();

			}

			screenQuad = new RawModel(GL_TRIANGLE_STRIP);
			screenQuad.uploadData(null, new Dataset(new float[] { -1, 1, -1, -1, 1, 1, 1, -1 }, 2));

			try {

				lightShader = engine.getRenderer().genProgram("/2dlight.vert", "/2dlight.frag");
				overlayShader = engine.getRenderer().genProgram("/2dlight.vert", "/2doverlay.frag");

			} catch (IOException e) {

				e.printStackTrace();

			}

			gears.setRendererCallback(context -> {

				float mx = Maths.map((float)context.getMouseX(), (float)gears.getX(), (float)gears.getX() + (float)gears.getWidth(), 0, 1);
				float my = Maths.map((float)context.getMouseY(), (float)gears.getY(), (float)gears.getY() + (float)gears.getHeight(), 1, 0);
				
				glClearColor(0, 0, 0, 1);
				glClear(GL_COLOR_BUFFER_BIT);

				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE);

				lightShader.bind();
				lightShader.uniform1f("u_aspect", (float)context.getWidth() / (float)context.getHeight());
				lightShader.uniform1f("u_grain", (float) engine.getTime() * 10f);
				lightShader.uniform1f("u_grainSize", 1.0f);
				lightShader.uniform1f("u_stepSize", 0.002f);
				lightShader.uniform4f("u_filterColor", 1, 1, 1, 1);

				texture.bind(0);
				screenQuad.bind();

				lightShader.uniform1f("u_lightSize", 0.6f);
				lightShader.uniform2f("u_lightPosition", mx, my);
				lightShader.uniform3f("u_lightColor", 1f, 0f, 0f);
				screenQuad.render();

				lightShader.uniform1f("u_lightSize", RNG.nextFloat(0.3f, 0.4f));
				lightShader.uniform2f("u_lightPosition", 0.5f, 0.5f);
				lightShader.uniform3f("u_lightColor", RNG.nextFloat(0.4f, 0.8f), 1f, 0f);
				screenQuad.render();

				lightShader.uniform1f("u_lightSize", 0.4f);
				lightShader.uniform2f("u_lightPosition", 0.1f, 0.4f);
				lightShader.uniform3f("u_lightColor", 0f, 0f, 1f);
				screenQuad.render();

				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

				overlayShader.bind();
				screenQuad.render();

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
