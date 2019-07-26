package cc.antho.ae.terrain.generator;

public final class FlatTerrainGenerator extends TerrainGenerator {

	private float height;

	public FlatTerrainGenerator(float height) {

		this.height = height;

	}

	public float getHeight(float x, float z) {

		return height;

	}

}
