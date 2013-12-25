package org.chof.surfcomp.trimesh.io;

import org.chof.surfcomp.trimesh.exception.TrimeshException;

/**
 * Abstract baseclass for surface readers
 * <p>
 * This class takes care of all the major issues a surface reader implementation must
 * adress except for the read method itself.</p>
 * <p>
 * Use this class as a base class for concrete surface readers
 */
public abstract class DefaultSurfaceReader implements ISurfaceReader {
	
	/**
	 * The mode of the reader
	 */
	protected Mode mode = Mode.STRICT;
	
	/**
	 * The error handler assigned to this reader
	 */
	protected ISurfaceReaderErrorHandler errorHandler = null;

	@Override
	public void setReaderMode(Mode mode) {
		this.mode = mode;
	}

	@Override
	public void setErrorHandler(ISurfaceReaderErrorHandler handler) {
		this.errorHandler = handler;
	}

	@Override
	public void handleError(String message) throws TrimeshException {
        if (this.errorHandler != null) {
			this.errorHandler.handleError(message);
		}
        if (this.mode == Mode.STRICT) {
        	throw new TrimeshException(message);		
        }
	}

	@Override
	public void handleError(String message, Exception exception)
			throws TrimeshException {
        if (this.errorHandler != null) {
			this.errorHandler.handleError(message, exception);
		}
        if (this.mode == Mode.STRICT) {
        	throw new TrimeshException(message, exception);		
        }
	}

	@Override
	public void handleError(String message, int row, int colStart, int colEnd)
			throws TrimeshException {
		if (errorHandler != null) {
			errorHandler.handleError(message, row, colStart, colEnd);
		}
        if (this.mode == Mode.STRICT) {
        	throw new TrimeshException(message);		
        }
	}

	@Override
	public void handleError(String message, int row, int colStart, int colEnd,
			Exception exception) throws TrimeshException {
		if (errorHandler != null) {
			errorHandler.handleError(message, row, colStart, colEnd, exception);
		}
        if (this.mode == Mode.STRICT) {
        	throw new TrimeshException(message, exception);		
        }
	}

}
