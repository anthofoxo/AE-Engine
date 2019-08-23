package cc.antho.ae.window;

import org.joml.Vector2f;

public abstract class Window {

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract String getTitle();

	public abstract void setTitle(String title);

	public float getAspect() {

		return (float) getWidth() / (float) getHeight();

	}

	public abstract void pollEvents();

	public abstract void swapBuffers();

	public abstract void destroy();

	public abstract void triggerResize();

	public abstract void lockCursor();

	public abstract void unlockCursor();

	public Vector2f getSize() {

		return new Vector2f(getWidth(), getHeight());

	}

}
