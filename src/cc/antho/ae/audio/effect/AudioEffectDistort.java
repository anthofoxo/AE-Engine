package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

import lombok.Getter;

@Getter
public final class AudioEffectDistort extends AudioEffect {

	private float edge;
	private float gain;
	private float lowpassCutoff;
	private float eqCenter;
	private float eqBandwidth;

	public AudioEffectDistort() {

		super(AL_EFFECT_DISTORTION);

		setEdge(0.2f);
		setGain(0.05f);
		setLowpassCutoff(8000f);
		setEqCenter(3600f);
		setEqBandwidth(eqBandwidth);

	}

	public void setEdge(float edge) {

		this.edge = edge;
		alEffectf(handle, AL_DISTORTION_EDGE, edge);
		load();

	}

	public void setGain(float gain) {

		this.gain = gain;
		alEffectf(handle, AL_DISTORTION_GAIN, gain);
		load();

	}

	public void setLowpassCutoff(float lowpassCutoff) {

		this.lowpassCutoff = lowpassCutoff;
		alEffectf(handle, AL_DISTORTION_LOWPASS_CUTOFF, lowpassCutoff);
		load();

	}

	public void setEqCenter(float eqCenter) {

		this.eqCenter = eqCenter;
		alEffectf(handle, AL_DISTORTION_EQCENTER, eqCenter);
		load();

	}

	public void setEqBandwidth(float eqBandwidth) {

		this.eqBandwidth = eqBandwidth;
		alEffectf(handle, AL_DISTORTION_EQBANDWIDTH, eqBandwidth);
		load();

	}

}
