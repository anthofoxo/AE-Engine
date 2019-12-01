package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;
import static org.lwjgl.openal.AL10.*;

import lombok.Getter;

@Getter
public final class AudioEffectReverb extends AudioEffect {

	private float density = 1.0f;
	private float diffusion = 1.0f;
	private float gain = 0.32f;
	private float gainHf = 0.89f;
	private float decayTime = 1.49f;
	private float decayHfRatio = 0.83f;
	private float reflectionsGain = 0.05f;
	private float reflectionsDelay = 0.007f;
	private float lateReverbGain = 1.26f;
	private float lateReverbDelay = 0.011f;
	private float airObsorptionGainHf = 0.994f;
	private float roomRolloffFactor = 0.0f;
	private boolean hfLimit = true;

	public AudioEffectReverb() {

		super(AL_EFFECT_REVERB);

		load();

	}

	public void load() {

		alEffectf(handle, AL_REVERB_DENSITY, density);
		alEffectf(handle, AL_REVERB_DIFFUSION, diffusion);
		alEffectf(handle, AL_REVERB_GAIN, gain);
		alEffectf(handle, AL_REVERB_GAINHF, gainHf);
		alEffectf(handle, AL_REVERB_DECAY_TIME, decayTime);
		alEffectf(handle, AL_REVERB_DECAY_HFRATIO, decayHfRatio);
		alEffectf(handle, AL_REVERB_REFLECTIONS_GAIN, reflectionsGain);
		alEffectf(handle, AL_REVERB_REFLECTIONS_DELAY, reflectionsDelay);
		alEffectf(handle, AL_REVERB_LATE_REVERB_GAIN, lateReverbGain);
		alEffectf(handle, AL_REVERB_LATE_REVERB_DELAY, lateReverbDelay);
		alEffectf(handle, AL_REVERB_AIR_ABSORPTION_GAINHF, airObsorptionGainHf);
		alEffectf(handle, AL_REVERB_ROOM_ROLLOFF_FACTOR, roomRolloffFactor);
		alEffecti(handle, AL_REVERB_DECAY_HFLIMIT, hfLimit ? AL_TRUE : AL_FALSE);

	}

	public void setDensity(float density) {

		this.density = density;
		fullLoad();

	}

	public void setDiffusion(float diffusion) {

		this.diffusion = diffusion;
		fullLoad();

	}

	public void setGain(float gain) {

		this.gain = gain;
		fullLoad();

	}

	public void setGainHf(float gainHf) {

		this.gainHf = gainHf;
		fullLoad();

	}

	public void setDecayTime(float decayTime) {

		this.decayTime = decayTime;
		fullLoad();

	}

	public void setDecayHfRatio(float decayHfRatio) {

		this.decayHfRatio = decayHfRatio;
		fullLoad();

	}

	public void setReflectionsGain(float reflectionsGain) {

		this.reflectionsGain = reflectionsGain;
		fullLoad();

	}

	public void setReflectionsDelay(float reflectionsDelay) {

		this.reflectionsDelay = reflectionsDelay;
		fullLoad();

	}

	public void setLateReverbGain(float lateReverbGain) {

		this.lateReverbGain = lateReverbGain;
		fullLoad();

	}

	public void setLateReverbDelay(float lateReverbDelay) {

		this.lateReverbDelay = lateReverbDelay;
		fullLoad();

	}

	public void setAirObsorptionGainHf(float airObsorptionGainHf) {

		this.airObsorptionGainHf = airObsorptionGainHf;
		fullLoad();

	}

	public void setRoomRolloffFactor(float roomRolloffFactor) {

		this.roomRolloffFactor = roomRolloffFactor;
		fullLoad();

	}

	public void setHfLimit(boolean hfLimit) {

		this.hfLimit = hfLimit;
		fullLoad();

	}

}
