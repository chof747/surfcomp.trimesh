package org.chof.surfcomp.trimesh.io.formats;

/**
 * @author chof
 * <p>
 * This interface implements analog to the IResourceFormat of cdk a basic 
 * format for a mesh data format.</p>
 * <p>
 * Taken from CDK - original code copied from Egon Willighagen <egonw@users.sf.net></p>
 */
public interface IResourceFormat {

    /**
     * Returns a one-lined format name of the format.
     */
    public String getFormatName();

    /**
     * Returns the preferred resource name extension.
     */
    public String getPreferredNameExtension();

    /**
     * Returns an array of common resource name extensions.
     */
    public String[] getNameExtensions();

    /**
     * Returns the accepted MIME type for this format.
     *
     * @return null if no MIME type has been accepted on
     */
    public String getMIMEType();
    
    /**
     * Indicates if the format is an XML-based language.
     * 
     * @return if the format is XML-based.
     */
    public boolean isXMLBased();
}
