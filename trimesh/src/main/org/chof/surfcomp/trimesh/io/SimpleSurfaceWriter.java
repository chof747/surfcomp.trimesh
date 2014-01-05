package org.chof.surfcomp.trimesh.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import org.chof.surfcomp.trimesh.exception.TrimeshException;
import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.domain.Triangle;

public abstract class SimpleSurfaceWriter extends DefaultSurfaceWriter {

	protected BufferedWriter output = null;
	protected HashMap<Point, Integer> pointIndex = null;
	protected HashMap<Triangle, Integer> triangleIndex = null;

	@Override
	public void setWriter(Writer writer) throws TrimeshException {
		if (writer instanceof BufferedWriter) {
            output = (BufferedWriter) writer;
        } else {
            output = new BufferedWriter(writer);
        }
		
	}

	@Override
	public void setWriter(OutputStream writer) throws TrimeshException {
        setWriter(new OutputStreamWriter(writer));		
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	protected void initializeIndices() {
		if (pointIndex != null) {
			pointIndex.clear();
		} else {
			pointIndex = new HashMap<Point, Integer>();
		}
		
		if (triangleIndex != null) {
			triangleIndex.clear();
		} else {
			triangleIndex = new HashMap<Triangle, Integer>();
		}
		
	} 

}
