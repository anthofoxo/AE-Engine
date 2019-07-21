package cc.antho.ae.math.raw;

/**
 * A simpler, less memory intensive version of the Byte class
 */
public class RawByte {

	public byte value;

	public RawByte() {

		this((byte) 0);

	}

	public RawByte(byte value) {

		this.value = value;

	}

}
