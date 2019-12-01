package cc.antho.ae.audio;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryStack.*;

import java.nio.IntBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.system.MemoryStack;

import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class AudioContext {

	@Getter private AudioDevice device;
	@Getter private long handle;
	@Getter private ALCapabilities capabilities;

	public AudioContext(AudioDevice device) {

		this.device = device;

		Logger.info("Creating audio context for: " + device.getName());

		try (MemoryStack stack = stackPush()) {

			// Automatically set to 0
			IntBuffer attributes = stack.callocInt(1);

			handle = alcCreateContext(device.getHandle(), attributes);
			alcMakeContextCurrent(handle);

			capabilities = AL.createCapabilities(device.getCapabilities());

			if (capabilities.OpenAL10) Logger.info("AL10 is supported");
			else Logger.error("AL10 is supported");

			if (capabilities.OpenAL11) Logger.info("AL11 is supported");
			else Logger.warn("AL11 is not supported");

		}

	}

	public void destroy() {

		Logger.info("Destroying audio context for: " + device.getName());

		if (alcMakeContextCurrent(0l)) {

			alcDestroyContext(handle);
			handle = 0;
			capabilities = null;
			device = null;

		} else Logger.error("Failed to destroy audio context for: " + device.getName());

	}

}
