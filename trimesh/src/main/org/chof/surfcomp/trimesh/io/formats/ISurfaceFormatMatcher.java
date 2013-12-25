package org.chof.surfcomp.trimesh.io.formats;

/**
 * An interface used to check if data has the right format
 * <p>
 * The format matcher interface is mainly copied from the IO format module of
 * CDK - written by Egon Willighagen <egonw@users.sf.net>
 * @author chof
 * @author Egon Willighagen <egonw@users.sf.net>
 *
 */
public interface ISurfaceFormatMatcher extends ISurfaceFormat {
    /**
     * Method that checks whether the given line is part of the format
     * read by this reader.
     *
     * @param lineNumber  number of the line
     * @param line        line in the file being checked
     *
     * @return true if the line is of a file format read by this reader
     */
    public boolean matches(int lineNumber, String line);
}
