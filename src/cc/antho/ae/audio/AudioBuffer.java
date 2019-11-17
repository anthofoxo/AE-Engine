package cc.antho.ae.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;

import cc.antho.ae.common.Util;
import cc.antho.ae.log.Logger;
import lombok.Getter;

public final class AudioBuffer {

	@Getter private final int handle;

	AudioBuffer(final String file) {

		stackPush();
		final IntBuffer channelsBuffer = stackMallocInt(1);
		stackPush();
		final IntBuffer sampleRateBuffer = stackMallocInt(1);

		ByteBuffer buffer = null;

		try {

			final InputStream stream = Util.getStream(file);
			final byte[] data = Util.loadByteArray(stream);

			buffer = BufferUtils.createByteBuffer(data.length);
			buffer.put(data);
			buffer.flip();

		} catch (final IOException e) {

			e.printStackTrace();

		}

		final ShortBuffer rawAudioBuffer = stb_vorbis_decode_memory(buffer, channelsBuffer, sampleRateBuffer);

		final int channels = channelsBuffer.get();
		final int sampleRate = sampleRateBuffer.get();

		stackPop();
		stackPop();

		int format;

		if (channels == 1) format = AL_FORMAT_MONO16;
		else if (channels == 2) format = AL_FORMAT_STEREO16;
		else format = -1;

		handle = alGenBuffers();

		alBufferData(handle, format, rawAudioBuffer, sampleRate);

		free(rawAudioBuffer);

	}

	public void destroy() {

		alDeleteBuffers(handle);

	}

}
