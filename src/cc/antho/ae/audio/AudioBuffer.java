package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import cc.antho.ae.common.Destroyable;
import cc.antho.ae.common.Util;
import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class AudioBuffer implements Destroyable {

	@Getter private int handle;

	public AudioBuffer(String file) {

		Logger.debug("Creating audio buffer");

		ShortBuffer audioBuffer;
		int format = -1;
		int channels;
		int sampleRate;

		try (MemoryStack stack = stackPush()) {

			IntBuffer channelsBuffer = stack.mallocInt(1);
			IntBuffer sampleRateBuffer = stack.mallocInt(1);

			ByteBuffer buffer = null;

			try {

				Logger.info("Loading audio file: " + file);

				byte[] data = Util.loadByteArray(Util.getStream(file));

				buffer = BufferUtils.createByteBuffer(data.length);
				buffer.put(data);
				buffer.flip();

			} catch (IOException e) {

				e.printStackTrace();

			}

			Logger.info("Decoding audio file: " + file);
			audioBuffer = stb_vorbis_decode_memory(buffer, channelsBuffer, sampleRateBuffer);

			channels = channelsBuffer.get();
			sampleRate = sampleRateBuffer.get();

		}

		if (channels == 1) format = AL_FORMAT_MONO16;
		else if (channels == 2) format = AL_FORMAT_STEREO16;

		handle = alGenBuffers();
		alBufferData(handle, format, audioBuffer, sampleRate);
		free(audioBuffer);

	}

	public void destroy() {

		Logger.debug("Destroying audio buffer");

		alDeleteBuffers(handle);
		handle = 0;

	}

}
