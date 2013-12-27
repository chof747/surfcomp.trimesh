package org.chof.surfcomp.trimesh.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import java.util.Scanner;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.domain.Triangle;
import org.chof.surfcomp.trimesh.exception.FailedPointAddition;
import org.chof.surfcomp.trimesh.exception.TrianglePointMissing;
import org.chof.surfcomp.trimesh.exception.TrimeshException;
import org.chof.surfcomp.trimesh.io.formats.IResourceFormat;
import org.chof.surfcomp.trimesh.io.formats.MSMSFormat;

/**
 * Surface file reader for Michael Sanners MSMS mol surface programm
 *  
 * @author chof
 */
public class MSMSReader extends SimpleSurfaceReader {
	
	private Mesh mesh;

	/**
	 * Default constructor
	 * <p>
	 * The default constructor sets up a reader from an empty StringReader.
	 */
    public MSMSReader() {
    	this(new StringReader(""));
    }

    /**
     * Constructor setting up a reader with the provided input Reader
     * @param in the input reader from which the data has to be taken
     */
    public MSMSReader(Reader in) {
    	this(in, Mode.STRICT);
	}

    /**
     * Constructs a reader for the MSMS surface file from the given InputReader and
     * with the given mode.
     * @param in the input reader from which data has to be taken
     * @param mode the mode of the input reader
     */
	public MSMSReader(Reader in, Mode mode) {
		super.mode = mode;
        input = new BufferedReader(in);		
	}
	
	/**
	 * Constructs a reader for MSMS surfaces from a given input stream
	 * @param in the input stream from which data has to be taken
	 */
	public MSMSReader(InputStream in) {
		this(in, Mode.STRICT);
	}

	/**
	 * Constructs a reader from MSMS surfaces from a given input stream and with the specified
	 * mode
	 * @param in the input stream from which the data has to be taken
	 * @param mode the specific mode
	 */
	public MSMSReader(InputStream in, Mode mode) {
		this(new InputStreamReader(in), mode);
	}

	@Override
	public boolean accepts(Class<? extends Mesh> classObject) {
		return (Mesh.class.equals(classObject)); 
	}

	@Override
	public IResourceFormat getFormat() {
		return MSMSFormat.getInstance();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <M extends Mesh> M read(M instance) throws TrimeshException {
		try {
			mesh = instance;
			
			Scanner scanner;
			String line;
			
			line = readWithoutComments();

			int nvertices, nfaces, nspheres;
			double density, probeRadius;
			
			//read vertex numbers ...
			scanner = new Scanner(line);
			scanner.useLocale(Locale.ROOT);

			nvertices = scanner.nextInt();
			nspheres = scanner.nextInt();
			density = scanner.nextDouble();
			probeRadius = scanner.nextDouble();
			
			readVertices(nvertices);
			
			line = readWithoutComments();
			
			//read face numbers
			scanner = new Scanner(line);
			scanner.useLocale(Locale.ROOT);

			nfaces = scanner.nextInt();
			if ((scanner.nextInt() != nspheres) ||
			    (density != scanner.nextDouble()) ||
			    (probeRadius != scanner.nextDouble())) {
				throw new IOException("face and vertices part is not related!");
			}
			
			readFaces(nfaces);
			
		} catch (IOException e) {
			throw new TrimeshException("Error reading input for surface", e);
		}

		
		return (M) mesh;
	}

	private String readWithoutComments() throws IOException {
		String line;
		do {
			line = input.readLine();
		} while (line.charAt(0) == '#');
		
		return line;
	}

	/**
	 * Reads the faces from the file
	 * <p>
	 * The faces are stored with the 
	 * <ul>
	 * <li>indices from the three corners of each triangle</li>
	 * <li>1 for a triangle in a toric reentrant surface, 2 for a triangle in a spheric
	 * reentrant surface, 3 for a triangle in a contact face (ignored)</li>
	 * <li>Number of the face in the analytical surface (ignored)</li>
	 * 
	 * @param nfaces
	 * @throws IOException
	 * @throws TrianglePointMissing
	 */
	private void readFaces(int nfaces) throws IOException, TrianglePointMissing {
		for(int i= 0; i<nfaces;i++) {
			int a,b,c;
			int faceType;
			int faceNumber;
			
			String line = readWithoutComments();
			a = new Integer(line.substring( 0, 6).trim()).intValue() - 1;
			b = new Integer(line.substring( 7,13).trim()).intValue() - 1;
			c = new Integer(line.substring(14,20).trim()).intValue() - 1;
			
			faceType = new Integer(line.substring(21, 23).trim()).intValue();
			faceNumber = new Integer(line.substring(24, 30).trim()).intValue();
			
			mesh.addTriangle(a, b, c);
			Triangle t = mesh.getTriangle(i);
			t.setProperty("faceType", faceType);
			t.setProperty("faceNumber", faceNumber);
		}		
	}
	/**
	 * Reads the vertex lines to get the point coordinates and the normale vector
	 * <p>
	 * The MSMS file format for vectors contains the following line for each 
	 * vertex:<p>
	 * <p>
	 * <code>%9.3f %9.3f %9.3f %9.3f %9.3f %9.3f %7d %7d %2d %s'</code><br/>
	 * Where the
	 * <ul>
	 * <li>first three values are the coordinates of the point,</li>
	 * <li>the second three values represent the surface normal for that point</li>
	 * <li>followed by the index of the analytical surface (stored as Property faceNumber)</li>
	 * <li>followed by the 1-based index of the closest sphere (stored as Property sphereIndex)</li>
	 * <li>followed by a fag if the vertex inside a toric reentrant faces (1), 
	 * lies inside reentrant faces (2) and inside contact faces (3) - (stored as Property faceType)</li>
	 * <li>followed by an optional name of the atom (stored as Property atomName)</li>
	 * </ul> 
	 * @param nvertices
	 * @throws IOException
	 * @throws FailedPointAddition
	 */

	private void readVertices(int nvertices) throws IOException,
			FailedPointAddition {
		for(int i= 0; i<nvertices;i++) {
			double x,y,z,
			       nx,ny,nz;
			int facenum,
			    ixSphere,
			    facetype;
			String atomName;
			
			String line = readWithoutComments();
			x  = new Double(line.substring( 0, 9).trim()).doubleValue();
			y  = new Double(line.substring(10,19).trim()).doubleValue();
			z  = new Double(line.substring(20,29).trim()).doubleValue();
			nx = new Double(line.substring(30,39).trim()).doubleValue();
			ny = new Double(line.substring(40,49).trim()).doubleValue();
			nz = new Double(line.substring(50,59).trim()).doubleValue();
			
			facenum  = new Integer(line.substring(60, 67).trim()).intValue();
			ixSphere = new Integer(line.substring(68, 75).trim()).intValue();
			facetype = new Integer(line.substring(76, 79).trim()).intValue();
			
			Point point = new Point(new Point3d(x,y,z),
	                new Vector3d(nx, ny, nz));

			if (line.length()>79) {
				atomName = line.substring(79, line.length()-1).trim();
				point.setProperty("atomName", atomName);
			}
			
			point.setProperty("faceNumber", facenum);
			point.setProperty("sphereIndex", ixSphere);
			point.setProperty("faceType", facetype);
			
			mesh.addPoint(point);
		}
	}

}
