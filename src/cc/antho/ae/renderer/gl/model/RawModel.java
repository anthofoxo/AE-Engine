package cc.antho.ae.renderer.gl.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.renderer.gl.Destroyable;
import lombok.Getter;

public final class RawModel implements Destroyable {

	@Getter private final int primitive;
	@Getter private VertexArray vao;
	@Getter private int vbo, ibo, count;

	public RawModel(int primitive) {

		this.primitive = primitive;

	}

	public void bind() {

		vao.bind();

	}

	private Dataset[] parseNull(Dataset[] datasets) {

		List<Dataset> sets = new ArrayList<>();

		for (int i = 0; i < datasets.length; i++)
			if (datasets[i] != null) sets.add(datasets[i]);

		Dataset[] newSet = new Dataset[sets.size()];
		sets.toArray(newSet);

		return newSet;

	}

	// TODO Allow reuse to prevent allocating more gl objects
	public void uploadData(int[] indices, Dataset... datasets) {

		datasets = parseNull(datasets);

		vao = new VertexArray();
		vbo = glGenBuffers();

		bind();

		if (indices != null) {

			ibo = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

			count = indices.length;

		} else {

			count = datasets[0].getData().length / datasets[0].getSize();

		}

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, DataInterlacer.interlace(datasets), GL_STATIC_DRAW);

		int stride = 0;
		long offset = 0;

		for (int i = 0; i < datasets.length; i++)
			stride += datasets[i].getSize();

		stride *= Float.BYTES;

		for (int i = 0; i < datasets.length; i++) {

			glVertexAttribPointer(i, datasets[i].getSize(), GL_FLOAT, false, stride, offset);
			glEnableVertexAttribArray(i);

			offset += datasets[i].getSize() * Float.BYTES;

		}

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		for (int i = 0; i < datasets.length; i++)
			glDisableVertexAttribArray(i);

	}

	public void render() {

		if (ibo == 0) glDrawArrays(primitive, 0, count);
		else glDrawElements(primitive, count, GL_UNSIGNED_INT, 0L);

	}

	public void destroy() {

		vao.destroy();
		glDeleteBuffers(vbo);
		if (ibo != 0) glDeleteBuffers(ibo);

	}

}
