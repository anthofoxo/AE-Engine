package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;
import static org.lwjgl.openal.AL10.*;

import lombok.Getter;

@Getter
public final class AudioEffectReverb extends AudioEffect {

	private float density;
	private float diffusion;
	private float gain;
	private float gainHf;
	private float decayTime;
	private float decayHfRatio;
	private float reflectionsGain;
	private float reflectionsDelay;
	private float lateReverbGain;
	private float lateReverbDelay;
	private float airObsorptionGainHf;
	private float roomRolloffFactor;
	private boolean hfLimit;

	public AudioEffectReverb() {

		super(AL_EFFECT_REVERB);

		setDensity(1.0f);
		setDiffusion(1.0f);
		setGain(0.32f);
		setGainHf(0.89f);
		setDecayTime(1.49f);
		setDecayHfRatio(0.83f);
		setReflectionsGain(0.05f);
		setReflectionsDelay(0.007f);
		setLateReverbGain(1.26f);
		setLateReverbDelay(0.011f);
		setAirObsorptionGainHf(0.994f);
		setRoomRolloffFactor(0.0f);
		setHfLimit(true);

	}

	public void setDensity(float density) {

		this.density = density;
		alEffectf(handle, AL_REVERB_DENSITY, density);
		load();

	}

	public void setDiffusion(float diffusion) {

		this.diffusion = diffusion;
		alEffectf(handle, AL_REVERB_DIFFUSION, diffusion);
		load();

	}

	public void setGain(float gain) {

		this.gain = gain;
		alEffectf(handle, AL_REVERB_GAIN, gain);
		load();

	}

	public void setGainHf(float gainHf) {

		this.gainHf = gainHf;
		alEffectf(handle, AL_REVERB_GAINHF, gainHf);
		load();

	}

	public void setDecayTime(float decayTime) {

		this.decayTime = decayTime;
		alEffectf(handle, AL_REVERB_DECAY_TIME, decayTime);
		load();

	}

	public void setDecayHfRatio(float decayHfRatio) {

		this.decayHfRatio = decayHfRatio;
		alEffectf(handle, AL_REVERB_DECAY_HFRATIO, decayHfRatio);
		load();

	}

	public void setReflectionsGain(float reflectionsGain) {

		this.reflectionsGain = reflectionsGain;
		alEffectf(handle, AL_REVERB_REFLECTIONS_GAIN, reflectionsGain);
		load();

	}

	public void setReflectionsDelay(float reflectionsDelay) {

		this.reflectionsDelay = reflectionsDelay;
		alEffectf(handle, AL_REVERB_REFLECTIONS_DELAY, reflectionsDelay);
		load();

	}

	public void setLateReverbGain(float lateReverbGain) {

		this.lateReverbGain = lateReverbGain;
		alEffectf(handle, AL_REVERB_LATE_REVERB_GAIN, lateReverbGain);
		load();

	}

	public void setLateReverbDelay(float lateReverbDelay) {

		this.lateReverbDelay = lateReverbDelay;
		alEffectf(handle, AL_REVERB_LATE_REVERB_DELAY, lateReverbDelay);
		load();

	}

	public void setAirObsorptionGainHf(float airObsorptionGainHf) {

		this.airObsorptionGainHf = airObsorptionGainHf;
		alEffectf(handle, AL_REVERB_AIR_ABSORPTION_GAINHF, airObsorptionGainHf);
		load();

	}

	public void setRoomRolloffFactor(float roomRolloffFactor) {

		this.roomRolloffFactor = roomRolloffFactor;
		alEffectf(handle, AL_REVERB_ROOM_ROLLOFF_FACTOR, roomRolloffFactor);
		load();

	}

	public void setHfLimit(boolean hfLimit) {

		this.hfLimit = hfLimit;
		alEffecti(handle, AL_REVERB_DECAY_HFLIMIT, hfLimit ? AL_TRUE : AL_FALSE);
		load();

	}

}
