package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioFilterBandpass extends AudioFilter {

	private float freqGain = 1f;
	private float lowFreqGain = 1f;
	private float highFreqGain = 1f;

	public AudioFilterBandpass() {

		super(AL_FILTER_BANDPASS);

	}

	public void load() {

		alFilterf(handle, AL_BANDPASS_GAIN, freqGain);
		alFilterf(handle, AL_BANDPASS_GAINLF, lowFreqGain);
		alFilterf(handle, AL_BANDPASS_GAINHF, highFreqGain);

	}

	public void setFreqGain(float freqGain) {

		this.freqGain = freqGain;
		fullLoad();

	}

	public void setLowFreqGain(float lowFreqGain) {

		this.lowFreqGain = lowFreqGain;
		fullLoad();

	}

	public void setHighFreqGain(float highFreqGain) {

		this.highFreqGain = highFreqGain;
		fullLoad();

	}

}
