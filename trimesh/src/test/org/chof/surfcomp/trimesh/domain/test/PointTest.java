package org.chof.surfcomp.trimesh.domain.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.domain.Point;
import org.junit.Test;
import org.junit.experimental.theories.Theory;

public class PointTest {
	
	@Test
	public void testPointSetup() {
		
		 double[] coordinates= { 0.0, 0.0, 0.0 };
		 double[] normale  = { 0.0, 0.0, 1.0 };
		 
		 Point point = new Point();
		 
		 coordinates[1] = 0.5;
		 point = new Point(new Point3d(coordinates));
		 testPoint(coordinates, normale, point);
		 
		 coordinates[2] = 0.5;
		 normale[1] = 1;
		 normale[2] = 0;
		 point = new Point(new Point3d(coordinates), new Vector3d(normale));
		 testPoint(coordinates, normale, point);
		 
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

	private void testPoint(double[] coordinates, double[] normale,
			Point point) {
		double[] triple = new double[3];

		 point.getCoordinates().get(triple);
		 assertArrayEquals(coordinates, triple, DomainTests.doubleDelta);
		 point.getNormale().get(triple);
    	 assertArrayEquals(normale, triple, DomainTests.doubleDelta);
	}

}
