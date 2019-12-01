package cc.antho.ae.editor;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.AL.*;
import static org.lwjgl.openal.EXTEfx.*;

import cc.antho.ae.audio.AudioBuffer;
import cc.antho.ae.audio.AudioManager;
import cc.antho.ae.audio.AudioSettings;
import cc.antho.ae.audio.AudioSource;
import cc.antho.ae.audio.effect.AudioEffect;
import cc.antho.ae.audio.effect.AudioEffectDistort;
import cc.antho.ae.audio.effect.AudioEffectSlot;
import cc.antho.ae.audio.effect.AudioFilter;
import cc.antho.ae.audio.effect.AudioFilterLowpass;

public class ALTest {

	private static AudioFilterLowpass filter;
	private static AudioSource source;

	private static AudioEffectSlot slot;
	private static AudioEffectDistort effect;

	static void init(AudioManager mgr) {

		effect = new AudioEffectDistort();
		effect.setEdge(.7f);
		effect.setGain(.3f);

		slot = new AudioEffectSlot();
		slot.setEffect(effect);

		filter = new AudioFilterLowpass();
		filter.setFreqGain(1f);
		filter.setHighFreqGain(0f);

		source = mgr.play(new AudioBuffer("/music/meat_boy.ogg"), AudioSettings.generate2DLooped());
		source.setDirectFilter(filter);
		source.setAuxSend(0, null, filter);
		source.setPitch(.6f);

//		effect = alGenEffects();
//		alEffecti(effect, AL_EFFECT_TYPE, AL_EFFECT_REVERB);
//		alEffectf(effect, AL_REVERB_DECAY_TIME, 5f);
//		alEffecti(effect, AL_EFFECT_TYPE, AL_EFFECT_REVERB);
//		alEffectf(effect, AL_REVERB_DENSITY, 0.4f);
//		alEffectf(effect, AL_REVERB_DIFFUSION, .1f);
//		alEffectf(effect, AL_REVERB_GAIN, .4f);
//		alEffectf(effect, AL_REVERB_DECAY_TIME, 5f);

	}

	static void render() {

	}

	static void destroy() {

		filter.destroy();
		slot.destroy();
		effect.destroy();

	}

}
