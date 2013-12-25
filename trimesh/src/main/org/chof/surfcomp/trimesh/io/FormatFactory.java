package org.chof.surfcomp.trimesh.io;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.chof.surfcomp.trimesh.io.formats.ISurfaceFormat;
import org.chof.surfcomp.trimesh.io.formats.ISurfaceFormatMatcher;

/**
 * A factory for recognizing surface file formats. Formats of GZiped files can
 * be detected too.
 * 
 * Mainly copied from CDK io module.
 * 
 * @author Egon Willighagen <egonw@sci.kun.nl>
 * @author chof
 */
public class FormatFactory {

	private final static String IO_FORMATS_LIST = "io-formats.set";

	private int headerLength;

	private static List<ISurfaceFormatMatcher> formats = null;

	/**
	 * Constructs a ReaderFactory which tries to detect the format in the first
	 * 65536 chars.
	 */
	public FormatFactory() {
		this(65536);
	}

	/**
	 * Constructs a ReaderFactory which tries to detect the format in the first
	 * given number of chars.
	 * 
	 * @param headerLength
	 *            length of the header in number of chars
	 */
	public FormatFactory(int headerLength) {
		this.headerLength = headerLength;
		loadFormats();
	}

	private void loadFormats() {
		if (formats == null) {
			formats = new ArrayList<ISurfaceFormatMatcher>();
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(this.getClass().getClassLoader()
								.getResourceAsStream(IO_FORMATS_LIST)));

				while (reader.ready()) {
					// load them one by one
					String formatName = reader.readLine();

					try {
						Class<? extends Object> formatClass = this.getClass()
								.getClassLoader().loadClass(formatName);
						Method getinstanceMethod = formatClass.getMethod(
								"getInstance", new Class[0]);
						ISurfaceFormatMatcher format = (ISurfaceFormatMatcher) getinstanceMethod
								.invoke(null, new Object[0]);
						formats.add(format);
					} catch (ClassNotFoundException exception) {
					} catch (Exception exception) {
					}
				}
			} catch (Exception exception) {
			}
		}
	}

	/**
	 * Registers a format for detection.
	 */
	public void registerFormat(ISurfaceFormatMatcher format) {
		formats.add(format);
	}

	/**
	 * Returns the list of recognizable formats.
	 * 
	 * @return {@link List} of {@link ISurfaceFormat}s.
	 */
	public List<ISurfaceFormatMatcher> getFormats() {
		return formats;
	}

	/**
	 * Creates a String of the Class name of the Surface reader
	 * for this file format. The input is read line-by-line until a line
	 * containing an identifying string is found.
	 * 
	 * <p>
	 * This method is not able to detect the format of gziped files. Use
	 * <code>guessFormat(InputStream)</code> instead for such files.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws IllegalArgumentException
	 *             if the input is null
	 * @return The guessed <code>ISurfaceFormat</code> or <code>null</code> if the
	 *         file format is not recognized.
	 * 
	 * @see #guessFormat(InputStream)
	 */
	public ISurfaceFormat guessFormat(Reader input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("input cannot be null");
		}

		// make a copy of the header
		char[] header = new char[this.headerLength];
		if (!input.markSupported()) {
			throw new IllegalArgumentException("input must support mark");
		}
		input.mark(this.headerLength);
		input.read(header, 0, this.headerLength);
		input.reset();

		BufferedReader buffer = new BufferedReader(new CharArrayReader(header));

		/* Search file for a line containing an identifying keyword */
		String line = null;
		int lineNumber = 1;
		while ((line = buffer.readLine()) != null) {
			for (int i = 0; i < formats.size(); i++) {
				ISurfaceFormatMatcher cfMatcher = formats.get(i);
				if (cfMatcher.matches(lineNumber, line)) {
					return cfMatcher;
				}
			}
			lineNumber++;
		}

		return null;
	}

	public ISurfaceFormat guessFormat(InputStream input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("input cannot be null");
		}

		// make a copy of the header
		byte[] header = new byte[this.headerLength];
		if (!input.markSupported()) {
			throw new IllegalArgumentException("input must support mark");
		}
		input.mark(this.headerLength);
		input.read(header, 0, this.headerLength);
		input.reset();

		BufferedReader buffer = new BufferedReader(new StringReader(new String(
				header)));

		/* Search file for a line containing an identifying keyword */
		String line = null;
		int lineNumber = 1;
		while ((line = buffer.readLine()) != null) {
			for (int i = 0; i < formats.size(); i++) {
				ISurfaceFormatMatcher cfMatcher = formats.get(i);
				if (cfMatcher.matches(lineNumber, line)) {
					return cfMatcher;
				}
			}
			lineNumber++;
		}

		return null;
	}

}
