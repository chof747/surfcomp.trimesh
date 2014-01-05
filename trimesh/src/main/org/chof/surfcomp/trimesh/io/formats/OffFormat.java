package org.chof.surfcomp.trimesh.io.formats;

import org.chof.surfcomp.trimesh.tools.DataFeatures;

/**
 * MSMS Surface format
 * <p>
 * Provides a format instance for the surface format for Michael Sanners
 * MSMS software. 
 * <p>
 * For better treatment the format expects the vert and face file concatenated together
 * 
 * @author chof
 *
 */
public class OffFormat implements ISurfaceFormatMatcher {
	
	private static OffFormat instance = null;
	
	/**
	 * MSMSFormat is implemented as a singleton
	 * @return the singleton instance of the format
	 */
	public static OffFormat getInstance() {
		if (instance == null) {
			instance = new OffFormat();
		}
		
		return instance;
	}
	
	private OffFormat() {
		super();
	}

	@Override
	public String getReaderClassName() {
		return "org.chof.surfcomp.trimesh.io.OffReader";
	}

	@Override
	public String getWriterClassName() {
		return "org.chof.surgcomp.trimesh.io.OffWriter";
	}

	@Override
	public int getSupportedDataFeatures() {
		return getRequiredDataFeatures();
	}

	@Override
	public int getRequiredDataFeatures() {
		return DataFeatures.HAS_POINT_COORDINATES |
			   DataFeatures.HAS_POINT_NORMALS |
			   DataFeatures.HAS_TRIANGULATION;
	}

	@Override
	public String getFormatName() {
		return "Geomview Object file format";
	}

	@Override
	public String getPreferredNameExtension() {
		return getNameExtensions()[0];
	}

	@Override
	public String[] getNameExtensions() {
		String[] extensions = { "off" };
		return extensions;
	}

	@Override
	public String getMIMEType() {
		return null;
	}

	@Override
	public boolean isXMLBased() {
		return false;
	}

	@Override
	public boolean matches(int lineNumber, String line) {
		if ((line.contains("OFF")) && (!line.startsWith("#"))) {
		    return line.endsWith("OFF");
		} else {
			return false;
		}
	}

}
