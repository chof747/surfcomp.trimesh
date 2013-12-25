package org.chof.surfcomp.trimesh.domain.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.exception.FailedPointAddition;
import org.chof.surfcomp.trimesh.exception.TrianglePointMissing;
import org.junit.Test;

public class MeshTest {

	@Test
	public void testSetup() {
		Mesh mesh = new Mesh();
		assertEquals(0,mesh.sizePoints());
		assertEquals(0,mesh.sizeTriangles());
	}
	
	@Test
	public void testBuildMesh() throws FailedPointAddition, TrianglePointMissing {

		Point[] points = {
				makePoint(0.0,0.0,0.0),
				makePoint(1,0,0),
				makePoint(0,1,0),
				makePoint(0,0,1)
		};
		
		Mesh mesh = new Mesh();
		
		int c = 0;
		int ix = -2;
		for(Point p : points) {
			ix = mesh.addPoint(p);
			assertEquals(c++, ix);
		}
		
		c = 0;
		ix = mesh.addTriangle(points[0], points[1], points[2]);
		assertEquals(c++, ix);
		
		ix = mesh.addTriangle(0, 2, 3);
		assertEquals(c++, ix);
		
	}

	private Point makePoint(double x, double y, double z) {
		Point p = new Point();
		p.setCoordinates(new Point3d(x, y, z));
		return p;
	}

}
