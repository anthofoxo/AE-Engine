package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;

import org.joml.Vector3f;

import lombok.Getter;

public class AudioSource {

	@Getter private final int handle;
	@Getter private float gain = 1f;
	@Getter private float pitch = 1f;
	private AudioManager manager;

	AudioSource(AudioManager manager) {

		handle = alGenSources();
		this.manager = manager;

		set3D();

	}

	public void set(AudioSettings settings) {

		setGain(settings.getGain());
		setPitch(settings.getPitch());
		setRelative(settings.isRelative());
		setPosition(settings.getPosition());
		setProperties(settings.getRolloffFactor(), settings.getReferenceDistance(), settings.getMaxDistance());
		setLooping(settings.isLooping());

	}

	public final void setRelative(final boolean relative) {

		alSourcei(handle, AL_SOURCE_RELATIVE, relative ? 1 : 0);

	}

	public final void setProperties(final float rolloffFactor, final float referenceDistance, final float maxDistance) {

		alSourcef(handle, AL_ROLLOFF_FACTOR, rolloffFactor);
		alSourcef(handle, AL_REFERENCE_DISTANCE, referenceDistance);
		if (maxDistance >= 0) alSourcef(handle, AL_MAX_DISTANCE, maxDistance);

	}

	public final void set2D() {

		setPosition(new Vector3f());
		setProperties(0, 1, 0);
		setRelative(true);

	}

	public final void set3D() {

		setPosition(new Vector3f());
		setProperties(1, 1, 1000);
		setRelative(false);

	}

	public void setBuffer(final AudioBuffer buffer) {

		alSourcei(handle, AL_BUFFER, buffer.getHandle());

	}

	public void setLooping(final boolean looping) {

		alSourcei(handle, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);

	}

	public void setPitch(final float pitch) {

		this.pitch = pitch;
		alSourcef(handle, AL_PITCH, pitch);

	}

	public void setGain(final float gain) {

		this.gain = gain;
		manager.update(this);

	}

	public boolean isPlaying() {

		return alGetSourcei(handle, AL_SOURCE_STATE) == AL_PLAYING;

	}

	public void play() {

		alSourcePlay(handle);

	}

	public void stop() {

		alSourceStop(handle);

	}

	public void pause() {

		alSourcePause(handle);

	}

	public void setPosition(Vector3f position) {

		alSource3f(handle, AL_POSITION, position.x, position.y, position.z);

	}

	public void destroy() {

		alDeleteSources(handle);

	}

}
