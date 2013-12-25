package org.chof.surfcomp.trimesh.io;

import java.io.Closeable;
import java.io.IOException;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.io.formats.IResourceFormat;

/**
 * Base interface for the ISurfaceReader and ISurfaceWriter interfaces
 * <p>
 * The interface implements basic methods for alignment with surface formats and 
 * file closing and acceptance of data.</p>
 * <p>
 * The design was mainly copied from CDK's IO design written by Egon Willinghagen</p>
 * @author chof
 * 
 */
public interface ISurfaceIO extends Closeable {
	
	/**
	 * Checks wether the reader or writer is able to accept objects of the 
	 * given class.
	 * 
	 * @param classObject
	 * @return true if the reader or writer accepts the object, false otherwise
	 */
	public boolean accepts(Class<? extends Mesh> classObject);
	
	/**
	 * closes the resources needed by the IO reder/writer
	 * @throws IOException 
	 */
	public void close() throws IOException;
	
	/**
	 * @return the instance of the format corresponding to the reader
	 */
	public IResourceFormat getFormat();
}
