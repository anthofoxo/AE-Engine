package cc.antho.ae.terrain.generator;

import cc.antho.ae.math.OpenSimplexNoise;

public final class MultiNoiseTerrainGenerator extends TerrainGenerator {

	private OpenSimplexNoise[] noises;
	private float[] factors, scales, offsets;

	public MultiNoiseTerrainGenerator(OpenSimplexNoise[] noises, float[] factors, float[] scales, float[] offsets) {

		this.noises = noises;
		this.factors = factors;
		this.scales = scales;
		this.offsets = offsets;

	}

	public float getHeight(float x, float z) {

		float totalHeight = 0;

		for (int i = 0; i < noises.length; i++)
			totalHeight += (float) (noises[i].eval(x / factors[i], z / factors[i]) * scales[i]) + offsets[i];

		return totalHeight;

	}

}
