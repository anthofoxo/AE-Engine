package cc.antho.ae.gl.model;

import static org.lwjgl.opengl.GL30.*;

import lombok.Getter;

public final class VertexArray {

	public static final VertexArray DEFAULT = new VertexArray(0);

	@Getter private int handle;

	public VertexArray(int handle) {

		this.handle = handle;

	}

	public VertexArray() {

		this(glGenVertexArrays());

	}

	public void bind() {

		glBindVertexArray(handle);

	}

	public void destroy() {

		glDeleteVertexArrays(handle);

	}

}
