package org.chof.surfcomp.trimesh.io.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.vecmath.Point3d;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.exception.FailedPointAddition;
import org.chof.surfcomp.trimesh.exception.TrianglePointMissing;
import org.chof.surfcomp.trimesh.exception.TrimeshException;
import org.chof.surfcomp.trimesh.io.OffWriter;
import org.junit.Test;

public class OffWriterTest extends IOTestCase{

	@Test
	public void test() throws IOException, TrimeshException {
		Mesh testMesh = makeTestMesh();
		OffWriter offWriter = new OffWriter();
		Writer writer = new StringWriter();
		offWriter.setWriter(writer);
		
		offWriter.write(testMesh);
		offWriter.close();
		
		BufferedReader refReader = new 
				BufferedReader(new InputStreamReader(loadTestFile("data/off/simpletest.off")));
		BufferedReader testData = new BufferedReader(new StringReader(writer.toString()));
		while (refReader.ready()) {
			assertEquals(refReader.readLine(), testData.readLine());	
		}
		//System.out.println(testData.readLine());
		//assertFalse(testData.ready());
		
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

	private Point makePoint(double x, double y, double z) {
		Point p = new Point();
		p.setCoordinates(new Point3d(x, y, z));
		return p;
	}
	
}
