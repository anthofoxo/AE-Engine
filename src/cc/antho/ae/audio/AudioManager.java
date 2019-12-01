package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

import org.joml.Vector3f;

import lombok.Getter;

public final class AudioManager {

	@Getter private float gain = 1f;

	@Getter private AudioDevice device;
	@Getter private AudioContext context;

	private AudioSource[] sources;

	public AudioManager(int maxSources) {

		device = new AudioDevice(alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER));
		context = new AudioContext(device);

		createSources(maxSources);

	}

	private void createSources(int maxSources) {

		sources = new AudioSource[maxSources];

		for (int i = 0; i < this.sources.length; i++)
			sources[i] = new AudioSource(this, AudioSettings.generate3D(new Vector3f()));

	}

	private void destroySources() {

		for (int i = 0; i < sources.length; i++)
			sources[i].destroy();

		sources = null;

	}

	public AudioSource play(AudioBuffer buffer, AudioSettings settings) {

		for (int i = 0; i < sources.length; i++) {

			AudioSource source = sources[i];
			if (!source.isPlaying()) return play(source, buffer, settings);

		}

		return null;

	}

	public AudioSource play(AudioSource source, AudioBuffer buffer, AudioSettings settings) {

		source.set(settings);
		source.setBuffer(buffer);
		source.play();

		return source;

	}

	void update(AudioSource source) {

		alSourcef(source.getHandle(), AL_GAIN, gain * source.getGain());

	}

	public void setGain(float gain) {

		this.gain = gain;

		for (int i = 0; i < sources.length; i++)
			update(sources[i]);

	}

	public final void stopAll() {

		for (int i = 0; i < sources.length; i++)
			sources[i].stop();

	}

	public final void destroy() {

		stopAll();
		destroySources();

		context.destroy();
		context = null;

		device.destroy();
		device = null;

	}

	public int getUsedSources() {

		int count = 0;

		for (int i = 0; i < sources.length; i++)
			if (sources[i].isPlaying()) count++;

		return count;

	}

	public int getMaxSources() {

		return sources.length;

	}

}
