package cc.antho.ae.math.builder;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import cc.antho.ae.math.Maths;
import cc.antho.ae.math.raw.RawFloat;

public class FloatBuilder extends DataBuilder {

	private List<RawFloat> list = new ArrayList<>();

	public void append(float value) {

		list.add(new RawFloat(value));

	}

	public void append(float... values) {

		for (float f : values)
			list.add(new RawFloat(f));

	}

	public void append(FloatBuilder values) {

		append(values.toArray());

	}

	public void set(int index, float value) {

		list.get(index).value = value;

	}

	public float get(int index) {

		return list.get(index).value;

	}

	public void add(float x, float y) {

		for (int i = 0; i < list.size(); i += 2) {

			list.get(i + 0).value += x;
			list.get(i + 1).value += y;

		}

	}

	public void add(float x, float y, float z) {

		for (int i = 0; i < list.size(); i += 3) {

			list.get(i + 0).value += x;
			list.get(i + 1).value += y;
			list.get(i + 2).value += z;

		}

	}

	public void add(int... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value += dims[j];

	}

	public void add(float... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value += dims[j];

	}

	public void sub(float... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value -= dims[j];

	}

	public void mul(float... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value *= dims[j];

	}

	public void div(float... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value /= dims[j];

	}

	public void mul(float x, float y, float z) {

		for (int i = 0; i < list.size(); i += 3) {

			list.get(i + 0).value *= x;
			list.get(i + 1).value *= y;
			list.get(i + 2).value *= z;

		}

	}

	public void mul(float x, float y) {

		for (int i = 0; i < list.size(); i += 2) {

			list.get(i + 0).value *= x;
			list.get(i + 1).value *= y;

		}

	}

	public float[] toArray() {

		float[] array = new float[list.size()];

		for (int i = 0; i < array.length; i++)
			array[i] = list.get(i).value;

		return array;

	}

	public void rotate3y(float rotation) {

		for (int i = 0; i < list.size(); i += 3) {

			Vector3f pos = new Vector3f();

			pos.x = list.get(i + 0).value;
			pos.y = list.get(i + 1).value;
			pos.z = list.get(i + 2).value;

			pos.rotateY(Maths.toRadians(rotation));

			list.get(i + 0).value = pos.x;
			list.get(i + 1).value = pos.y;
			list.get(i + 2).value = pos.z;

		}

	}

	public int size() {

		return list.size();

	}

	public void clear() {

		list.clear();

	}

}
