package org.chof.surfcomp.trimesh.domain;

import java.util.Vector;

import org.chof.surfcomp.trimesh.exception.FailedPointAddition;
import org.chof.surfcomp.trimesh.exception.TrianglePointMissing;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Mesh {
	
	/**
	 * TODO: Implement datastructure as a directed weighted graph
	 * TODO: Implement Vertices as an vector of points (extra)
	 * TODO: Implement Edges as facedes to the triangles containing the start corner and 
	 *       the triangle
	 * TODO: 
	 */
	
	protected DefaultDirectedWeightedGraph<Point, MeshEdge> mesh;
	
	protected Vector<Point> points;
	protected Vector<Triangle> triangles;
	
	public Mesh() {
		intialize();
	}

	private void intialize() {
		points = new Vector<Point>();
		triangles = new Vector<Triangle>();
		mesh = new DefaultDirectedWeightedGraph<Point, MeshEdge>(new MeshEdgeFactory());
	}
	
	//**************************************************************************
	//Getters
	//**************************************************************************

	public Point getPoint(int index) {
		return points.get(index);
	}
	
	public Triangle getTriangle(int index) {
		return triangles.get(index);
	}
	
	public MeshEdge getEdge(int start, int end) {
		Point source = points.get(start);
		Point target = points.get(end);
		
		if ((source != null) && (target != null)) {
			return getEdge(source, target);
		} else {
			return null;
		}
	}
	
	public MeshEdge getEdge(Point source, Point target) {
		return mesh.getEdge(source, target);
	}
	
	/**
	 * @return the number of points in the mesh
	 */
	public int sizePoints() {
		return points.size();
	}
	
	/**
	 * @return the number of triangles in the mesh
	 */
	public int sizeTriangles() {
		return triangles.size();
	}
	
	//**************************************************************************
	// Manipulation methods
	//**************************************************************************

	public int addPoint(Point point) throws FailedPointAddition {
		if ((points.add(point)) && (mesh.addVertex(point))) {
			return points.size() - 1;
		} else {
			throw new FailedPointAddition("Failed to add a point to the mesh");
		}
	}	
	
	public int addTriangle(int a, int b, int c) throws TrianglePointMissing {
		Point pA = points.get(a);
		Point pB = points.get(b);
		Point pC = points.get(c);
		
		if ((pA != null) && (pB != null) && (pC != null)) {
			return addTriangle(pA, pB, pC);
		}
		else {
			throw new TrianglePointMissing("Missing point in Mesh for Triangle", 
				(pA == null),
				(pB == null),
				(pC == null));
		}
	}

	public int addTriangle(Point a, Point b, Point c) {
		Triangle t = new Triangle(a, b, c);
		((MeshEdgeFactory) mesh.getEdgeFactory()).setTriangle(t);
		
		if (buildEdges(a, b, c)) {
			triangles.add(t);
			return triangles.size()-1;
		}
		else {
			return -1;
		}
		
	}

	private boolean buildEdges(Point a, Point b, Point c) {
		
		MeshEdge[] edges = {
			mesh.addEdge(a, b),
			mesh.addEdge(b, c),
			mesh.addEdge(c, a)
		};
		
		boolean noEdge = false;
		
		for (MeshEdge e : edges) {
			if (e == null) {
				noEdge = true;
				mesh.removeEdge(e);
			}
		}
		
		return !noEdge;
	}
}
