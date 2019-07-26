package cc.antho.ae.terrain;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import cc.antho.ae.math.Maths;
import cc.antho.ae.terrain.generator.TerrainGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TerrainGroup {

	@Getter private final List<Terrain> terrains = new ArrayList<>();

	@Getter private final int vertexCount;
	@Getter private final float size;

	public Terrain getTerrain(int x, int z) {

		Terrain terrain = null;

		for (int i = 0; i < terrains.size(); i++) {

			terrain = terrains.get(i);
			if (terrain.getX() == x && terrain.getZ() == z) return terrain;

		}

		return null;

	}

	public Terrain addTerrain(int x, int z, TerrainGenerator generator) {

		Terrain terrain = getTerrain(x, z);
		if (terrain != null) return terrain;

		terrain = new Terrain(x, z, this, generator);
		terrains.add(terrain);

		return terrain;

	}

	public void removeTerrain(int x, int z) {

		Terrain terrain = getTerrain(x, z);
		if (terrain != null) terrains.remove(terrain);

	}

	public Vector3f getNormal(float x, float z) {

		int tx = Maths.floor(x / size);
		int tz = Maths.floor(z / size);

		Terrain terrain = getTerrain(tx, tz);
		if (terrain == null) return new Vector3f(0F, 1F, 0F);

		return terrain.getNormal(x - tx * size, z - tz * size);

	}

	public float getHeight(float x, float z) {

		int tx = Maths.floor(x / size);
		int tz = Maths.floor(z / size);

		Terrain terrain = getTerrain(tx, tz);
		if (terrain == null) return 0F;

		return terrain.getHeight(x - tx * size, z - tz * size);

	}

}
