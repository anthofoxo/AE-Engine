package cc.antho.ae.audio;

import org.joml.Vector3f;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents settings for a sound effect to be played, this includes volume,
 * position, attenuation factors and looping.
 * 
 * An instance of this class may be created by calling on of the generate
 * methods.
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class AudioSettings {

	private float gain;
	private float pitch;
	private boolean relative;
	private boolean looping;
	private Vector3f position;
	private float rolloffFactor;
	private float referenceDistance;
	private float maxDistance;

	public static AudioSettings generate(float gain, float pitch, boolean relative, boolean looping, Vector3f position, float rolloffFactor, float referenceDistance, float maxDistance) {

		return new AudioSettings(gain, pitch, relative, looping, position, rolloffFactor, referenceDistance, maxDistance);

	}

	public static AudioSettings generate2D() {

		return new AudioSettings(1F, 1F, true, false, new Vector3f(), 0F, 1F, 0F);

	}

	public static AudioSettings generate2DLooped() {

		return new AudioSettings(1F, 1F, true, true, new Vector3f(), 0F, 1F, 0F);

	}

	public static AudioSettings generate3D(Vector3f position) {

		return new AudioSettings(1F, 1F, false, false, new Vector3f(position), 1F, 1F, 1000F);

	}

}
