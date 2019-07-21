package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class AudioManager {

	private float gain = 1f;

	@Getter private long device, context;

	private AudioSource[] sources;

	private Map<String, String> bufferRegisterMap = new HashMap<>();
	private Map<String, AudioBuffer> bufferMap = new HashMap<>();

	public void registerBufferMapping(String key, String value) {

		if (value == null) {

			bufferRegisterMap.remove(key);
			bufferMap.remove(key).destroy();

		} else {

			bufferRegisterMap.put(key, value);
			bufferMap.put(key, new AudioBuffer(value));

		}

	}

	public AudioManager(int numSources) {

		sources = new AudioSource[numSources];

		createAudioDevice();
		createAudioContext();
		createSources();

		Logger.info("Initialized OpenAL");

	}

	private void createAudioDevice() {

		final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);

	}

	private void createAudioContext() {

		final int[] attributes = { 0 };
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		final ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		final ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		if (!alCapabilities.OpenAL10) Logger.error("OpenAL 1.0 is not supported");
		if (!alCapabilities.OpenAL11) Logger.error("OpenAL 1.1 is not supported");

	}

	private void createSources() {

		for (int i = 0; i < sources.length; i++)
			sources[i] = new AudioSource(this);

	}

	private void destroySources() {

		for (int i = 0; i < sources.length; i++)
			sources[i].destroy();

	}

	public AudioSource playAudio(String buffer, AudioSettings settings) {

		if (buffer == null) return null;

		return playAudio(bufferMap.get(buffer), settings);

	}

	public AudioSource playAudio(AudioBuffer buffer, AudioSettings settings) {

		for (int i = 0; i < sources.length; i++) {

			AudioSource source = sources[i];

			if (!source.isPlaying()) return playAudio(buffer, settings, source);

		}

		return null;

	}

	private AudioSource playAudio(AudioBuffer buffer, AudioSettings settings, AudioSource source) {

		source.set(settings);
		source.setBuffer(buffer);
		source.play();

		return source;

	}

	void update(final AudioSource source) {

		alSourcef(source.getHandle(), AL_GAIN, gain * source.getGain());

	}

	public final void setGain(final float p) {

		gain = p;

		for (int i = 0; i < sources.length; i++)
			update(sources[i]);

	}

	public final void stopAll() {

		for (int i = 0; i < sources.length; i++)
			sources[i].stop();

	}

	public final float getGain() {

		return gain;

	}

	public final void destroy() {

		stopAll();
		destroySources();

		for (AudioBuffer buffer : bufferMap.values())
			buffer.destroy();

		bufferMap.clear();
		bufferRegisterMap.clear();

		alcDestroyContext(context);
		alcCloseDevice(device);
		Logger.info("Terminated OpenAL");

	}

	public int getUsedSources() {

		int count = 0;

		for (AudioSource source : sources)
			if (source.isPlaying()) count++;

		return count;

	}

	public int getMaxSources() {

		return sources.length;

	}

}
