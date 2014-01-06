package org.chof.surfcomp.trimesh.domain.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.domain.Point;
import org.junit.Test;

public class PointTest {
	
	@Test
	public void testPointSetup() {
		
		 double[] coordinates= { 0.0, 0.0, 0.0 };
		 double[] normale  = { 0.0, 0.0, 1.0 };
		 
		 Point point = new Point();
		 
		 coordinates[1] = 0.5;
		 point = new Point(new Point3d(coordinates));
		 assertPoint(coordinates, normale, point);
		 
		 coordinates[2] = 0.5;
		 normale[1] = 1;
		 normale[2] = 0;
		 point = new Point(new Point3d(coordinates), new Vector3d(normale));
		 assertPoint(coordinates, normale, point);
		 
	}
	
	@Test(expected = IllegalArgumentException.class) 
	public void testNullCoordinates() {
		Point p = new Point();
		p.setCoordinates(null);
	}

	@Test(expected = IllegalArgumentException.class) 
	public void testNullNormale() {
		Point p = new Point();
		p.setNormale(null);
	}
	
	@Test
	public void testPointProperties() {
		Point p = new Point();
		p.setProperty("curvature", 0.5);
		p.setProperty("potential", -0.12);
		p.setProperty("name", "peter");
		
		double val = p.getProperty("curvature");
		assertEquals(0.5, val, 1e-1);
		assertEquals("peter", p.getProperty("name", String.class));
		
		p.removeProperty("potential");
		assertNull(p.getProperty("potential"));
		
	    HashMap<Object, Object> otherProps = new HashMap<Object, Object>();
	    otherProps.put(p, "what");
	    otherProps.put("charged", true);
	    p.setProperties(otherProps);
	    assertEquals("what", p.getProperty(p));
	    assertTrue(p.getProperty("charged", Boolean.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPointPropertyWrongType() {
		Point p = new Point();
		p.setProperty("prop", true);
		p.getProperty("prop", Double.class);
	}
	
	@Test
	public void testCopyConstructor() {
		double[] coordinates = new double[] { 1.4, 2.3, -1.2 };
		double[] normale = new double[] { 0.2, 0.4, -0.1 };

		Point p = new Point();
		p.setCoordinates(new Point3d(coordinates));
		p.setNormale(new Vector3d(normale));
		p.setProperty("prop1", 3.2);
		p.setProperty("prop2", "TestProp");
		
		Point q = new Point(p);
		p.setCoordinates(new Point3d(new double[] { 0, 0, 0}));
		
		assertPoint(coordinates, normale, q);
		assertEquals(q.getProperty("prop1", Double.class), 3.2, DomainTests.doubleDelta);
		assertEquals(q.getProperty("prop2", String.class),"TestProp"); 
	}

	private void assertPoint(double[] coordinates, double[] normale,
			Point point) {
		double[] triple = new double[3];

		 point.getCoordinates().get(triple);
		 assertArrayEquals(coordinates, triple, DomainTests.doubleDelta);
		 point.getNormale().get(triple);
    	 assertArrayEquals(normale, triple, DomainTests.doubleDelta);
	}

}
