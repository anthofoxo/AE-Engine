package cc.antho.ae.audio.effect;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.EXTEfx.*;

import java.util.ArrayList;
import java.util.List;

import cc.antho.ae.audio.AudioSource;
import cc.antho.ae.log.Logger;
import lombok.Getter;

@Getter
public final class AudioEffectSlot {

	protected int handle;

	private float gain;
	private boolean auto;
	private AudioEffect effect;

	private List<AudioSource> attached = new ArrayList<>();
	private List<Integer> indexes = new ArrayList<>();

	public AudioEffectSlot(int handle) {

		Logger.debug("Creating aux effect slot");
		this.handle = handle;

		setGain(1f);
		setAuto(true);

	}

	public AudioEffectSlot() {

		this(alGenAuxiliaryEffectSlots());

	}

	public void load() {

		for (int i = 0; i < attached.size(); i++)
			attached.get(i).setAuxSend(indexes.get(i), this, attached.get(i).getFilters()[indexes.get(i)]);

	}

	public void setGain(float gain) {

		this.gain = gain;
		alAuxiliaryEffectSlotf(handle, AL_EFFECTSLOT_GAIN, gain);
		load();

	}

	public void setAuto(boolean auto) {

		this.auto = auto;
		alAuxiliaryEffectSloti(handle, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, auto ? AL_TRUE : AL_FALSE);
		load();

	}

	public void setEffect(AudioEffect effect) {

		if (effect == null) alAuxiliaryEffectSloti(handle, AL_EFFECTSLOT_EFFECT, AL_EFFECT_NULL);
		else {

			if (this.effect != null) this.effect.getSlots().remove(this);
			effect.getSlots().add(this);

			alAuxiliaryEffectSloti(handle, AL_EFFECTSLOT_EFFECT, effect.getHandle());

		}

		this.effect = effect;
		load();

	}

	public void destroy() {

		Logger.debug("Destroying aux effect slot");

		if (effect != null) effect.getSlots().remove(this);

		for (int i = 0; i < attached.size(); i++)
			attached.get(i).setAuxSend(indexes.get(i), null, attached.get(i).getFilters()[indexes.get(i)]);

		attached.clear();
		indexes.clear();

		alDeleteAuxiliaryEffectSlots(handle);
		handle = 0;
		effect = null;

	}

}
