package org.chof.surfcomp.calculator.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Vector;

import org.chof.surfcomp.trimesh.calculator.GaussianCurvature;
import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.exception.TrimeshException;
import org.chof.surfcomp.trimesh.io.MSMSReader;
import org.chof.surfcomp.trimesh.io.test.IOTestCase;
import org.junit.Test;

public class GaussianCurvatureTest extends IOTestCase{

	@Test
	public void testGaussianProperty() throws TrimeshException, IOException {
		Mesh mesh = loadTestSurface();
		
		GaussianCurvature curvCalculator = new GaussianCurvature();
		curvCalculator.calculate(mesh);
		assertEquals(1.359347638, 
				     mesh.getPoint(4).getProperty("GaussianCurvature", Double.class), 1e-9);
	}

	private Mesh loadTestSurface() throws TrimeshException, IOException {
		MSMSReader reader = new MSMSReader();
		reader.setReader(loadTestFile("data/msms/gausscurvaturetest.msms"));
		Mesh mesh = reader.read(new Mesh());
		reader.close();
		return mesh;
	}
	
	@Test
	public void testGaussianVector() throws TrimeshException, IOException {
		Mesh mesh = loadTestSurface();

		GaussianCurvature curvCalculator = new GaussianCurvature();
		curvCalculator.setParameter("StoreProperty", false);
		Vector<Double> curvature = curvCalculator.calculate(mesh);
		
		assertNull(mesh.getPoint(4).getProperty("GaussianCurvature"));
		assertEquals(1.359347638,
			   curvature.get(4), 1e-9);
	}

}
