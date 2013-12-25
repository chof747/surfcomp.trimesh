package org.chof.surfcomp.trimesh.io.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class IOTestCase {

	public static InputStream loadTestFile(String pathToFile) {
		InputStream ins = IOTestCase.class.getClassLoader()
				.getResourceAsStream(pathToFile);
		return ins;
	}

	public static InputStream loadGzipTestFile(String pathToGzipFile)
			throws IOException {
		InputStream ins = new BufferedInputStream(new GZIPInputStream(
				IOTestCase.class.getClassLoader().getResourceAsStream(
						pathToGzipFile)));
		return ins;
	}
}
