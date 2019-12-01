package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.EXTEfx.*;

import org.joml.Vector3f;

import cc.antho.ae.audio.effect.AudioEffectSlot;
import cc.antho.ae.audio.effect.AudioFilter;
import cc.antho.ae.common.Destroyable;
import cc.antho.ae.log.Logger;
import lombok.Getter;

@Getter
public final class AudioSource implements Destroyable {

	private int handle;
	private float gain = 1f;
	private float pitch = 1f;
	private AudioManager manager;
	private AudioFilter filter;

	private AudioEffectSlot[] effectSlots;
	private AudioFilter[] filters;

	public AudioSource(int handle, AudioManager manager, AudioSettings settings) {

		Logger.debug("Creating audio source");

		this.handle = handle;
		this.manager = manager;

		effectSlots = new AudioEffectSlot[manager.getDevice().getMaxAuxSends()];
		filters = new AudioFilter[manager.getDevice().getMaxAuxSends()];

		set(settings);

	}

	public AudioSource(AudioManager manager, AudioSettings settings) {

		this(alGenSources(), manager, settings);

	}

	public void set(AudioSettings settings) {

		setGain(settings.gain);
		setPitch(settings.pitch);
		setRelative(settings.relative);
		setPosition(settings.position);
		setAttenuation(settings.rolloffFactor, settings.referenceDistance, settings.maxDistance);
		setLooping(settings.looping);

	}

	public void setRelative(boolean relative) {

		alSourcei(handle, AL_SOURCE_RELATIVE, relative ? 1 : 0);

	}

	public void setAttenuation(float rolloffFactor, float referenceDistance, float maxDistance) {

		alSourcef(handle, AL_ROLLOFF_FACTOR, rolloffFactor);
		alSourcef(handle, AL_REFERENCE_DISTANCE, referenceDistance);
		alSourcef(handle, AL_MAX_DISTANCE, maxDistance);

	}

	public void setBuffer(AudioBuffer buffer) {

		alSourcei(handle, AL_BUFFER, buffer.getHandle());

	}

	public void setLooping(boolean looping) {

		alSourcei(handle, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);

	}

	public void setPitch(float pitch) {

		alSourcef(handle, AL_PITCH, this.pitch = pitch);

	}

	public void setGain(float gain) {

		this.gain = gain;
		manager.update(this);

	}

	public void setDirectFilter(AudioFilter filter) {

		if (filter != null) {

			if (this.filter != null) this.filter.getAttached().remove(this);
			filter.getAttached().add(this);

		}

		this.filter = filter;

		if (filter == null) alSourcei(handle, AL_DIRECT_FILTER, AL_FILTER_NULL);
		else alSourcei(handle, AL_DIRECT_FILTER, filter.getHandle());

	}

	public void setAuxSend(int index, AudioEffectSlot slot, AudioFilter filter) {

		if (this.effectSlots[index] != null) {

			this.effectSlots[index].getAttached().remove(this);
			this.effectSlots[index].getIndexes().remove(index);

		}

		if (slot != null) {

			slot.getAttached().add(this);
			slot.getIndexes().add(index);

		}

		if (this.filters[index] != null) this.filters[index].getAttached().remove(this);
		if (filter != null) filter.getAttached().add(this);

		alSource3i(handle, AL_AUXILIARY_SEND_FILTER, slot == null ? AL_EFFECTSLOT_NULL : slot.getHandle(), 0, filter == null ? AL_EFFECT_NULL : filter.getHandle());

		effectSlots[index] = slot;
		filters[index] = filter;

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

		Logger.debug("Destroying audio source");

		if (filter != null) filter.getAttached().remove(this);

		for (int i = 0; i < filters.length; i++) {

			if (filters[i] != null) filters[i].getAttached().remove(this);
			if (effectSlots[i] != null) effectSlots[i].getAttached().remove(this);

			filters[i] = null;
			effectSlots[i] = null;

		}

		alDeleteSources(handle);
		handle = 0;
		manager = null;
		filter = null;
		filters = null;
		effectSlots = null;

	}

}
