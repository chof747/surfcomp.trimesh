package org.chof.surfcomp.trimesh.io.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.domain.Triangle;
import org.chof.surfcomp.trimesh.domain.Triangle.Corner;
import org.chof.surfcomp.trimesh.exception.TrimeshException;
import org.chof.surfcomp.trimesh.io.MSMSReader;
import org.junit.Test;

public class MSMSReaderTest extends IOTestCase{

	@Test
	public void test() throws TrimeshException, IOException {
		MSMSReader reader = new MSMSReader();
		reader.setReader(loadTestFile("data/msms/1crn.msms"));
		Mesh mesh = reader.read(new Mesh());
		reader.close();
		
		assertEquals(2474, mesh.sizePoints());
		assertEquals(4944, mesh.sizeTriangles());
		
		double[] expectedPoint  = { 25.578,    12.640,    10.156};
		double[] expectedNormal = {  0.809,    -0.476,    -0.345};
		double[] actual = new double[3];
		
		Point point = mesh.getPoint(2472);
		point.getCoordinates().get(actual);
		assertArrayEquals(expectedPoint, actual, 1e-3);
		point.getNormale().get(actual);
		assertArrayEquals(expectedNormal, actual, 1e-3);
		assertEquals(new Integer(1303), 
				     point.getProperty("faceNumber", Integer.class));
		assertEquals(new Integer(275), 
			     point.getProperty("sphereIndex", Integer.class));
		assertEquals(new Integer(1),
		         point.getProperty("faceType", Integer.class));
		assertEquals("michael_sanner",
		         point.getProperty("atomName", String.class));
		
		"1322     11     10".charAt(0);
		Triangle t = mesh.getTriangle(13);
		assertEquals(mesh.getPoint(1321), t.getCorner(Corner.A));
		assertEquals(mesh.getPoint(10), t.getCorner(Corner.B));
		assertEquals(mesh.getPoint(9), t.getCorner(Corner.C));
		
		assertEquals(new Integer(7), t.getProperty("faceNumber", Integer.class));
		assertEquals(new Integer(3), t.getProperty("faceType", Integer.class));
	}

}
