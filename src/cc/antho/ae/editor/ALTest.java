package cc.antho.ae.editor;

import cc.antho.ae.audio.AudioBuffer;
import cc.antho.ae.audio.AudioManager;
import cc.antho.ae.audio.AudioSettings;
import cc.antho.ae.audio.AudioSource;
import cc.antho.ae.audio.effect.AudioEffectReverb;
import cc.antho.ae.audio.effect.AudioEffectSlot;
import cc.antho.ae.audio.effect.AudioFilterLowpass;

public class ALTest {

	private static AudioFilterLowpass filter;
	private static AudioSource source;

	private static AudioEffectSlot slot;
	private static AudioEffectReverb effect;

	static void init(AudioManager mgr) {

		effect = new AudioEffectReverb();

		slot = new AudioEffectSlot();
		slot.setEffect(effect);

		filter = new AudioFilterLowpass();
		filter.setFreqGain(1f);
		filter.setHighFreqGain(.01f);

		source = mgr.play(new AudioBuffer("/music/meat_boy.ogg"), AudioSettings.generate2DLooped());
		source.setDirectFilter(filter);
		source.setAuxSend(0, slot, filter);

	}

	static void render() {

	}

	static void destroy() {

		filter.destroy();
		slot.destroy();
		effect.destroy();

	}

}
