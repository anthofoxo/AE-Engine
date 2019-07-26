package cc.antho.ae.renderer.gl.model;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import cc.antho.ae.math.builder.FloatBuilder;
import cc.antho.ae.math.builder.IntBuilder;

public class Batcher {

	private FloatBuilder positions = new FloatBuilder();
	private FloatBuilder textures = new FloatBuilder();
	private FloatBuilder normals = new FloatBuilder();
	private IntBuilder indices = new IntBuilder();
	private float[] textureScales;

	private FloatBuilder final_positions = new FloatBuilder();
	private FloatBuilder final_textures = new FloatBuilder();
	private FloatBuilder final_normals = new FloatBuilder();
	private IntBuilder final_indices = new IntBuilder();

	public Batcher(float[] positions, float[] textures, float[] normals, int[] indices, float[] textureScales) {

		this.positions.append(positions);
		this.textures.append(textures);
		this.normals.append(normals);

		if (indices != null) this.indices.append(indices);
		else {

			this.final_indices = null;
			this.indices = null;

		}

		this.textureScales = textureScales;

	}

	public void addInstance(Matrix4f transformation, int[] textureOffsets) {

		for (int n = 0; n < normals.size(); n += 3) {

			Vector4f normal = new Vector4f(normals.get(n + 0), normals.get(n + 1), normals.get(n + 2), 0F);
			transformation.transform(normal);
			final_normals.append(normal.x, normal.y, normal.z);

		}

		for (int n = 0; n < positions.size(); n += 3) {

			Vector4f position = new Vector4f(positions.get(n + 0), positions.get(n + 1), positions.get(n + 2), 1F);
			transformation.transform(position);
			final_positions.append(position.x, position.y, position.z);

		}

		if (indices != null) {

			IntBuilder ind = new IntBuilder();
			ind.append(indices);
			ind.add(final_positions.size());
			final_indices.append(ind);

		}

		FloatBuilder tmp_tex = new FloatBuilder();
		tmp_tex.append(textures);
		tmp_tex.add(textureOffsets);
		final_textures.append(tmp_tex);

	}

	public RawModel build(int primitive) {

		final_textures.mul(textureScales);

		RawModel model = new RawModel(primitive);
		model.uploadData(final_indices == null ? null : final_indices.toArray(), new Dataset(final_positions.toArray(), 3), new Dataset(final_textures.toArray(), 2), new Dataset(final_normals.toArray(), 3));

		final_positions.clear();
		final_textures.clear();
		final_normals.clear();
		if (final_indices != null) final_indices.clear();

		return model;

	}

}
