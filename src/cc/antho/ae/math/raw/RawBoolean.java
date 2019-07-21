package cc.antho.ae.math.raw;

/**
 * A simpler, less memory intensive version of the Boolean class
 */
public class RawBoolean {

	public boolean value;

	public RawBoolean() {

		this(false);

	}

	public RawBoolean(boolean value) {

		this.value = value;

	}

}
