package cc.antho.ae.terrain.generator;

public abstract class TerrainGenerator {

	public TerrainGeneratorPositionMode mode = TerrainGeneratorPositionMode.WORLD;

	public abstract float getHeight(float x, float z);

}
