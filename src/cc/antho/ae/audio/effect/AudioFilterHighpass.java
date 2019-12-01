package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioFilterHighpass extends AudioFilter {

	private float freqGain = 1f;
	private float lowFreqGain = 1f;

	public AudioFilterHighpass() {

		super(AL_FILTER_HIGHPASS);

	}

	public void load() {

		alFilterf(handle, AL_HIGHPASS_GAIN, freqGain);
		alFilterf(handle, AL_HIGHPASS_GAINLF, lowFreqGain);

	}

	public void setFreqGain(float freqGain) {

		this.freqGain = freqGain;
		fullLoad();

	}

	public void setLowFreqGain(float lowFreqGain) {

		this.lowFreqGain = lowFreqGain;
		fullLoad();

	}

}
