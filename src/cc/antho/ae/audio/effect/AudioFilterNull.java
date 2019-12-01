package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.EXTEfx.*;

public final class AudioFilterNull extends AudioFilter {

	public AudioFilterNull() {

		super(AL_FILTER_NULL);

	}

	public void load() {

	}

}
