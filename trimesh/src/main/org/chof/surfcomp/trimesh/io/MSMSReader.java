package org.chof.surfcomp.trimesh.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
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
	private String line;

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
	public <M extends Mesh> M read(M object) throws TrimeshException {
		mesh = null;
		try {
			mesh = object.getClass().newInstance();
			try {
				Scanner scanner;
				
				skipComments();

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
				
				skipComments();
				
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

		} catch (InstantiationException e) {
			new TrimeshException("Cannot create Mesh instance for reading data", e);
		} catch (IllegalAccessException e) {
			new TrimeshException("Cannot create Mesh instance for reading data", e);
		}
		
		return (M) mesh;
	}

	private void skipComments() throws IOException {
		do {
			line = input.readLine();
		} while (line.charAt(0) == '#');
	}

	private void readFaces(int nfaces) throws IOException, TrianglePointMissing {
		for(int i= 0; i<nfaces;i++) {
			int a,b,c;
			
			line = input.readLine();
			a = new Integer(line.substring( 0, 6).trim()).intValue() - 1;
			b = new Integer(line.substring( 7,13).trim()).intValue() - 1;
			c = new Integer(line.substring(14,20).trim()).intValue() - 1;
			
			mesh.addTriangle(a, b, c);
		}		
	}

	private void readVertices(int nvertices) throws IOException,
			FailedPointAddition {
		for(int i= 0; i<nvertices;i++) {
			double x,y,z,
			       nx,ny,nz;
			//int facenum,
			//    ixSphere,
			//    facetype;
			
			line = input.readLine();
			x  = new Double(line.substring( 0, 9).trim()).doubleValue();
			y  = new Double(line.substring(10,19).trim()).doubleValue();
			z  = new Double(line.substring(20,29).trim()).doubleValue();
			nx = new Double(line.substring(30,39).trim()).doubleValue();
			ny = new Double(line.substring(40,49).trim()).doubleValue();
			nz = new Double(line.substring(50,59).trim()).doubleValue();
			
			Point point = new Point(new Point3d(x,y,z),
					                new Vector3d(nx, ny, nz));
			mesh.addPoint(point);
		}
	}

	@Override
	public boolean accepts(Class<? extends Mesh> classObject) {
		return (Mesh.class.equals(classObject)); 
	}

	@Override
	public IResourceFormat getFormat() {
		return MSMSFormat.getInstance();
	}

}
