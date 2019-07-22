package cc.antho.ae.terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

import cc.antho.ae.math.Maths;
import cc.antho.ae.renderer.gl.model.ModelData;
import lombok.Getter;

public final class Terrain {

	@Getter private ModelData data;
	private float[][] heights;
	private Vector3f[][] normals;
	private int vertexCount;
	private float size;

	public Terrain(int vertexCount, float size, TerrainGenerator generator) {

		this.vertexCount = vertexCount;
		this.size = size;

		heights = new float[vertexCount][vertexCount];
		normals = new Vector3f[vertexCount][vertexCount];
		fillHeights(size, generator);
		generate(size);

	}

	private void fillHeights(float size, TerrainGenerator generator) {

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {

				float x = (float) j / ((float) vertexCount - 1) * size;
				float z = (float) i / ((float) vertexCount - 1) * size;
				heights[j][i] = generator.getHeight(x, z);

			}
		}

	}

	public Vector3f getNormal(float wx, float wz) {

		float tx = wx - 0;
		float tz = wz - 0;
		float gridSquareSize = size / (heights.length - 1);
		int gx = (int) Math.floor(tx / gridSquareSize);
		int gz = (int) Math.floor(tz / gridSquareSize);

		if (gx < 0 || gz < 0 || gx >= heights.length - 1 || gz >= heights.length - 1) return new Vector3f(0F, 1F, 0F);

		float xCoord = (tx % gridSquareSize) / gridSquareSize;
		float zCoord = (tz % gridSquareSize) / gridSquareSize;

		if (xCoord <= (1 - zCoord)) {

			float x = Maths.barryCentric(new Vector3f(0, normals[gx][gz].x, 0), new Vector3f(1, normals[gx + 1][gz].x, 0), new Vector3f(0, normals[gx][gz + 1].x, 1), new Vector2f(xCoord, zCoord));
			float y = Maths.barryCentric(new Vector3f(0, normals[gx][gz].y, 0), new Vector3f(1, normals[gx + 1][gz].y, 0), new Vector3f(0, normals[gx][gz + 1].y, 1), new Vector2f(xCoord, zCoord));
			float z = Maths.barryCentric(new Vector3f(0, normals[gx][gz].z, 0), new Vector3f(1, normals[gx + 1][gz].z, 0), new Vector3f(0, normals[gx][gz + 1].z, 1), new Vector2f(xCoord, zCoord));

			return new Vector3f(x, y, z).normalize();

		} else {

			float x = Maths.barryCentric(new Vector3f(1, normals[gx + 1][gz].x, 0), new Vector3f(1, normals[gx + 1][gz + 1].x, 1), new Vector3f(0, normals[gx][gz + 1].x, 1), new Vector2f(xCoord, zCoord));
			float y = Maths.barryCentric(new Vector3f(1, normals[gx + 1][gz].y, 0), new Vector3f(1, normals[gx + 1][gz + 1].y, 1), new Vector3f(0, normals[gx][gz + 1].y, 1), new Vector2f(xCoord, zCoord));
			float z = Maths.barryCentric(new Vector3f(1, normals[gx + 1][gz].z, 0), new Vector3f(1, normals[gx + 1][gz + 1].z, 1), new Vector3f(0, normals[gx][gz + 1].z, 1), new Vector2f(xCoord, zCoord));

			return new Vector3f(x, y, z).normalize();

		}

	}

	public float getHeight(float wx, float wz) {

		float tx = wx - 0;
		float tz = wz - 0;
		float gridSquareSize = size / (heights.length - 1);
		int gx = (int) Math.floor(tx / gridSquareSize);
		int gz = (int) Math.floor(tz / gridSquareSize);

		if (gx < 0 || gz < 0 || gx >= heights.length - 1 || gz >= heights.length - 1) return 0;

		float xCoord = (tx % gridSquareSize) / gridSquareSize;
		float zCoord = (tz % gridSquareSize) / gridSquareSize;

		if (xCoord <= (1 - zCoord)) {
			return Maths.barryCentric(new Vector3f(0, heights[gx][gz], 0), new Vector3f(1, heights[gx + 1][gz], 0), new Vector3f(0, heights[gx][gz + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			return Maths.barryCentric(new Vector3f(1, heights[gx + 1][gz], 0), new Vector3f(1, heights[gx + 1][gz + 1], 1), new Vector3f(0, heights[gx][gz + 1], 1), new Vector2f(xCoord, zCoord));
		}

	}

	private void generate(float size) {

		int count = vertexCount * vertexCount;

		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];

		int vertexPointer = 0;

		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {

				vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * size;
				vertices[vertexPointer * 3 + 1] = heights[j][i];
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * size;

				Vector3f normal = calcNormal(j, i);
				this.normals[j][i] = normal;

				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;

				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				vertexPointer++;
			}
		}

		int pointer = 0;
		for (int gz = 0; gz < vertexCount - 1; gz++) {
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
		}

		data = new ModelData(vertices, textureCoords, normals, null, indices);

	}

	private float getHeight(int x, int z) {

		if (x < 0) x = 0;
		if (z < 0) z = 0;
		if (x >= vertexCount) x = vertexCount - 1;
		if (z >= vertexCount) z = vertexCount - 1;

		return heights[x][z];

	}

	private Vector3f calcNormal(int x, int z) {

		float heightL = getHeight(x - 1, z);
		float heightR = getHeight(x + 1, z);
		float heightD = getHeight(x, z - 1);
		float heightU = getHeight(x, z + 1);

		return new Vector3f(heightL - heightR, 2F, heightD - heightU).normalize();

	}

}
