package cc.antho.ae.renderer;

import static org.lwjgl.opengl.GL11.*;

import cc.antho.ae.renderer.color.Color;

public class Renderer {

	public void clearColor(Color color) {

		clearColor(color.r, color.g, color.b, color.a);

	}

	public void clearColor(float r, float g, float b, float a) {

		glClearColor(r, g, b, a);

	}

	public void clear(int mask) {

		glClear(mask);

	}

}
