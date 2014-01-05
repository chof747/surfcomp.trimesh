package org.chof.surfcomp.calculator.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.chof.surfcomp.trimesh.calculator.CanonicalCurvature;
import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.exception.TrimeshException;
import org.chof.surfcomp.trimesh.io.MSMSReader;
import org.chof.surfcomp.trimesh.io.test.IOTestCase;
import org.junit.Test;

public class CanonicalCurvatureTest extends IOTestCase{

	@Test
	public void testCanonicalProperty() throws TrimeshException, IOException {
		Mesh mesh = loadTestSurface();
		
		HashMap<Point, Integer> pindex = makePointIndex(mesh);
		
		Point actual = mesh.getPoint(341);
		assertEquals(341, pindex.get(actual).intValue());
		
		actual = mesh.getPoint(414);
		assertEquals(414, pindex.get(actual).intValue());
		
		
		CanonicalCurvature curvCalculator = new CanonicalCurvature();
		curvCalculator.setParameter("CutOff", 2.0);
		curvCalculator.calculate(mesh);
		System.out.println(mesh.getPoint(4).getProperty("CanonicalCurvature").toString());
		System.out.println(printNeighbors(mesh, pindex, 4));
		System.out.println(mesh.getPoint(341).getProperty("CanonicalCurvature").toString());
		System.out.println(printNeighbors(mesh, pindex, 341));
		System.out.println(mesh.getPoint(917).getProperty("CanonicalCurvature").toString());
		System.out.println(printNeighbors(mesh, pindex, 917));
	}

	private String printNeighbors(Mesh mesh, HashMap<Point, Integer> pindex, int i) {
		Set<Point> neighbors = mesh.getNeighbors(mesh.getPoint(i));
		String result = "";
		for(Point p : neighbors) {
			result += pindex.get(p) + " ";
		}
		
		return "[" + result + "]";
	}

	private HashMap<Point, Integer> makePointIndex(Mesh mesh) {
		HashMap<Point, Integer> pIndex = new HashMap<Point, Integer>();
		int i=0;
		for(Point p : mesh.getPoints()) {
			pIndex.put(p, i++);
		}
		
		return pIndex;
	}

	private Mesh loadTestSurface() throws TrimeshException, IOException {
		MSMSReader reader = new MSMSReader();
		reader.setReader(loadTestFile("data/msms/1crn.msms"));
		Mesh mesh = reader.read(new Mesh());
		reader.close();
		return mesh;
	}
	
	//@Test
	public void testCanonicalVector() throws TrimeshException, IOException {
		Mesh mesh = loadTestSurface();

		CanonicalCurvature curvCalculator = new CanonicalCurvature();
		curvCalculator.setParameter("StoreProperty", false);
		Vector<Double> curvature = curvCalculator.calculate(mesh);
		
		assertNull(mesh.getPoint(4).getProperty("CanonicalCurvature"));
		assertEquals(1.359347638,
			   curvature.get(4), 1e-9);
	}

}
