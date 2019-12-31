package cc.antho.ae.common;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

import javax.imageio.ImageIO;

public final class Util {

	public static int BLOCK_READ_SIZE = 1024;

	private Util() {

	}

	public static Class<?> loadClass(String className, URL url) {

		ClassLoader loader = classLoader();

		try {

			return loader.loadClass(className);

		} catch (ClassNotFoundException e) {

			try (URLClassLoader cl = new URLClassLoader(new URL[] { url }, classLoader())) {

				return cl.loadClass(className);

			} catch (IOException | ClassNotFoundException e1) {

				return null;

			}

		}

	}

	public static ClassLoader classLoader() {

		return Util.class.getClassLoader();

	}

	public static final byte[] loadByteArray(final InputStream stream) throws IOException {

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		final byte[] tmp = new byte[BLOCK_READ_SIZE];
		int len;

		while ((len = stream.read(tmp)) != -1)
			os.write(tmp, 0, len);

		return os.toByteArray();

	}

	public static final String loadString(final InputStream stream) throws IOException {

		return new String(loadByteArray(stream));

	}

	public static final InputStream getStream(final String input) throws FileNotFoundException {

		if (input.startsWith("/")) return Util.class.getResourceAsStream(input);
		return new FileInputStream(input);

	}

	public static BufferedImage loadImage(InputStream stream, boolean flip) throws IOException {

		BufferedImage image = ImageIO.read(stream);
		image.flush();

		if (flip) flip(image);

		return image;

	}

	@Deprecated
	public static BufferedImage loadResourceToImage(String file) throws IOException {

		InputStream is = getStream(file);

		BufferedImage image = ImageIO.read(is);
		image.flush();

		is.close();

		return image;

	}

	// https://stackoverflow.com/a/23457861/10830440
	public static void flip(BufferedImage image) {

		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight() / 2; j++) {

				int tmp = image.getRGB(i, j);
				image.setRGB(i, j, image.getRGB(i, image.getHeight() - j - 1));
				image.setRGB(i, image.getHeight() - j - 1, tmp);

			}

	}

	public static <T> boolean arrayContains(T[] array, T value) {

		for (T t : array)
			if (t.equals(value)) return true;

		return false;

	}

	@Deprecated
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
