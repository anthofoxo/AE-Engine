package cc.antho.ae.common;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public final class Util {

	private Util() {

	}

	public static final byte[] loadByteArray(final InputStream stream) throws IOException {

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		final byte[] tmp = new byte[1024];
		int len;

		while ((len = stream.read(tmp)) != -1)
			os.write(tmp, 0, len);

		return os.toByteArray();

	}

	public static final InputStream getStream(final String input) throws FileNotFoundException {

		if (input.startsWith("/")) return Util.class.getResourceAsStream(input);
		return new FileInputStream(input);

	}

	public static BufferedImage loadResourceToImage(String file) throws IOException {

		InputStream is = getStream(file);

		BufferedImage image = ImageIO.read(is);
		image.flush();

		is.close();

		return image;

	}

	public static <T> boolean arrayContains(T[] array, T value) {

		for (T t : array)
			if (t.equals(value)) return true;

		return false;

	}

	public static String loadResourceToString(String file) throws IOException {

		InputStream is = getStream(file);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = br.readLine()) != null)
			sb.append(line + "\n");

		br.close();
		isr.close();
		is.close();

		return sb.toString();

	}

	public static String exceptionToString(Throwable e) {

		StringBuilder s = new StringBuilder();
		boolean first = true;

		String name, message;
		StackTraceElement[] trace;

		do {

			if (first) first = false;
			else s.append("Caused by: ");

			name = e.getClass().getName();
			message = e.getLocalizedMessage();
			s.append(((message != null) ? (name + ": " + message) : name) + "\n");

			trace = e.getStackTrace();

			for (StackTraceElement traceElement : trace)
				s.append("\tat " + traceElement + "\n");

		} while ((e = e.getCause()) != null);

		return s.toString();

	}

	public static void yield(long millis) {

		try {

			Thread.sleep(millis);

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();

		}

	}

}
