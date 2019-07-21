package cc.antho.ae.window;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import cc.antho.ae.events.window.EventWindowClosed;
import cc.antho.ae.events.window.EventWindowKeyChar;
import cc.antho.ae.events.window.EventWindowKeyPress;
import cc.antho.ae.events.window.EventWindowKeyRelease;
import cc.antho.ae.events.window.EventWindowKeyRepeat;
import cc.antho.ae.events.window.EventWindowMouseMoved;
import cc.antho.ae.events.window.EventWindowMousePress;
import cc.antho.ae.events.window.EventWindowMouseRelease;
import cc.antho.ae.events.window.EventWindowResize;
import cc.antho.ae.log.Logger;
import cc.antho.eventsystem.EventLayer;
import lombok.Getter;

public final class GLFWWindow extends Window {

	@Getter private long handle;
	@Getter private int width, height;
	@Getter private String title;

	private EventLayer layer;

	public static void initContext() {

		Logger.debug("Initializing GLFW");
		if (!glfwInit()) Logger.error("Failed to initialize GLFW");

	}

	public static void terminateContext() {

		Logger.debug("Terminating GLFW");
		glfwTerminate();

	}

	public GLFWWindow(GLContext context, EventLayer layer, int width, int height, String title, int swapInterval) {

		this.layer = layer;

		glfwDefaultWindowHints();

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, context.getMajor());
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, context.getMinor());
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, context.isForward() ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, context.isCore() ? GLFW_OPENGL_CORE_PROFILE : GLFW_OPENGL_COMPAT_PROFILE);

		handle = glfwCreateWindow(width, height, title, 0L, 0L);

		if (handle == 0) throw new RuntimeException("Window failed to be created");

		glfwSetWindowSizeLimits(handle, 320, 180, GLFW_DONT_CARE, GLFW_DONT_CARE);

		glfwSetFramebufferSizeCallback(handle, (long window, int w, int h) -> {

			this.width = w;
			this.height = h;

			layer.dispatch(new EventWindowResize(this, w, h));

		});

		glfwSetCursorPosCallback(handle, (long window, double xpos, double ypos) -> {

			layer.dispatch(new EventWindowMouseMoved(this, (float) xpos, (float) ypos));

		});

		glfwSetMouseButtonCallback(handle, (long window, int button, int action, int mods) -> {

			if (action == GLFW_PRESS) layer.dispatch(new EventWindowMousePress(this, button));
			else if (action == GLFW_RELEASE) layer.dispatch(new EventWindowMouseRelease(this, button));

		});

		glfwSetCharCallback(handle, (long window, int codepoint) -> {

			layer.dispatch(new EventWindowKeyChar(this, (char) codepoint));

		});

		glfwSetKeyCallback(handle, (long window, int key, int scancode, int action, int mods) -> {

			if (action == GLFW_PRESS) layer.dispatch(new EventWindowKeyPress(this, key));
			else if (action == GLFW_RELEASE) layer.dispatch(new EventWindowKeyRelease(this, key));
			else if (action == GLFW_REPEAT) layer.dispatch(new EventWindowKeyRepeat(this, key));

		});

		glfwSetWindowCloseCallback(handle, window -> {

			layer.dispatch(new EventWindowClosed(this));

		});

		try (MemoryStack stack = MemoryStack.stackPush()) {

			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(handle, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(handle, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);

		}

		Logger.debug("Creating OpenGL context");

		glfwMakeContextCurrent(handle);
		GL.createCapabilities();
		glfwSwapInterval(swapInterval);

		this.width = width;
		this.height = height;

	}

	public void hide() {

		glfwHideWindow(handle);

	}

	public void show() {

		glfwShowWindow(handle);

	}

	public void setTitle(String title) {

		glfwSetWindowTitle(handle, this.title = title);

	}

	public void swapBuffers() {

		glfwSwapBuffers(handle);

	}

	public void unlockCursor() {

		glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		Logger.debug("Cursor unlocked");

	}

	public void lockCursor() {

		glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		Logger.debug("Cursor locked");

	}

	public void destroy() {

		GL.setCapabilities(null);

		Logger.debug("Closing window");
		glfwFreeCallbacks(handle);
		glfwDestroyWindow(handle);

	}

	public void pollEvents() {

		glfwPollEvents();

	}

	public void triggerResize() {

		layer.dispatch(new EventWindowResize(this, width, height));

	}

}
