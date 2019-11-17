package cc.antho.ae.renderer.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import cc.antho.ae.common.Destroyable;
import lombok.Getter;

public class GLQuery implements Destroyable {

	@Getter private int handle;
	@Getter private int type;
	@Getter private boolean using = false;

	GLQuery(int handle, int type) {

		this.type = type;
		this.handle = handle;

	}

	GLQuery(int type) {

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
