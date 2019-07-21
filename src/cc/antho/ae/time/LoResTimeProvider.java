package cc.antho.ae.time;

public final class LoResTimeProvider implements TimeProvider {

	public double getTime() {

		return System.currentTimeMillis() / 1000D;

	}

}
