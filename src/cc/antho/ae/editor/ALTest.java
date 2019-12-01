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
import cc.antho.ae.audio.effect.AudioFilter;
import cc.antho.ae.audio.effect.AudioFilterLowpass;

public class ALTest {

	private static int auxEffectSlot;
	private static int effect;
	private static AudioFilterLowpass filter;
	private static AudioSource source;

	static void init(AudioManager mgr) {

//		auxEffectSlot = alGenAuxiliaryEffectSlots();
//		effect = alGenEffects();
//
//		alEffecti(effect, AL_EFFECT_TYPE, AL_EFFECT_REVERB);
//		alEffectf(effect, AL_REVERB_DECAY_TIME, 5f);

		filter = new AudioFilterLowpass();
		filter.setFreqGain(1f);
		filter.setHighFreqGain(.02f);

		source = mgr.play(new AudioBuffer("/music/menu_1.ogg"), AudioSettings.generate2DLooped());
		source.setDirectFilter(filter);

//		alEffecti(effect, AL_EFFECT_TYPE, AL_EFFECT_REVERB);
//		alEffectf(effect, AL_REVERB_DENSITY, 0.4f);
//		alEffectf(effect, AL_REVERB_DIFFUSION, .1f);
//		alEffectf(effect, AL_REVERB_GAIN, .4f);
//		alEffectf(effect, AL_REVERB_DECAY_TIME, 5f);
//
//		alAuxiliaryEffectSloti(auxEffectSlot, AL_EFFECTSLOT_EFFECT, effect);
		// alSourcei(source.getHandle(), AL_DIRECT_FILTER, filter);
		// alSource3i(source.getHandle(), AL_AUXILIARY_SEND_FILTER, effect, 0,
		// AL_FILTER_NULL);

	}

	static void render() {

//		alSourcei(source.getHandle(), AL_DIRECT_FILTER, filter);
//		source.setPitch(.6f);
//
//		alSource3i(source.getHandle(), AL_AUXILIARY_SEND_FILTER, effect, 0, filter);

		// alSourcei(source.getHandle(), AL_DIRECT_FILTER, AL_FILTER_NULL);
		// source.setPitch(1f);

	}

	static void destroy() {

		filter.destroy();

//		alDeleteFilters(filter);
//		alDeleteEffects(effect);
//		alDeleteAuxiliaryEffectSlots(auxEffectSlot);

	}

}
