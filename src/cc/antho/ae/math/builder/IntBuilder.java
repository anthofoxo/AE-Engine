package cc.antho.ae.math.builder;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.math.raw.RawInt;

public class IntBuilder extends DataBuilder {

	private List<RawInt> list = new ArrayList<>();

	public void append(int value) {

		list.add(new RawInt(value));

	}

	public void append(int... values) {

		for (int f : values)
			list.add(new RawInt(f));

	}

	public void append(IntBuilder values) {

		append(values.toArray());

	}

	public void add(int x, int y) {

		for (int i = 0; i < list.size(); i += 2) {

			list.get(i + 0).value += x;
			list.get(i + 1).value += y;

		}

	}

	public void add(int x, int y, int z) {

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

	public void sub(int... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value -= dims[j];

	}

	public void mul(int... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value *= dims[j];

	}

	public void div(int... dims) {

		for (int i = 0; i < list.size(); i += dims.length)
			for (int j = 0; j < dims.length; j++)
				list.get(i + j).value /= dims[j];

	}

	public void mul(int x, int y, int z) {

		for (int i = 0; i < list.size(); i += 3) {

			list.get(i + 0).value *= x;
			list.get(i + 1).value *= y;
			list.get(i + 2).value *= z;

		}

	}

	public void mul(int x, int y) {

		for (int i = 0; i < list.size(); i += 2) {

			list.get(i + 0).value *= x;
			list.get(i + 1).value *= y;

		}

	}

	public int[] toArray() {

		int[] array = new int[list.size()];

		for (int i = 0; i < array.length; i++)
			array[i] = list.get(i).value;

		return array;

	}

	public int size() {

		return list.size();

	}

	public void clear() {

		list.clear();

	}

}
