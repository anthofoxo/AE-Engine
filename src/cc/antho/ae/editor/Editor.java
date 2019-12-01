package cc.antho.ae.editor;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import cc.antho.abstractwindow.GLContext;
import cc.antho.abstractwindow.GlfwWindow;
import cc.antho.ae.common.Util;
import cc.antho.ae.engine.AEEngine;
import cc.antho.ae.engine.AEEngineStartProps;
import cc.antho.ae.log.Logger;
import cc.antho.ae.log.LoggerImpl;
import cc.antho.ae.renderer.color.Colors;
import cc.antho.ae.renderer.gl.GLFramebuffer;
import cc.antho.ae.renderer.gl.GLRenderbuffer;
import cc.antho.ae.renderer.gl.GLShaderProgram;
import cc.antho.ae.renderer.gl.GLTexture2D;
import cc.antho.ae.renderer.gl.model.ModelData;
import cc.antho.ae.renderer.gl.model.ModelLoader;
import cc.antho.ae.renderer.gl.model.RawModel;
import cc.antho.ae.state.State;
import cc.antho.ae.time.GLFWTimeProvider;
import cc.antho.eventsystem.EventLayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lwjgui.geometry.Insets;
import lwjgui.geometry.Pos;
import lwjgui.paint.Color;
import lwjgui.scene.Context;
import lwjgui.scene.Node;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.CheckBox;
import lwjgui.scene.control.ColorPicker;
import lwjgui.scene.control.Label;
import lwjgui.scene.control.Menu;
import lwjgui.scene.control.MenuBar;
import lwjgui.scene.control.MenuItem;
import lwjgui.scene.control.PopupWindow;
import lwjgui.scene.control.SeparatorMenuItem;
import lwjgui.scene.control.SplitPane;
import lwjgui.scene.control.TextField;
import lwjgui.scene.control.ToolBar;
import lwjgui.scene.layout.HBox;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.StackPane;
import lwjgui.scene.layout.VBox;
import lwjgui.theme.Theme;

public class Editor {

	private AEEngine engine;

	private Menu assetMenu;
	private VBox sceneGraph;
	private VBox entityProperties;
	public Map<String, Asset> assets = new HashMap<>();
	public Map<Asset, List<AssetInstance>> instances = new HashMap<>();
	private float r = .7f, g = .8f, b = .9f;
	private GLShaderProgram basicShader;
	private OpenGLPane gameView;

	private GLFramebuffer framebuffer;
	private GLRenderbuffer depth_buffer;
	private GLTexture2D albedo_buffer;
	private GLTexture2D normal_buffer;

	@AllArgsConstructor
	@Getter
	static class Asset {

		private RawModel model;
		private GLTexture2D texture;
		private boolean cullFaces;
		private String id;

		void destroy() {

			model.destroy();
			texture.destroy();

		}

	}

	@AllArgsConstructor
	@Getter
	static class AssetInstance {

		private Asset asset;
		private Vector3f positon;

	}

	public static void main(String[] args) {

		new Editor();

	}

