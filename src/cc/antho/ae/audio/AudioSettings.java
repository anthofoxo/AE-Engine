package cc.antho.ae.audio;

import org.joml.Vector3f;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class AudioSettings {

	public float gain;
	public float pitch;
	public boolean relative;
	public boolean looping;
	public Vector3f position;
	public float rolloffFactor;
	public float referenceDistance;
	public float maxDistance;

	public static AudioSettings generate2D() {

		return new AudioSettings(1f, 1f, true, false, new Vector3f(), 0f, 1f, 0f);

	}

	public static AudioSettings generate2DLooped() {

		return new AudioSettings(1f, 1f, true, true, new Vector3f(), 0f, 1f, 0f);

	}

	public static AudioSettings generate3D(Vector3f position) {

		return new AudioSettings(1f, 1f, false, false, new Vector3f(position), 1f, 1f, 1000f);

	}

}
