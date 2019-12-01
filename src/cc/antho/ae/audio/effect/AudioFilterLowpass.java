package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioFilterLowpass extends AudioFilter {

	private float freqGain = 1f;
	private float highFreqGain = 1f;

	public AudioFilterLowpass() {

		super(AL_FILTER_LOWPASS);

	}

	public void load() {

		alFilterf(handle, AL_LOWPASS_GAIN, freqGain);
		alFilterf(handle, AL_LOWPASS_GAINHF, highFreqGain);

	}

	public void setFreqGain(float freqGain) {

		this.freqGain = freqGain;
		fullLoad();

	}

	public void setHighFreqGain(float highFreqGain) {

		this.highFreqGain = highFreqGain;
		fullLoad();

	}

}
