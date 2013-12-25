package org.chof.surfcomp.trimesh.exception;

/**
 * The basic exception for the trimesh module
 * @author chof
 */
public class TrimeshException extends Exception {

	private static final long serialVersionUID = -2846027189175711022L;
	
	public TrimeshException(String msg) {
		super(msg);
	}

	public TrimeshException(String message, Throwable exception) {
		super(message, exception);
	}

}
