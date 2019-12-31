package cc.antho.ae.time;

import org.lwjgl.glfw.GLFW;

public final class GlfwTimeProvider implements TimeProvider {

	public double getTime() {

		return GLFW.glfwGetTime();

	}

}
