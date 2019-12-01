package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioEffectDistort extends AudioEffect {

	private float edge = 0.2f;
	private float gain = 0.05f;
	private float lowpassCutoff = 8000f;
	private float eqCenter = 3600f;
	private float eqBandwidth = 3600f;

	public AudioEffectDistort() {

		super(AL_EFFECT_DISTORTION);

		load();

	}

	public void load() {

		alEffectf(handle, AL_DISTORTION_EDGE, edge);
		alEffectf(handle, AL_DISTORTION_GAIN, gain);
		alEffectf(handle, AL_DISTORTION_LOWPASS_CUTOFF, lowpassCutoff);
		alEffectf(handle, AL_DISTORTION_EQCENTER, eqCenter);
		alEffectf(handle, AL_DISTORTION_EQBANDWIDTH, eqBandwidth);

	}

	public void setEdge(float edge) {

		this.edge = edge;
		fullLoad();

	}

	public void setGain(float gain) {

		this.gain = gain;
		fullLoad();

	}

	public void setLowpassCutoff(float lowpassCutoff) {

		this.lowpassCutoff = lowpassCutoff;
		fullLoad();

	}

	public void setEqCenter(float eqCenter) {

		this.eqCenter = eqCenter;
		fullLoad();

	}

	public void setEqBandwidth(float eqBandwidth) {

		this.eqBandwidth = eqBandwidth;
		fullLoad();

	}

}
