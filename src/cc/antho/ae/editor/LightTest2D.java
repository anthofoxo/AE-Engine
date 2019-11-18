//package cc.antho.ae.editor;
//
//import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.system.MemoryStack.*;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.DoubleBuffer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.joml.Matrix4f;
//import org.joml.Vector3f;
//import org.lwjgl.glfw.GLFW;
//import org.lwjgl.system.MemoryStack;
//
//import cc.antho.ae.common.Util;
//import cc.antho.ae.engine.AEEngine;
//import cc.antho.ae.engine.AEEngineStartProps;
//import cc.antho.ae.log.Logger;
//import cc.antho.ae.log.LoggerImpl;
//import cc.antho.ae.math.RNG;
//import cc.antho.ae.renderer.gl.GLShaderProgram;
//import cc.antho.ae.renderer.gl.GLTexture2D;
//import cc.antho.ae.renderer.gl.model.Dataset;
//import cc.antho.ae.renderer.gl.model.RawModel;
//import cc.antho.ae.state.State;
//import cc.antho.ae.time.GLFWTimeProvider;
//import cc.antho.ae.window.GLContext;
//import cc.antho.ae.window.GLFWWindow;
//import cc.antho.eventsystem.EventLayer;
//import lwjgui.geometry.Insets;
//import lwjgui.geometry.Orientation;
//import lwjgui.geometry.Pos;
//import lwjgui.paint.Color;
//import lwjgui.scene.Context;
//import lwjgui.scene.control.Button;
//import lwjgui.scene.control.ColorPicker;
//import lwjgui.scene.control.Label;
//import lwjgui.scene.control.PopupWindow;
//import lwjgui.scene.control.SplitPane;
//import lwjgui.scene.control.ToolBar;
//import lwjgui.scene.layout.OpenGLPane;
//import lwjgui.scene.layout.StackPane;
//import lwjgui.scene.layout.VBox;
//
//public class EditorLightTest {
//
//	private static GLTexture2D defaultTexture;
//
//	static class Asset {
//
//		RawModel model;
//		GLTexture2D texture;
//
//	}
//
//	static class AssetInstance {
//
//		Asset asset;
//
//		Vector3f position = new Vector3f();
//		Vector3f rotation = new Vector3f();
//		Vector3f scale = new Vector3f(1f);
//
//	}
//
//	private static float r = .7f, g = .8f, b = .9f;
//
//	private static Map<String, Asset> assets = new HashMap<>();
//	private static List<AssetInstance> objects = new ArrayList<>();
//	private static GLShaderProgram shader;
//
//	static class NewAssetPopupWindow extends PopupWindow {
//
//		public NewAssetPopupWindow() {
//
//			this.setPadding(new Insets(10));
//			this.setPrefSize(500, 500);
//
//			VBox background = new VBox();
//			background.setSpacing(4);
//			background.setPadding(new Insets(4));
//			this.getChildren().add(background);
//
//			background.getChildren().add(new Label("fshdobnpbhfgn"));
//
//		}
//
//	}
//
//	static OpenGLPane renderPane;
//
//	public static void main(String[] args) {
//
//		System.setProperty("java.awt.headless", Boolean.TRUE.toString());
//		Logger.logger = new LoggerImpl();
//
//		new Thread(() -> {
//
//			GLFWWindow.initContext();
//
//			EventLayer layer = new EventLayer();
//			AEEngineStartProps props = new AEEngineStartProps(new GLFWTimeProvider(), layer, 16);
//			AEEngine engine = new AEEngine(props);
//
//			GLContext context = new GLContext(3, 3, true, true);
//			GLFWWindow window = new GLFWWindow(context, layer, 1280, 720, "AE Engine", 0);
//			engine.setWindow(window);
//
//			GLFW.glfwSwapInterval(1);
//
//			engine.defer(() -> {
//
//				engine.getManager().setState(new State() {
//
//					public void init() {
//
//						genDefaultTexture(engine);
//
//						try {
//
//							shader = engine.getRenderer().genProgram("/shaders/basic.vert", "/shaders/basic.frag");
//
//						} catch (IOException e1) {
//
//							e1.printStackTrace();
//
//						}
//
//						{
//
//							StackPane root = new StackPane();
//							root.setAlignment(Pos.CENTER);
//
//							// Create background pane
//							VBox background = new VBox();
//							background.setFillToParentWidth(true);
//							background.setFillToParentHeight(true);
//							root.getChildren().add(background);
//
//							// Tool Bar
//							ToolBar toolBar = new ToolBar();
//
//							{
//								Button button = new Button("New Asset");
//
//								button.setOnAction(e -> {
//
//									NewAssetPopupWindow window = new NewAssetPopupWindow();
//
//									window.show(engine.surface(), button.getX(), button.getY() + button.getHeight());
//
//									// String modelFile = tinyfd_openFileDialog("Open model", null, null, null,
//									// false);
//									// String textureFile = tinyfd_openFileDialog("Open texture", null, null, null,
//									// false);
//
//								});
//
//								toolBar.getItems().add(button);
//							}
//
//							toolBar.getItems().add(new Button("New Asset Instance"));
//
//							{
//
//								ColorPicker picker = new ColorPicker(new Color(r, g, b));
//								picker.setOnAction(e -> {
//
//									r = picker.getColor().getRed() / 255f;
//									g = picker.getColor().getGreen() / 255f;
//									b = picker.getColor().getBlue() / 255f;
//
//								});
//								toolBar.getItems().add(picker);
//
//							}
//
//							background.getChildren().add(toolBar);
//
//							SplitPane split = new SplitPane();
//							split.setFillToParentHeight(true);
//							split.setFillToParentWidth(true);
//							split.setOrientation(Orientation.VERTICAL);
//							background.getChildren().add(split);
//
//							SplitPane instancesPane = new SplitPane();
//							instancesPane.setFillToParentHeight(true);
//							instancesPane.setFillToParentWidth(true);
//							instancesPane.setOrientation(Orientation.HORIZONTAL);
//							split.getItems().add(instancesPane);
//
//							VBox assetInstancePane = new VBox();
//							instancesPane.getItems().add(assetInstancePane);
//
//							VBox assetsPane = new VBox();
//							instancesPane.getItems().add(assetsPane);
//
//							renderPane = new OpenGLPane();
//							renderPane.setFillToParentHeight(true);
//							renderPane.setFillToParentWidth(true);
//							renderPane.setRendererCallback(context -> {
//
//								detachRender(context);
//
//							});
//							split.getItems().add(renderPane);
//
//							StackPane propertiesPane = new StackPane();
//							split.getItems().add(propertiesPane);
//
//							// Set the scene
//							engine.surface().setRoot(root);
//						}
//
//						// engine code
//
//						
//
//					}
//
//					public void tick() {
//
//					}
//
//					public void fixedTick() {
//
//					}
//
//					GLShaderProgram shader;
//					GLShaderProgram shader2;
//					RawModel model;
//					GLTexture2D texture;
//
//					public void detachRender(Context context) {
//
//						glViewport(0, 0, (int) context.getWidth(), (int) context.getHeight());
//						glClearColor(r, g, b, 1f);
//						glClear(GL_COLOR_BUFFER_BIT);
//
//						float mx = (float) (context.getMouseX());
//						float my = (float) (context.getMouseY());
//
//						mx /= context.getWidth();
//						my /= context.getHeight();
//
//						glEnable(GL_BLEND);
//						glBlendFunc(GL_SRC_ALPHA, GL_ONE);
//
//						shader.bind();
//						shader.uniform1f("u_aspect", (float) context.getWidth() / (float) context.getHeight());
//						
//						
//						texture.bind(0);
//						model.bind();
//
//						shader.uniform1f("atten", 0.6f);
//						shader.uniform2f("u_lightPos", mx, my);
//						shader.uniform3f("lightColor", 1f, 0f, 0f);
//						model.render();
//
//						shader.uniform1f("atten", RNG.nextFloat(0.3f, 0.4f));
//						shader.uniform2f("u_lightPos", 0.5f, 0.5f);
//						shader.uniform3f("lightColor", RNG.nextFloat(0.4f, 0.8f), 1f, 0f);
//						model.render();
//
//						shader.uniform1f("atten", 0.4f);
//						shader.uniform2f("u_lightPos", 0.1f, 0.4f);
//						shader.uniform3f("lightColor", 0f, 0f, 1f);
//						model.render();
//
//						glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//
//						shader2.bind();
//						model.render();
//
//					}
//
//					public void render() {
//
//					}
//
//					public void destroy() {
//
//					}
//
//				});
//
//			});
//
//			engine.start();
//
//			GLFWWindow.destroyContext();
//
//		}).start();
//
//	}
//
//	private static void genDefaultTexture(AEEngine engine) {
//
//		try (MemoryStack stack = MemoryStack.stackPush()) {
//
//			ByteBuffer pixels = stack.malloc(4);
//			pixels.put((byte) 255);
//			pixels.put((byte) 255);
//			pixels.put((byte) 255);
//			pixels.put((byte) 255);
//			pixels.flip();
//
//			defaultTexture = engine.getRenderer().genTexture2D();
//			defaultTexture.storage(1, 1, pixels);
//
//		}
//
//	}
//
//	public static Matrix4f createTransformationMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
//		Matrix4f matrix = new Matrix4f();
//
//		matrix.translate(position);
//		matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
//		matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
//		matrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
//		matrix.scale(scale);
//
//		return matrix;
//	}
//
//}
