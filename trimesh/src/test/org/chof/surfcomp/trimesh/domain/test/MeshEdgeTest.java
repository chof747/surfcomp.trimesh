package org.chof.surfcomp.trimesh.domain.test;

import static org.junit.Assert.*;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.domain.MeshEdge;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.domain.Triangle;
import org.chof.surfcomp.trimesh.domain.Triangle.Corner;
import org.junit.Test;

public class MeshEdgeTest {

	@Test
	public void testSetup() {
		Point a = new Point(new Point3d(1.0,0.0,0.0)); 
		Point b = new Point(new Point3d(0.0,1.0,0.0)); 
		Point c = new Point(new Point3d(0,0.3,1));
		Triangle t = new Triangle(a, b, c);
		
		MeshEdge edge = new MeshEdge(t, Corner.B);
		assertEdge(b, c, edge, Corner.B);
		
		edge = new MeshEdge(t, a);
		assertEdge(a, b, edge, Corner.A);
	}

	private void assertEdge(Point start, Point end, MeshEdge edge, Corner corner) {
		
		Vector3d side = new Vector3d(end.getCoordinates());
		side.sub(start.getCoordinates());
		
		assertEquals(corner, edge.getStart());
		assertEquals(start, edge.getStartPoint());
		assertEquals(corner.getNext(), edge.getEnd());
		assertEquals(end, edge.getEndPoint());
		assertEquals(side.length(), edge.getWeight(), DomainTests.doubleDelta);
	}

}
