package cc.antho.ae.launcher;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cc.antho.ae.engine.AeEngine;
import cc.antho.ae.entity.Component;
import cc.antho.ae.entity.Entity;
import cc.antho.ae.entity.EntityGroup;
import cc.antho.ae.entity.Transform;
import cc.antho.ae.renderer.gl.GLShaderProgram;
import cc.antho.ae.renderer.gl.GLShaderSource;
import cc.antho.ae.renderer.gl.GLTexture2D;
import cc.antho.ae.renderer.gl.model.ModelData;
import cc.antho.ae.renderer.gl.model.ModelLoader;
import cc.antho.ae.renderer.gl.model.RawModel;
import cc.antho.ae.state.State;
import cc.antho.audio.AudioBuffer;
import cc.antho.audio.AudioSettings;
import cc.antho.common.Camera;
import cc.antho.common.Util;
import cc.antho.common.math.Maths;

public class BaseState extends State<AeEngine> {

	private AudioBuffer buffer;
	private GLShaderProgram solidShader;

	private JSONObject sceneConfig;

	private Map<String, Asset> models = new HashMap<>();
	private EntityGroup group = new EntityGroup();

	private File root;

	private Camera camera = new Camera();

	public BaseState(File root) {

		this.root = root;

	}

	@Override
	public void init() {

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

		try {

			byte[] bytes = Util.loadByteArray(Util.getStream(root.getAbsolutePath() + "/scene.json"));
			sceneConfig = new JSONObject(new String(bytes));

		} catch (IOException e2) {

			e2.printStackTrace();

		}

		// play music if it set
		if (sceneConfig.optString("music", null) != null) {

			buffer = new AudioBuffer(root.getAbsolutePath() + sceneConfig.getString("music"));

			getOwner().getAudioManager().play(buffer, AudioSettings.generate2DLooped());

		}

		JSONObject modelsNode = sceneConfig.optJSONObject("assets").getJSONObject("models");

		if (modelsNode != null) {

			for (String key : modelsNode.keySet()) {

				JSONObject value = modelsNode.getJSONObject(key);

				try {

					ModelData data = ModelLoader.loadOBJ(root.getAbsolutePath() + value.getString("model"));

					RawModel model = new RawModel(GL_TRIANGLES);
					model.uploadData(data.getIndices(), data.getPositions3(), data.getTextures2(), data.getNormals3());

					GLTexture2D texture = getOwner().getRenderer().genTexture2D();
					texture.storage(Util.loadImage(Util.getStream(root.getAbsolutePath() + value.getString("texture")), true));

					models.put(key, new Asset(model, texture));

				} catch (IOException e1) {

					e1.printStackTrace();

				}

			}

		}

		JSONArray entitiesNode = sceneConfig.optJSONArray("entities");

		if (entitiesNode != null) {

			for (Object o : entitiesNode) {

				JSONObject modelNode = (JSONObject) o;

				Entity entity = new Entity();
				if (modelNode.has("model")) entity.addComponent(new RawModelComponent(models.get(modelNode.getString("model"))));
				Transform t = entity.getComponent(Transform.class);

				JSONArray position = modelNode.getJSONArray("position");

				t.getPosition().x = position.getFloat(0);
				t.getPosition().y = position.getFloat(1);
				t.getPosition().z = position.getFloat(2);

				group.addEntity(entity);

				entity.addComponent(new GeneralEntityAccessor(getOwner()));

				JSONArray components = modelNode.optJSONArray("components");

				if (components != null) {

					for (int i = 0; i < components.length(); i++) {

						String component = components.getString(i);

						try {

							Class<?> clazz = Util.loadClass(component, new URL("file:/" + root.getAbsolutePath() + "/code/"));

							if (clazz != null) {

								// check if this class is a components
								if (Component.class.isAssignableFrom(clazz)) {

									try {

										Object a = clazz.getConstructor().newInstance();

										Component c = Component.class.cast(a);
										entity.addComponent(c);

									} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {

										e.printStackTrace();
									}

								} else System.err.println("Defined class is not a component" + component);

							} else throw new RuntimeException(component + " not found");

						} catch (MalformedURLException e) {

							e.printStackTrace();

						}

					}

				}

			}

		}

		try {

			GLShaderSource vertSource = getOwner().getRenderer().genShaderSource(new String(Util.loadByteArray(Util.getStream(root.getAbsolutePath() + "/assets/shaders/tex.vert"))), GL_VERTEX_SHADER);
			GLShaderSource fragSource = getOwner().getRenderer().genShaderSource(new String(Util.loadByteArray(Util.getStream(root.getAbsolutePath() + "/assets/shaders/tex.frag"))), GL_FRAGMENT_SHADER);
			solidShader = getOwner().getRenderer().genShaderProgram(vertSource, fragSource);
			vertSource.destroy();
			fragSource.destroy();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@Override
	public void tick() {

		group.tick();

	}

	@Override
	public void fixedTick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {

		group.render();

		glViewport(0, 0, getOwner().getWindow().getWidth(), getOwner().getWindow().getHeight());

		glClearColor(.7f, .8f, .9f, 1f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		solidShader.bind();

		solidShader.uniformMat4f("u_view", camera.getViewMatrix());
		solidShader.uniformMat4f("u_projection", camera.getProjectionMatrix(getOwner().getWindow().getAspect()));
		solidShader.uniform3f("u_color", 1.0f, 0.5f, 0f);

		for (Entity e : group.getEntities()) {

			RawModelComponent model = e.getComponent(RawModelComponent.class);
			Transform transform = e.getComponent(Transform.class);

			if (model != null) {

				solidShader.uniformMat4f("u_model", Maths.createModelMatrix(transform.getPosition(), transform.getRotation(), transform.getScale()));

				model.model.albedo.bind(0);
				model.model.model.bind();
				model.model.model.render();

			}

		}

	}

	@Override
	public void destroy() {

		for (Asset asset : models.values()) {
			asset.model.destroy();
			asset.albedo.destroy();
		}

		models.clear();

		solidShader.destroy();
		if (buffer != null) buffer.destroy();

	}

}
