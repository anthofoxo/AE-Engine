package cc.antho.ae.editor;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import cc.antho.abstractwindow.GLContext;
import cc.antho.abstractwindow.GlfwWindow;
import cc.antho.ae.engine.AEEngine;
import cc.antho.ae.engine.AEEngineStartProps;
import cc.antho.ae.log.Logger;
import cc.antho.ae.log.LoggerImpl;
import cc.antho.ae.renderer.gl.model.ModelData;
import cc.antho.ae.renderer.gl.model.ModelLoader;
import cc.antho.ae.renderer.gl.model.RawModel;
import cc.antho.ae.state.State;
import cc.antho.ae.time.GLFWTimeProvider;
import cc.antho.eventsystem.EventLayer;
import lwjgui.geometry.Insets;
import lwjgui.geometry.Pos;
import lwjgui.scene.Context;
import lwjgui.scene.Node;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.Label;
import lwjgui.scene.control.Menu;
import lwjgui.scene.control.MenuBar;
import lwjgui.scene.control.MenuItem;
import lwjgui.scene.control.PopupWindow;
import lwjgui.scene.control.SeparatorMenuItem;
import lwjgui.scene.control.SplitPane;
import lwjgui.scene.control.ToolBar;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.VBox;
import lwjgui.theme.Theme;

public class Editor {

	private AEEngine engine;

	private Menu assetMenu;
	private Map<String, RawModel> assets = new HashMap<>();
	private float r = .7f, g = .8f, b = .9f;

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

	private void createAssetInstanceGui(Node caller) {

		new PopupWindow() {

			{

				this.setPadding(new Insets(1));
				this.setPaddingColor(Theme.current().getControlOutline());
				this.setBackground(Theme.current().getBackground());
				this.setAlignment(Pos.CENTER);

				VBox background = new VBox();
				background.setSpacing(4);
				background.setBackground(null);
				background.setPadding(new Insets(4));
				this.getChildren().add(background);

				for (String key : assets.keySet()) {

					Button button = new Button(key);
					button.setOnAction(e -> createAssetInstance(button.getText()));
					background.getChildren().add(button);

				}

			}

		}.show(engine.getLwjgui().getScene(), caller.getX(), caller.getY() + caller.getHeight());

	}

	private void createAssetGui(Node caller) {

		new PopupWindow() {

			{

				this.setPadding(new Insets(1));
				this.setPaddingColor(Theme.current().getControlOutline());
				this.setBackground(Theme.current().getBackground());
				this.setAlignment(Pos.CENTER);

				VBox background = new VBox();
				background.setSpacing(4);
				background.setBackground(null);
				background.setPadding(new Insets(4));
				this.getChildren().add(background);

				String[] input = new String[1];

				Label label = new Label("null");

				Button modelButton = new Button("Open Model");
				background.getChildren().add(modelButton);
				modelButton.setOnAction(e2 -> {

					try (MemoryStack stack = stackPush()) {

						String path = new File("./res").getAbsolutePath() + "/";
						PointerBuffer aFilterPatterns = stack.mallocPointer(1);
						aFilterPatterns.put(stack.UTF8("*.obj"));
						aFilterPatterns.flip();

						input[0] = tinyfd_openFileDialog("Open Model", path, aFilterPatterns, "3D Models (*.obj)", false);
						if (input[0] != null) label.setText(input[0]);

					}

				});

				background.getChildren().add(label);

				Button createButton = new Button("Finish");
				background.getChildren().add(createButton);
				createButton.setOnAction(e2 -> {

					if (input[0] != null) createAsset(input[0]);
					this.close();

				});

			}

		}.show(engine.getLwjgui().getScene(), caller.getX(), caller.getY() + caller.getHeight());

	}

	private void createAssetInstance(String key) {

		// TODO create asset instance

	}

