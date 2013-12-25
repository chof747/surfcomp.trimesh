package org.chof.surfcomp.trimesh.io;

import java.io.InputStream;
import java.io.Reader;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.exception.TrimeshException;

/**
 * Surface reader interface
 * <p>
 * The surface reader interface enables error handling and basic handling of
 * input stream and reader modes</p>
 * <p>
 * The readers can implement two different modes: strict and relaxed. A strict
 * reader raises an exception, whenever a problem occurs. A relaxed reader tries
 * to recover the problem before it raises an exception.</p>
 * <p>
 * The code was mainly copied from the CDK IO written by Egon Willighagen <egonw@users.sf.net></p>
 * @author chof
 *
 */
public interface ISurfaceReader extends ISurfaceIO {

	public enum Mode {
		/** Only fail on serious format problems */
		RELAXED,
		/** Fail on any format problem */
		STRICT
	}

    /**
     * Sets the Reader from which this ChemObjectReader should read
     * the contents.
     */
    public void setReader(Reader reader) throws TrimeshException;

    /**
     * Sets the InputStream from which this ChemObjectReader should read
     * the contents.
     */
    public void setReader(InputStream reader) throws TrimeshException;
    
    /**
     * Sets the reader mode. If Mode.STRICT, then the reader will fail on
     * any problem in the format of the read file, instead of trying to
     * recover from that.
     * 
     * @param mode
     */
    public void setReaderMode(Mode mode);
    
    /**
     * Reads a Mesh object or one of its descendants of from input. The constructor
     * of the actual implementation may take a Reader as input to get
     * a very flexible reader that can read from string, files, etc.
     * 
     * @param  object    the type of object to return
     * @return returns an object of that contains the content (or 
     *         part) of the input content
     *
     * @exception TrimeshException it is thrown if
     *            the type of information is not available from 
     *            the input
     **/
    public <T extends Mesh> T read(T object) throws TrimeshException;

    /**
     * Sets an error handler that is sent events when file format issues occur.
     *
     * @param handler {@link ISurfaceReaderErrorHandler} to send error
     *                messages to.
     */
    public void setErrorHandler(ISurfaceReaderErrorHandler handler);

	/**
	 * Redirects an error message to the {@link ISurfaceReaderErrorHandler}.
	 * Throws an {@link TrimeshException} when in STRICT {@link Mode}.
	 *
	 * @param message the error message.
	 */
	public void handleError(String message) throws TrimeshException;

    /**
     * Redirects an error message to the {@link ISurfaceReaderErrorHandler}.
     * Throws an {@link TrimeshException} when in STRICT {@link Mode}.
     *
     * @param message  the error message.
     * @param exception the corresponding {@link Exception}.
     */
    public void handleError(String message, Exception exception)
    throws TrimeshException;

    /**
     * Redirects an error message to the {@link ISurfaceReaderErrorHandler}.
     * Throws an {@link TrimeshException} when in STRICT {@link Mode}.
     *
     * @param message  the error message.
     * @param row      Row in the file where the error is found.
     * @param colStart Start column in the file where the error is found.
     * @param colEnd   End column in the file where the error is found.
     */
    public void handleError(String message, int row, int colStart, int colEnd) throws TrimeshException;

    /**
     * Redirects an error message to the {@link ISurfaceReaderErrorHandler}.
     * Throws an {@link TrimeshException} when in STRICT {@link Mode}.
     *
     * @param message  the error message.
     * @param exception the corresponding {@link Exception}.
     * @param row       Row in the file where the error is found.
     * @param colStart Start column in the file where the error is found.
     * @param colEnd   End column in the file where the error is found.
     */
    public void handleError(String message, int row, int colStart, int colEnd, Exception exception)
    throws TrimeshException;
}
