package cc.antho.ae.renderer.gl.model;

import cc.antho.ae.renderer.gl.model.Dataset;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ModelData {

	private final float[] positions;
	private final float[] textures;
	private final float[] normals;
	private final float[] tangents;
	private final int[] indices;

	public Dataset getPositions3() {

		return new Dataset(positions, 3);

	}

	public Dataset getTextures2() {

		return new Dataset(textures, 2);

	}

	public Dataset getNormals3() {

		return new Dataset(normals, 3);

	}

	public Dataset getTangents3() {

		return new Dataset(tangents, 3);

	}

}
