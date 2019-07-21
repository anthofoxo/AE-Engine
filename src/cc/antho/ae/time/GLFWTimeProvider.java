package cc.antho.ae.time;

import org.lwjgl.glfw.GLFW;

public final class GLFWTimeProvider implements TimeProvider {

	public double getTime() {

		return GLFW.glfwGetTime();

	}

}
