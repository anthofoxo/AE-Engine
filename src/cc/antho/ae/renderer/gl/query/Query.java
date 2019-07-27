package cc.antho.ae.renderer.gl.query;

import static org.lwjgl.opengl.GL15.*;

import lombok.Getter;

public class Query {

	@Getter private int handle;
	@Getter private int type;
	@Getter private boolean using = false;

	public Query(int handle, int type) {

		this.type = type;
		this.handle = handle;

	}

	public Query(int type) {

		this(glGenQueries(), type);

	}

	public void begin() {

		glBeginQuery(type, handle);
		using = true;

	}

	public void end() {

		glEndQuery(type);

	}

	public int result() {

		using = false;
		return glGetQueryObjecti(handle, GL_QUERY_RESULT);

	}

	public boolean available() {

		return glGetQueryObjecti(handle, GL_QUERY_RESULT_AVAILABLE) == GL_TRUE;

	}

	public void destroy() {

		glDeleteQueries(handle);

	}

}