	private void createAsset(String file) {

		if (assets.containsKey(file)) return;

		try {

			ModelData data = ModelLoader.loadOBJ(file);

			RawModel model = new RawModel(GL_TRIANGLES);
			model.uploadData(data.getIndices(), data.getPositions3());

			if (assetMenu.getItems().size() == 1) assetMenu.getItems().add(new SeparatorMenuItem());

			MenuItem button = new MenuItem(file);
			button.setOnAction(e -> createAssetInstance(file));
			assetMenu.getItems().add(button);

			assets.put(file, model);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private class StateInstance extends State {

		public void init() {

			StackPane root = new StackPane();
			root.setFillToParentWidth(true);
			root.setFillToParentHeight(true);
			engine.getLwjgui().getScene().setRoot(root);

			VBox background = new VBox();
			background.setFillToParentWidth(true);
			background.setFillToParentHeight(true);
			root.getChildren().add(background);

			MenuBar menuBar = new MenuBar();

			{

				MenuItem exit = new MenuItem("Exit");
				exit.setOnAction(e -> engine.stop());

				Menu file = new Menu("File");
				// TODO file.getItems().add(new MenuItem("New"));
				// TODO file.getItems().add(new MenuItem("Open"));
				// TODO file.getItems().add(new MenuItem("Save"));
				// TODO file.getItems().add(new SeparatorMenuItem());
				file.getItems().add(exit);
				menuBar.getItems().add(file);

			}

			{

				MenuItem createNew = new MenuItem("Create New");
				createNew.setOnAction(e -> createAssetGui(createNew));

				assetMenu = new Menu("Asset");
				assetMenu.getItems().add(createNew);
				menuBar.getItems().add(assetMenu);

			}

			background.getChildren().add(menuBar);

			// TODO Use gui solution when bugs are fixed
//			ColorPicker picker = new ColorPicker(new Color(r, g, b));
//			
//			picker.setOnAction(e -> {
//
//				r = picker.getColor().getRedF();
//				g = picker.getColor().getGreenF();
//				b = picker.getColor().getBlueF();
//
//			});

			ToolBar toolBar = new ToolBar();
			background.getChildren().add(toolBar);

			Button picker = new Button("Set Clear Color");
			toolBar.getItems().add(picker);
			picker.setOnAction(e -> {

				try (MemoryStack stack = stackPush()) {

					ByteBuffer color = stack.malloc(3);

					color.put((byte) (r * 255f));
					color.put((byte) (g * 255f));
					color.put((byte) (b * 255f));
					color.flip();

					if (tinyfd_colorChooser("Set Clear Color", null, color, color) != null) {

						r = (color.get() & 0xFF) / 255f;
						g = (color.get() & 0xFF) / 255f;
						b = (color.get() & 0xFF) / 255f;

					}

				}

			});

			Button newAssetButton = new Button("Create New Asset");
			newAssetButton.setOnAction(e -> createAssetGui(newAssetButton));
			toolBar.getItems().add(newAssetButton);

			Button newAssetInstanceButton = new Button("Create New Asset Instance");
			newAssetInstanceButton.setOnAction(e -> createAssetInstanceGui(newAssetInstanceButton));
			toolBar.getItems().add(newAssetInstanceButton);

			SplitPane splitPane = new SplitPane();
			splitPane.setFillToParentHeight(true);
			splitPane.setFillToParentWidth(true);
			background.getChildren().add(splitPane);

			// TODO tabPane.getItems().add(new VBox());

			OpenGLPane gameView = new OpenGLPane();
			gameView.setFillToParentHeight(true);
			gameView.setFillToParentWidth(true);
			gameView.setRendererCallback(context -> renderScene(context));
			splitPane.getItems().add(gameView);

			// TODO tabPane.getItems().add(new VBox());

		}

		public void tick() {

		}

		public void fixedTick() {

		}

		public void render() {

			glClear(GL_COLOR_BUFFER_BIT);

		}

		public void destroy() {

			for (RawModel value : assets.values())
				value.destroy();

			assets.clear();

		}

	}

	public void renderScene(Context context) {

		glClearColor(r, g, b, 1f);
		glClear(GL_COLOR_BUFFER_BIT);

	}

}
