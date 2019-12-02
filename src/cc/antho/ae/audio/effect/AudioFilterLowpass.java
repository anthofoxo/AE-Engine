package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioFilterLowpass extends AudioFilter {

	private float gain;
	private float gainHf;

	public AudioFilterLowpass() {

		super(AL_FILTER_LOWPASS);

		setGain(1.0f);
		setGainHf(1.0f);

	}

	public void setGain(float gain) {

		this.gain = gain;
		alFilterf(handle, AL_LOWPASS_GAIN, gain);
		load();

	}

	public void setGainHf(float gainHf) {

		this.gainHf = gainHf;
		alFilterf(handle, AL_LOWPASS_GAINHF, gainHf);
		load();

	}

}
