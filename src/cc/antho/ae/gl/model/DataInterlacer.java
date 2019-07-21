package cc.antho.ae.gl.model;

public final class DataInterlacer {

	private DataInterlacer() {

	}

	public static final float[] interlace(Dataset... datasets) {

		Dataset currentSet;
		int totalFloats = 0;

		for (int i = 0; i < datasets.length; i++) {

			datasets[i].position = 0;
			totalFloats += datasets[i].getData().length;

		}

		float[] data = new float[totalFloats];

		for (int i = 0; i < data.length;) {

			for (int j = 0; j < datasets.length; j++) {

				currentSet = datasets[j];
				System.arraycopy(currentSet.getData(), currentSet.position, data, i, currentSet.getSize());
				currentSet.position += currentSet.getSize();
				i += currentSet.getSize();

			}

		}

		return data;

	}

}
