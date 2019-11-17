package cc.antho.ae.editor;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import org.lwjgl.opengl.GL;

import cc.antho.abstractwindow.GLContext;
import cc.antho.abstractwindow.GlfwWindow;
import cc.antho.ae.common.Util;
import cc.antho.ae.engine.AEEngine;
import cc.antho.ae.engine.AEEngineStartProps;
import cc.antho.ae.gui.GuiDirection;
import cc.antho.ae.gui.GuiEmpty;
import cc.antho.ae.gui.GuiFlexLayout;
import cc.antho.ae.gui.GuiOpenGLPanel;
import cc.antho.ae.gui.GuiPanel;
import cc.antho.ae.gui.GuiText;
import cc.antho.ae.log.Logger;
import cc.antho.ae.log.LoggerImpl;
import cc.antho.ae.math.RNG;
import cc.antho.ae.renderer.color.Colors;
import cc.antho.ae.renderer.gl.GLShaderProgram;
import cc.antho.ae.renderer.gl.GLTexture2D;
import cc.antho.ae.renderer.gl.model.Dataset;
import cc.antho.ae.renderer.gl.model.RawModel;
import cc.antho.ae.state.State;
import cc.antho.ae.time.GLFWTimeProvider;
import cc.antho.eventsystem.EventLayer;

public class NewGuiTest {

	private AEEngine engine;

	public static void main(String[] args) {

		new NewGuiTest();

	}

	private NewGuiTest() {

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

		GuiOpenGLPanel openGlPanel;

		public void init() {

			GuiEmpty splitPanel = new GuiEmpty();
			GuiFlexLayout layout = new GuiFlexLayout();
			splitPanel.layout = layout;
			layout.direction = GuiDirection.VERTICAL;
			engine.getGuiContext().add(splitPanel);

			GuiPanel pane = new GuiPanel();
			splitPanel.add(pane);
			pane.style.put("grow", 1f);

			GuiPanel separator = new GuiPanel();
			separator.color.set(Colors.DARK_GRAY);
			separator.w = 10;
			separator.h = 10;
			splitPanel.add(separator);

			GuiText text = new GuiText("AE Engine");
			text.color.set(Colors.BLACK);
			pane.add(text);

			openGlPanel = new GuiOpenGLPanel(engine.getRenderer());
			openGlPanel.style.put("grow", 2f);

			openGlPanel.setCallback(context -> {

				context.getRenderer().clearColor(Colors.BLACK);
				context.getRenderer().clear(GL_COLOR_BUFFER_BIT);

				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE);

				lightShader.bind();
				lightShader.uniform1f("u_aspect", context.getWidth() / context.getHeight());
				lightShader.uniform1f("time", (float)engine.getTime() * 10f);

				texture.bind(0);
				screenQuad.bind();

				lightShader.uniform1f("atten", 0.6f);
				lightShader.uniform2f("u_lightPos", context.getMouseX(), context.getMouseY());
				lightShader.uniform3f("lightColor", 1f, 0f, 0f);
				screenQuad.render();

				lightShader.uniform1f("atten", RNG.nextFloat(0.3f, 0.4f));
				lightShader.uniform2f("u_lightPos", 0.5f, 0.5f);
				lightShader.uniform3f("lightColor", RNG.nextFloat(0.4f, 0.8f), 1f, 0f);
				screenQuad.render();

				lightShader.uniform1f("atten", 0.4f);
				lightShader.uniform2f("u_lightPos", 0.1f, 0.4f);
				lightShader.uniform3f("lightColor", 0f, 0f, 1f);
				screenQuad.render();

				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

				overlayShader.bind();
				screenQuad.render();

			});

			splitPanel.add(openGlPanel);

			//

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

		}

		public void tick() {

			openGlPanel.style.put("grow", 100f);

		}

		public void fixedTick() {

		}

		public void render() {

		}

		public void destroy() {

		}

	}

}