	private Editor() {

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

				String[] input = new String[2];

				Label label = new Label("null");
				Label label2 = new Label("null");

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

				Button textureButton = new Button("Open Texture");
				background.getChildren().add(textureButton);
				textureButton.setOnAction(e2 -> {

					try (MemoryStack stack = stackPush()) {

						String path = new File("./res").getAbsolutePath() + "/";
						PointerBuffer aFilterPatterns = stack.mallocPointer(2);
						aFilterPatterns.put(stack.UTF8("*.png"));
						aFilterPatterns.put(stack.UTF8("*.jpg"));
						aFilterPatterns.flip();

						input[1] = tinyfd_openFileDialog("Open Texture", path, aFilterPatterns, "Textures (*.png, *.jpg)", false);
						if (input[1] != null) label2.setText(input[1]);

					}

				});

				background.getChildren().add(label2);

				CheckBox cullFaces = new CheckBox("Cull Faces");
				cullFaces.setChecked(true);

				background.getChildren().add(cullFaces);

				Button createButton = new Button("Finish");
				background.getChildren().add(createButton);
				createButton.setOnAction(e2 -> {

					if (input[0] != null && input[1] != null) createAsset(input[0], input[1], cullFaces.isChecked());
					this.close();

				});

			}

		}.show(engine.getLwjgui().getScene(), caller.getX(), caller.getY() + caller.getHeight());

	}

	private void createAssetInstance(String key) {

		Asset asset = assets.get(key);
		AssetInstance instance = new AssetInstance(asset, new Vector3f());

		if (!instances.containsKey(asset)) instances.put(asset, new ArrayList<>());

		instances.get(asset).add(instance);

		Button button = new Button(key);
		sceneGraph.getChildren().add(button);

		button.setOnAction(e -> {

			entityProperties.getChildren().clear();

			HBox positionX = new HBox();
			HBox positionY = new HBox();
			HBox positionZ = new HBox();
			entityProperties.getChildren().add(new Label("Position"));
			entityProperties.getChildren().add(positionX);
			entityProperties.getChildren().add(positionY);
			entityProperties.getChildren().add(positionZ);

			positionX.getChildren().add(new Label("X: "));
			positionY.getChildren().add(new Label("Y: "));
			positionZ.getChildren().add(new Label("Z: "));

			TextField fieldX = new TextField(instance.getPositon().x + "");
			TextField fieldY = new TextField(instance.getPositon().y + "");
			TextField fieldZ = new TextField(instance.getPositon().z + "");

			positionX.getChildren().add(fieldX);
			positionY.getChildren().add(fieldY);
			positionZ.getChildren().add(fieldZ);

			fieldX.setOnTextChange(e2 -> {

				try {

					instance.getPositon().x = Float.parseFloat(fieldX.getText());

				} catch (NumberFormatException e3) {

				}

			});

			fieldY.setOnTextChange(e2 -> {

				try {

					instance.getPositon().y = Float.parseFloat(fieldY.getText());

				} catch (NumberFormatException e3) {

				}

			});

			fieldZ.setOnTextChange(e2 -> {

				try {

					instance.getPositon().z = Float.parseFloat(fieldZ.getText());

				} catch (NumberFormatException e3) {

				}

			});

		});

	}

	private void createAsset(String file, String texture, boolean cullFaces) {

		if (assets.containsKey(file)) return;

		try {

			ModelData data = ModelLoader.loadOBJ(file);

			RawModel model = new RawModel(GL_TRIANGLES);
			model.uploadData(data.getIndices(), data.getPositions3(), data.getTextures2(), data.getNormals3());

			BufferedImage rawTexture = Util.loadResourceToImage(texture);
			GLTexture2D glTexture = engine.getRenderer().genTexture2D();
			glTexture.storage(rawTexture);

			if (assetMenu.getItems().size() == 1) assetMenu.getItems().add(new SeparatorMenuItem());

			MenuItem button = new MenuItem(file);
			button.setOnAction(e -> createAssetInstance(file));
			assetMenu.getItems().add(button);

			assets.put(file, new Asset(model, glTexture, cullFaces, file));

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

				MenuItem newItem = new MenuItem("New");
				newItem.setOnAction(e -> {

					for (Asset value : assets.values())
						value.getModel().destroy();

					assets.clear();

					instances.clear();
					sceneGraph.getChildren().clear();

					MenuItem keep = assetMenu.getItems().get(0);
					assetMenu.getItems().clear();
					assetMenu.getItems().add(keep);

					entityProperties.getChildren().clear();

				});

				MenuItem exit = new MenuItem("Exit");
				exit.setOnAction(e -> engine.stop());

				Menu file = new Menu("File");
				file.getItems().add(newItem);
				// TODO file.getItems().add(new MenuItem("Open"));
				// TODO file.getItems().add(new MenuItem("Save"));
				file.getItems().add(new SeparatorMenuItem());
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

			ColorPicker picker = new ColorPicker(new Color(r, g, b));

			picker.setOnAction(e -> {

				r = picker.getColor().getRedF();
				g = picker.getColor().getGreenF();
				b = picker.getColor().getBlueF();

			});

			ToolBar toolBar = new ToolBar();
			background.getChildren().add(toolBar);

			toolBar.getItems().add(picker);

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

			sceneGraph = new VBox();
			splitPane.getItems().add(sceneGraph);

			gameView = new OpenGLPane();
			gameView.setFillToParentHeight(true);
			gameView.setFillToParentWidth(true);
			gameView.setRendererCallback(context -> renderScene(context));
			splitPane.getItems().add(gameView);

			entityProperties = new VBox();
			splitPane.getItems().add(entityProperties);

			try {

				basicShader = engine.getRenderer().genProgram("/shaders/flat.vert", "/shaders/flat.frag");

			} catch (IOException e) {

				e.printStackTrace();

			}

			ALTest.init(engine.getAudioManager());

		}

		public void tick() {

		}

		public void fixedTick() {

		}

		public void render() {

			glClear(GL_COLOR_BUFFER_BIT);

			ALTest.render();

		}

		public void destroy() {

			for (Asset value : assets.values())
				value.destroy();

			assets.clear();

			basicShader.destroy();

			ALTest.destroy();

		}

	}

	public void renderScene(Context context) {

		glClearColor(r, g, b, 1f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glEnable(GL_DEPTH_TEST);
		glCullFace(GL_BACK);

		basicShader.bind();
		basicShader.uniform3f("u_color", Colors.WHITE);
		basicShader.uniformMat4f("u_view", new Matrix4f());
		basicShader.uniformMat4f("u_projection", new Matrix4f().setPerspective(70f, (float) gameView.getWidth() / (float) gameView.getHeight(), 0.1f, 1000f));

		for (Asset asset : instances.keySet()) {

			asset.model.bind();
			asset.texture.bind(0);

			if (asset.cullFaces) glEnable(GL_CULL_FACE);
			else glDisable(GL_CULL_FACE);

			for (AssetInstance instance : instances.get(asset)) {

				Matrix4f matrix = new Matrix4f();
				matrix.translate(instance.getPositon());
				basicShader.uniformMat4f("u_model", matrix);

				asset.model.render();

			}

		}

	}

}
