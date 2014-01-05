package org.chof.surfcomp.trimesh.io;

import java.io.IOException;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.domain.Triangle;
import org.chof.surfcomp.trimesh.domain.Triangle.Corner;
import org.chof.surfcomp.trimesh.exception.TrimeshException;
import org.chof.surfcomp.trimesh.io.formats.IResourceFormat;
import org.chof.surfcomp.trimesh.io.formats.OffFormat;

public class OffWriter extends SimpleSurfaceWriter {

	@Override
	public void write(Mesh meshObject) throws TrimeshException {
		initializeIndices();
		
		try {
			output.write("NOFF\n");
			output.write(String.format("%7d %7d %7d\n",
					meshObject.sizePoints(), meshObject.sizeTriangles(),
					meshObject.sizeEdges()));
			
			int i=0;
			for(Point p : meshObject.getPoints()) {
				Point3d position = p.getCoordinates();
				Vector3d normal  = p.getNormale();
				
				output.write(String.format("%9.3f %9.3f %9.3f %9.3f %9.3f %9.3f\n",
					position.x, position.y, position.z,
					normal.x, normal.y, normal.z));
				pointIndex.put(p, i++);
			}
			
			for(Triangle t : meshObject.getTriangles()) {
				int a = pointIndex.get(t.getCorner(Corner.A));
				int b  = pointIndex.get(t.getCorner(Corner.B));
				int c = pointIndex.get(t.getCorner(Corner.C));
				
				output.write(String.format("3 %6d %6d %6d\n", a, b, c));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean accepts(Class<? extends Mesh> classObject) {
		return Mesh.class.isInstance(classObject);
	}

	@Override
	public IResourceFormat getFormat() {
		return OffFormat.getInstance();
	}

}
