package org.chof.surfcomp.trimesh.io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.chof.surfcomp.trimesh.io.FormatFactory;
import org.chof.surfcomp.trimesh.io.formats.MSMSFormat;
import org.junit.Test;

public class FormatFactoryTest extends IOTestCase {

	@Test
	public void testFormatSetup() {
		FormatFactory formats = new FormatFactory();
		assertNotNull(formats);
	}

	@Test
	public void testFormatRegister() {
		FormatFactory formats = setupFormat();
		assertEquals(1, formats.getFormats().size());
	}

	@Test
	public void testFormatMatching() throws IOException {
		FormatFactory formats = setupFormat();

		InputStream testFile = loadTestFile("data/msms/1crn.msms");
		assertEquals("org.chof.surfcomp.trimesh.io.formats.MSMSFormat", formats
				.guessFormat(testFile).getClass().getName());

		testFile = loadTestFile("data/pdb/1crn.pdb");
		assertNull(formats.guessFormat(testFile));
	}

	@Test
	public void testGZipFormatMatching() throws IOException {
		FormatFactory formats = setupFormat();
		InputStream testFile = loadGzipTestFile("data/msms/1crn.msms.gz");
		assertEquals("org.chof.surfcomp.trimesh.io.formats.MSMSFormat", formats
				.guessFormat(testFile).getClass().getName());
	}

	@Test
	public void testReaderMatching() throws IOException {
		FormatFactory formats = setupFormat();

		InputStream testFile = loadTestFile("data/msms/1crn.msms");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				testFile));
		assertEquals("org.chof.surfcomp.trimesh.io.formats.MSMSFormat", formats
				.guessFormat(reader).getClass().getName());

	}

	private FormatFactory setupFormat() {
		FormatFactory formats = new FormatFactory();
		formats.registerFormat(MSMSFormat.getInstance());
		return formats;
	}

}
