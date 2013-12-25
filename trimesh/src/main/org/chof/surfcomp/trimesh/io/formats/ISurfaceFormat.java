package org.chof.surfcomp.trimesh.io.formats;

/**
 * @author chof
 * <p>
 * This interface implements analog to the IChemFormat of cdk a basic 
 * format for a mesh data format.</p>
 * <p>
 * Taken from CDK - original code copied from Egon Willighagen <egonw@users.sf.net></p>
 */
public interface ISurfaceFormat extends IResourceFormat {
	
    /**
     * Returns the class name of the CDK Reader for this format.
     *
     * @return null if no Surface Reader is available.
     */
    public String getReaderClassName();

    /**
     * Returns the class name of the CDK Writer for this format.
     *
     * @return null if no Surface Writer is available.
     */
    public String getWriterClassName();    

    /**
     * Returns an integer indicating the data features that this 
     * format supports. The integer is composed as explained in 
     * DataFeatures. May be set to DataFeatures.NONE as default.
     * 
     * @see org.chof.surfcomp.trimesh.tools.DataFeatures
     */
	public int getSupportedDataFeatures();

    /**
     * Returns an integer indicating the data features that this 
     * format requires. For example, the XYZ format requires 3D
     * coordinates.
     * 
     * @see org.chof.surfcomp.trimesh.tools.DataFeatures
     */
    public int getRequiredDataFeatures();
}