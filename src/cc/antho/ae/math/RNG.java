package cc.antho.ae.math;

import java.util.Random;
import java.util.Stack;

public final class RNG {

	private RNG() {

	}

	private static Stack<Random> stack = new Stack<>();

	static {

		push(new Random());

	}

	public static void push(long seed) {

		push(new Random(seed));

	}

	public static void push(Random random) {

		stack.push(random);

	}

	public static Random pop() {

		return stack.pop();

	}

	public static Random peek() {

		return stack.peek();

	}

	public static float nextFloat(float min, float max) {

		return Maths.map(peek().nextFloat(), 0F, 1F, min, max);

	}

	public static int nextInt(int min, int max) {

		return peek().nextInt((max - min) + 1) + min;

	}

	public static boolean nextBoolean() {

		return peek().nextBoolean();

	}

}
