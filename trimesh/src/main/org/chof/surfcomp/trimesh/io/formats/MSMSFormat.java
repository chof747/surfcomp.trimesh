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
public class MSMSFormat implements ISurfaceFormatMatcher {
	
	private static MSMSFormat instance = null;
	
	/**
	 * MSMSFormat is implemented as a singleton
	 * @return the singleton instance of the format
	 */
	public static MSMSFormat getInstance() {
		if (instance == null) {
			instance = new MSMSFormat();
		}
		
		return instance;
	}
	
	private MSMSFormat() {
		super();
	}

	@Override
	public String getReaderClassName() {
		return "org.chof.surfcomp.trimesh.io.MSMSReader";
	}

	@Override
	public String getWriterClassName() {
		return "org.chof.surgcomp.trimesh.io.MSMSWriter";
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
		return "MSMS Surface Format";
	}

	@Override
	public String getPreferredNameExtension() {
		return getNameExtensions()[0];
	}

	@Override
	public String[] getNameExtensions() {
		String[] extensions = { "msms" };
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
		if (lineNumber == 2) {
		   if ((line.indexOf("#vertex") != -1) &&
			   (line.indexOf("#sphere") != -1) &&
			   (line.indexOf("density") != -1) &&
			   (line.indexOf("probe_r") != -1)) {
			   return true;
		   } else {
			   return false;
		   }
		} else {
			return false;
		}
	}

}
