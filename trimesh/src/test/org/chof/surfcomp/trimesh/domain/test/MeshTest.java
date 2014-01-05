package org.chof.surfcomp.trimesh.domain.test;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.Vector;

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
	
	@Test
	public void testPropertyVector() throws FailedPointAddition, TrianglePointMissing {
		Point[] points = {
				makePoint(0.0,0.0,0.0),
				makePoint(1,0,0),
				makePoint(0,1,0),
				makePoint(0,0,1)
		};
		
		Mesh mesh = new Mesh();
		
		Double[] values = { 3.1415, 2.1789, -1.9981, 0.4294 };
		Double[] actual = new Double[4];
		
		int ix = -2;
		for(Point p : points) {
			ix = mesh.addPoint(p);
			p.setProperty("value", values[ix]);
		}

		Vector<Double> propertyVector = mesh.getPointPropertyVector("value", 0.0);
		propertyVector.copyInto(actual);
		assertArrayEquals(values, actual);

		mesh.getPoint(2).removeProperty("value");
		values[2] = 10.0;
		
		propertyVector = mesh.getPointPropertyVector("value", 10.0);
		propertyVector.copyInto(actual);
		assertArrayEquals(values, actual);
		

		String[] expected = { "Mary", "Josef" };
		String[] actuals  = new String[2];
		
		ix = mesh.addTriangle(points[0], points[1], points[2]);		
		ix = mesh.addTriangle(0, 2, 3);
		mesh.getTriangle(0).setProperty("name", expected[0]);
		mesh.getTriangle(1).setProperty("name", expected[1]);
		
		Vector<String> nameVector = mesh.getTrianglePropertyVector("name", "");
		nameVector.copyInto(actuals);
		assertArrayEquals(expected, actuals);
		
	}
	
	@Test
	public void testNeighborhood() throws FailedPointAddition, TrianglePointMissing {
		Mesh mesh = makeTestMesh();
		Set<Point> neighbors = mesh.getNeighbors(mesh.getPoint(0));
		assertTrue(neighbors.contains(mesh.getPoint(1)));
		assertTrue(neighbors.contains(mesh.getPoint(2)));
		assertTrue(neighbors.contains(mesh.getPoint(3)));
		assertFalse(neighbors.contains(mesh.getPoint(4)));
	}

	private Point makePoint(double x, double y, double z) {
		Point p = new Point();
		p.setCoordinates(new Point3d(x, y, z));
		return p;
	}
	
	private Mesh makeTestMesh() throws FailedPointAddition, TrianglePointMissing {
		Point[] points = {
				makePoint(0.0,0.0,0.0),
				makePoint(1,0,0),
				makePoint(0,1,0),
				makePoint(0,0,1),
				makePoint(0,1,1)
		};
		
		Mesh mesh = new Mesh();
		
		for(Point p : points) {
			mesh.addPoint(p);
		}
		
		mesh.addTriangle(0, 2, 1);		
		mesh.addTriangle(0, 3, 2);
		mesh.addTriangle(0, 1, 3);
		mesh.addTriangle(2, 3, 4);
		
		
		return mesh;
	}

}
