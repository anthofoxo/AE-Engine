package cc.antho.ae.launcher;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import cc.antho.abstractwindow.GLContext;
import cc.antho.abstractwindow.GlfwWindow;
import cc.antho.abstractwindow.Window;
import cc.antho.abstractwindow.event.window.EventWindowClose;
import cc.antho.ae.audio.AudioBuffer;
import cc.antho.ae.audio.AudioSettings;
import cc.antho.ae.common.Util;
import cc.antho.ae.engine.AEEngine;
import cc.antho.ae.engine.AEEngineStartProps;
import cc.antho.ae.log.Logger;
import cc.antho.ae.log.LoggerImpl;
import cc.antho.ae.renderer.gl.GLShaderProgram;
import cc.antho.ae.renderer.gl.GLShaderSource;
import cc.antho.ae.renderer.gl.model.ModelData;
import cc.antho.ae.renderer.gl.model.ModelLoader;
import cc.antho.ae.renderer.gl.model.RawModel;
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
		AEEngineStartProps props = new AEEngineStartProps(new GlfwTimeProvider(), layer, config.sources);
		AEEngine engine = new AEEngine(props);
		engine.setWindow(window);

		engine.defer(() -> {

			GL.createCapabilities();

			engine.getManager().setState(new State() {

				AudioBuffer buffer;
				GLShaderProgram solidShader;
				RawModel model;

				SceneConfig sceneConfig;

				@Override
				public void tick() {
					// TODO Auto-generated method stub

				}

				@Override
				public void render() {
					glViewport(0, 0, engine.getWindow().getWidth(), engine.getWindow().getHeight());

					glClearColor(.7f, .8f, .9f, 1f);
					glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

					Matrix4f projection = new Matrix4f().setPerspective((float) Math.toRadians(70), engine.getWindow().getAspect(), .1f, 1000f);
					Matrix4f view = new Matrix4f();
					Matrix4f modelView = new Matrix4f();
					modelView.translate(sceneConfig.position);

					solidShader.bind();
					solidShader.uniformMat4f("u_model", modelView);
					solidShader.uniformMat4f("u_view", view);
					solidShader.uniformMat4f("u_projection", projection);
					solidShader.uniform3f("u_color", 1.0f, 0.5f, 0f);

					model.bind();
					model.render();

				}

				@Override
				public void init() {

					glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

					// play music if it set
					try {

						sceneConfig = new SceneConfig(root.getAbsolutePath() + config.defaultScene);

						if (sceneConfig.music != null) {

							buffer = new AudioBuffer(root.getAbsolutePath() + sceneConfig.music);

							engine.getAudioManager().play(buffer, AudioSettings.generate2DLooped());

						}

					} catch (IOException e) {

						e.printStackTrace();
					}

					try {

						ModelData data = ModelLoader.loadOBJ(root.getAbsolutePath() + sceneConfig.model);

						model = new RawModel(GL_TRIANGLES);
						model.uploadData(data.getIndices(), data.getPositions3(), data.getTextures2(), data.getNormals3());

					} catch (IOException e1) {

						e1.printStackTrace();

					}

					try {

						GLShaderSource vertSource = engine.getRenderer().genShaderSource(new String(Util.loadByteArray(Util.getStream(root.getAbsolutePath() + "/assets/shaders/solid.vert"))), GL_VERTEX_SHADER);
						GLShaderSource fragSource = engine.getRenderer().genShaderSource(new String(Util.loadByteArray(Util.getStream(root.getAbsolutePath() + "/assets/shaders/solid.frag"))), GL_FRAGMENT_SHADER);
						solidShader = engine.getRenderer().genShaderProgram(vertSource, fragSource);
						vertSource.destroy();
						fragSource.destroy();

					} catch (IOException e) {

						e.printStackTrace();

					}

				}

				@Override
				public void fixedTick() {
					// TODO Auto-generated method stub

				}

				@Override
				public void destroy() {

					model.destroy();
					solidShader.destroy();
					if (buffer != null) buffer.destroy();

				}
			});

//			try {
//
//				URL dirUrl = new URL("file:/" + root.getAbsolutePath() + "/");
//
//				try (URLClassLoader cl = new URLClassLoader(new URL[] { dirUrl }, Util.class.getClassLoader())) {
//
//					Class<?> loadedClass = cl.loadClass("userdef.DefaultState");
//
//					if (loadedClass.getSuperclass().equals(State.class)) {
//
//						Object object = loadedClass.getConstructor().newInstance();
//
//						engine.getManager().setState((State) object);
//
//					}
//
//				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
//
//					e.printStackTrace();
//
//				}
//
//			} catch (MalformedURLException e1) {
//
//				e1.printStackTrace();
//
//			}

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
