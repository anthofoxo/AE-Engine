package cc.antho.ae.terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

import cc.antho.ae.math.Maths;
import cc.antho.ae.renderer.gl.model.ModelData;
import cc.antho.ae.terrain.generator.TerrainGenerator;
import cc.antho.ae.terrain.generator.TerrainGeneratorPositionMode;
import lombok.Getter;

public final class Terrain {

	@Getter private int x, z;
	@Getter private TerrainGroup group;

	private float[][] heights;
	private Vector3f[][] normals;
	private TerrainGenerator generator;

	public Terrain(int x, int z, TerrainGroup group, TerrainGenerator generator) {

		this.x = x;
		this.z = z;
		this.group = group;
		this.generator = generator;

		heights = new float[group.getVertexCount()][group.getVertexCount()];
		normals = new Vector3f[group.getVertexCount()][group.getVertexCount()];

	}

	public void generateHeightData() {

		for (int i = 0; i < group.getVertexCount(); i++) {
			for (int j = 0; j < group.getVertexCount(); j++) {

				float x = ((float) j / ((float) group.getVertexCount() - 1) * group.getSize());
				float z = ((float) i / ((float) group.getVertexCount() - 1) * group.getSize());

				if (generator.mode == TerrainGeneratorPositionMode.WORLD) {

					x += this.x * group.getSize();
					z += this.z * group.getSize();

				}

				heights[j][i] = generator.getHeight(x, z);

			}

		}

	}

	Vector3f getNormal(float tx, float tz) {

		float gridSquareSize = group.getSize() / (heights.length - 1);
		int gx = Maths.floor(tx / gridSquareSize);
		int gz = Maths.floor(tz / gridSquareSize);

		float xCoord = (tx % gridSquareSize) / gridSquareSize;
		float zCoord = (tz % gridSquareSize) / gridSquareSize;

		float x, y, z;

		if (xCoord <= (1F - zCoord)) {

			x = Maths.barryCentric(new Vector3f(0F, normals[gx][gz].x, 0F), new Vector3f(1F, normals[gx + 1][gz].x, 0F), new Vector3f(0F, normals[gx][gz + 1].x, 1F), new Vector2f(xCoord, zCoord));
			y = Maths.barryCentric(new Vector3f(0F, normals[gx][gz].y, 0F), new Vector3f(1F, normals[gx + 1][gz].y, 0F), new Vector3f(0F, normals[gx][gz + 1].y, 1F), new Vector2f(xCoord, zCoord));
			z = Maths.barryCentric(new Vector3f(0F, normals[gx][gz].z, 0F), new Vector3f(1F, normals[gx + 1][gz].z, 0F), new Vector3f(0F, normals[gx][gz + 1].z, 1F), new Vector2f(xCoord, zCoord));

		} else {

			x = Maths.barryCentric(new Vector3f(1F, normals[gx + 1][gz].x, 0F), new Vector3f(1F, normals[gx + 1][gz + 1].x, 1F), new Vector3f(0F, normals[gx][gz + 1].x, 1F), new Vector2f(xCoord, zCoord));
			y = Maths.barryCentric(new Vector3f(1F, normals[gx + 1][gz].y, 0F), new Vector3f(1F, normals[gx + 1][gz + 1].y, 1F), new Vector3f(0F, normals[gx][gz + 1].y, 1F), new Vector2f(xCoord, zCoord));
			z = Maths.barryCentric(new Vector3f(1F, normals[gx + 1][gz].z, 0F), new Vector3f(1F, normals[gx + 1][gz + 1].z, 1F), new Vector3f(0F, normals[gx][gz + 1].z, 1F), new Vector2f(xCoord, zCoord));

		}

		return new Vector3f(x, y, z).normalize();

	}

	float getHeight(float tx, float tz) {

		float gridSquareSize = group.getSize() / (heights.length - 1);
		int gx = Maths.floor(tx / gridSquareSize);
		int gz = Maths.floor(tz / gridSquareSize);

		float xCoord = (tx % gridSquareSize) / gridSquareSize;
		float zCoord = (tz % gridSquareSize) / gridSquareSize;

		if (xCoord <= (1 - zCoord)) return Maths.barryCentric(new Vector3f(0F, heights[gx][gz], 0F), new Vector3f(1F, heights[gx + 1][gz], 0F), new Vector3f(0F, heights[gx][gz + 1], 1F), new Vector2f(xCoord, zCoord));
		else return Maths.barryCentric(new Vector3f(1F, heights[gx + 1][gz], 0F), new Vector3f(1, heights[gx + 1][gz + 1], 1F), new Vector3f(0, heights[gx][gz + 1], 1F), new Vector2f(xCoord, zCoord));

	}

	public ModelData generateModelData() {

		int vertexCount = group.getVertexCount();

		int count = vertexCount * vertexCount;

		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];

		int vertexPointer = 0;

		for (int i = 0; i < vertexCount; i++)
			for (int j = 0; j < vertexCount; j++) {

				vertices[vertexPointer * 3] = ((float) j / ((float) vertexCount - 1) * group.getSize()) + x * group.getSize();
				vertices[vertexPointer * 3 + 1] = heights[j][i];
				vertices[vertexPointer * 3 + 2] = ((float) i / ((float) vertexCount - 1) * group.getSize()) + z * group.getSize();

				Vector3f normal = calcNormal(j, i);
				this.normals[j][i] = normal;

				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;

				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				vertexPointer++;

			}

		int pointer = 0;

		for (int gz = 0; gz < vertexCount - 1; gz++)
			for (int gx = 0; gx < vertexCount - 1; gx++) {

				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;

			}

		return new ModelData(vertices, textureCoords, normals, null, indices);

	}

	private Vector3f calcNormal(float x, float z) {

		x += this.x * group.getSize();
		z += this.z * group.getSize();

		float heightL = group.getHeight(x - 1F, z);
		float heightR = group.getHeight(x + 1F, z);
		float heightD = group.getHeight(x, z - 1F);
		float heightU = group.getHeight(x, z + 1F);

		return new Vector3f(heightL - heightR, 2F, heightD - heightU).normalize();

	}

}
