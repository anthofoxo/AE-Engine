package cc.antho.ae.time;

public final class HiResTimeProvider implements TimeProvider {

	public double getTime() {

		return System.nanoTime() / 1000000000D;

	}

}
