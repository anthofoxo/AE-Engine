package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioFilterBandpass extends AudioFilter {

	private float gain = 1f;
	private float gainLf = 1f;
	private float gainHf = 1f;

	public AudioFilterBandpass() {

		super(AL_FILTER_BANDPASS);

	}

	public void setGain(float gain) {

		this.gain = gain;
		alFilterf(handle, AL_BANDPASS_GAIN, gain);
		load();

	}

	public void setGainLf(float gainLf) {

		this.gainLf = gainLf;
		alFilterf(handle, AL_BANDPASS_GAINLF, gainLf);
		load();

	}

	public void setGainHf(float gainHf) {

		this.gainHf = gainHf;
		alFilterf(handle, AL_BANDPASS_GAINHF, gainHf);
		load();

	}

}
