package org.chof.surfcomp.trimesh.domain.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;

import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.domain.Triangle;
import org.chof.surfcomp.trimesh.domain.Triangle.Corner;
import org.chof.surfcomp.trimesh.tools.TrigomFunction;
import org.junit.Test;

public class TriangleTest {

	@Test
	public void testTriangleSetup() {

		Point a = new Point();
		Point b = new Point();
		Point c = new Point();
		a.setCoordinates(new Point3d(0.0, 0.0, 0.0));
		b.setCoordinates(new Point3d(0.0, 0.0, 2.0));
		c.setCoordinates(new Point3d(1.0, 1.0, 2.0));
		
		Triangle t = new Triangle(a, b, c);
		
	    assertEquals(a, t.getCorner(Corner.A));
	    assertEquals(b, t.getCorner(Corner.B));
	    assertEquals(c, t.getCorner(Corner.C));
	}
	
	@Test(
	    expected = IllegalArgumentException.class)
	public void testTriangleNullCorner() {
		Triangle t = new Triangle();
		
		assertNull(t.getCorner(Corner.A));
		t.setCorner(Corner.B, null);
	}
	
	@Test
	public void testTriangleArea() {
		
		Triangle t = new Triangle(
			new Point(new Point3d(1.0,0.0,0.0)), 
			new Point(new Point3d(0.0,1.0,0.0)), 
			new Point(new Point3d(0,0,1)));
		
		assertEquals(0.866025404, t.getArea(), 1e-9);

		t = new Triangle(
			new Point(new Point3d(6.5,2.4,-1.8)), 
			new Point(new Point3d(10.67,-14.75,20.21)), 
			new Point(new Point3d(-1.926, -11.524, 3.413)));
		
		assertEquals(181.0253063, t.getArea(), 1e-7);
			
	}
	
	@Test
	public void testSurfaceNormale() {
		Triangle t = new Triangle(
				new Point(new Point3d(1.0,0.0,0.0)), 
				new Point(new Point3d(0.0,1.0,0.0)), 
				new Point(new Point3d(0,0,1)));
		
		double[] expected = {
			0.577350269, 0.577350269, 0.577350269
		};
		double[] actual = new double[3];
		t.getNormale().get(actual);
		assertArrayEquals(expected, actual, 1e-9);

		t = new Triangle(
				new Point(new Point3d(6.5,2.4,-1.8)), 
				new Point(new Point3d(10.67,-14.75,20.21)), 
				new Point(new Point3d(-1.926, -11.524, 3.413)));
		
		double[] expected2 = {
			0.599541286, -0.572280402, -0.559504591
		};
		t.getNormale().get(actual);
		assertArrayEquals(expected2, actual, 1e-9);
	}
	
	@Test
	public void testTriangleEdgeSide() {
		Triangle t = new Triangle(
				new Point(new Point3d(1.0,0.0,0.0)), 
				new Point(new Point3d(0.0,1.0,0.0)), 
				new Point(new Point3d(0,0,1)));
		
		double[] expected = {
				-1, 1.0, 0
			};
		double[] actual = new double[3];
		t.getEdge(Corner.A).get(actual);
		assertArrayEquals(expected, actual, DomainTests.doubleDelta);

		t.getEdge(Corner.B).get(actual);
		t.getEdge(Corner.C).get(actual);

		double[] expected2 = {
				0,-1.0, 1.000
		};
		t.getSide(Corner.A).get(actual);
		assertArrayEquals(expected2, actual, DomainTests.doubleDelta);
	}
	
	@Test
	public void testGetCornerByPoint() {
		Point a = new Point(new Point3d(1.0,0.0,0.0)); 
		Point b = new Point(new Point3d(0.0,1.0,0.0)); 
		Point c = new Point(new Point3d(0,0,1));
		Triangle t = new Triangle(a, b, c);
		
		assertEquals(Corner.A, t.getCornerByPoint(a));
		assertEquals(Corner.B, t.getCornerByPoint(b));
		assertEquals(Corner.C, t.getCornerByPoint(c));
		
	}
	
	@Test
	public void testAngle() {
		Point a = new Point(new Point3d(1.0,0.0,0.0)); 
		Point b = new Point(new Point3d(0.0,1.0,0.0)); 
		Point c = new Point(new Point3d(0,0.3,1));
		Triangle t = new Triangle(a, b, c);
		assertEquals(0.635850784, t.getAngleFunction(Corner.A, TrigomFunction.COS), 1e-9);
		assertEquals(0.881685986, t.getAngle(Corner.A), 1e-9);		
	}
	
	@Test 
	public void testObtuseAngle() {
		Point a = new Point(new Point3d(0,0,0));
		Point b = new Point(new Point3d(10,0,0));
		Point c = new Point(new Point3d(20,1,1));
		
		Triangle t = new Triangle(a,b,c);
		assertEquals(Corner.B, t.getObtuseAngle());
		
		
	}
}
