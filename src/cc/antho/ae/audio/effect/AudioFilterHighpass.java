package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioFilterHighpass extends AudioFilter {

	private float gain;
	private float gainLf;

	public AudioFilterHighpass() {

		super(AL_FILTER_HIGHPASS);

		setGain(1.0f);
		setGainLf(1.0f);

	}

	public void setGain(float gain) {

		this.gain = gain;
		alFilterf(handle, AL_HIGHPASS_GAIN, gain);
		load();

	}

	public void setGainLf(float gainLf) {

		this.gainLf = gainLf;
		alFilterf(handle, AL_HIGHPASS_GAINLF, gainLf);
		load();

	}

}
