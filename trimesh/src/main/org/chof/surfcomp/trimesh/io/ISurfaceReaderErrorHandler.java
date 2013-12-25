package org.chof.surfcomp.trimesh.io;

/**
 * An interface representing an error handler for Surface readers
 * <p>
 *   Code mainly copied from CDK IO module</p>
 * @author chof, Egon Willighagen <egonw@users.sf.net>
 *
 */
public interface ISurfaceReaderErrorHandler {
    /**
     * Method that should react on an error message send by an 
     * {@link ISurfaceReader}.
     *
     * @param message Error found while reading.
     */
    public void handleError(String message);

    /**
     * Method that should react on an error message send by an 
     * {@link ISurfaceReader}.
     *
     * @param message   Error found while reading.
     * @param exception Exception thrown while reading.
     */
    public void handleError(String message, Exception exception);

    /**
     * Method that should react on an error message send by an 
     * {@link ISurfaceReader}.
     *
     * @param message  Error found while reading.
     * @param row      Row in the file where the error is found.
     * @param colStart Start column in the file where the error is found.
     * @param colEnd   End column in the file where the error is found.
     */
    public void handleError(String message, int row, int colStart, int colEnd);

    /**
     * Method that should react on an error message send by an 
     * {@link ISurfaceReader}.
     *
     * @param message   Error found while reading.
     * @param exception Exception thrown while reading.
     * @param colStart Start column in the file where the error is found.
     * @param colEnd   End column in the file where the error is found.
     */
    public void handleError(String message, int row,
            int colStart, int colEnd, Exception exception);

}
