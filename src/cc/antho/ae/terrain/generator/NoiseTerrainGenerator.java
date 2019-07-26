package cc.antho.ae.terrain.generator;

import cc.antho.ae.math.OpenSimplexNoise;

public final class NoiseTerrainGenerator extends TerrainGenerator {

	private OpenSimplexNoise noise;
	private float factor, scale;

	public NoiseTerrainGenerator(OpenSimplexNoise noise, float factor, float scale) {

		this.noise = noise;
		this.factor = factor;
		this.scale = scale;

	}

	public float getHeight(float x, float z) {

		return (float) (noise.eval(x / factor, z / factor) * scale);

	}

}
