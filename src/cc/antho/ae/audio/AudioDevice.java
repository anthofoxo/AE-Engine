package cc.antho.ae.audio;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTEfx.*;

import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import cc.antho.ae.common.Destroyable;
import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class AudioDevice implements Destroyable {

	@Getter private String name;
	@Getter private long handle;
	@Getter private ALCCapabilities capabilities;
	@Getter private int maxAuxSends;

	public AudioDevice(String name) {

		this.name = name;

		Logger.info("Creating audio device: " + name);
		handle = alcOpenDevice(name);

		if (handle == 0l) Logger.error("Failed to create audio device");
		else {

			capabilities = ALC.createCapabilities(handle);

			if (capabilities.OpenALC10) Logger.info("ALC10 is supported");
			else Logger.error("ALC10 is not supported");

			if (capabilities.OpenALC11) Logger.info("ALC11 is supported");
			else Logger.warn("ALC11 is not supported");

			if (capabilities.ALC_EXT_EFX) {

				maxAuxSends = alcGetInteger(handle, ALC_MAX_AUXILIARY_SENDS);

				Logger.info("ALC_EXT_EFX is supported");
				Logger.info("ALC_MAX_AUXILIARY_SENDS: " + maxAuxSends);

			} else Logger.warn("ALC_EXT_EFX is not supported");

		}

	}

	public void destroy() {

		Logger.info("Destroying audio device: " + name);
		if (alcCloseDevice(handle)) {

			handle = 0;
			name = null;
			capabilities = null;

		} else Logger.error("Failed to destroy audio device: " + name);

	}

}
