package org.chof.surfcomp.trimesh.domain;

import java.util.Set;
import java.util.Vector;

import org.chof.surfcomp.trimesh.exception.FailedPointAddition;
import org.chof.surfcomp.trimesh.exception.TrianglePointMissing;
import org.chof.surfcomp.trimesh.interfaces.IPropertyContainer;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Mesh {
	
	/**
	 * TODO: Implement datastructure as a directed weighted graph
	 * TODO: Implement Vertices as an vector of points (extra)
	 * TODO: Implement Edges as faces to the triangles containing the start corner and 
	 *       the triangle
	 * TODO: 
	 */
	
	protected DefaultDirectedWeightedGraph<Point, MeshEdge> mesh;
	
	protected Vector<Point> points;
	protected Vector<Triangle> triangles;
	
	/**
	 * Standard Constructor creating an empty triangular mesh
	 */
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

	/**
	 * @param index of the requested point in the mesh
	 * @return Point the point at the given index or null if no point is stored 
	 *         for the index
	 */
	public Point getPoint(int index) {
		return points.get(index);
	}
	
	/**
	 * @param index of the requested triangle in the mesh
	 * @return Triangle the requested triangle at the given index or null if no 
	 *         triangle is stored for the index
	 */
	public Triangle getTriangle(int index) {
		return triangles.get(index);
	}
	
	/**
	 * Retrieves an edge between two points by index of the points
	 * @param start the index of the source point
	 * @param end the index of the target point
	 * @return MeshEdge the edge between the points or null if they are not connected
	 */
	public MeshEdge getEdge(int start, int end) {
		Point source = points.get(start);
		Point target = points.get(end);
		
		if ((source != null) && (target != null)) {
			return getEdge(source, target);
		} else {
			return null;
		}
	}
	
	/**
	 * Retrieves an edge between two points by references of the points
	 * @param source the the source point
	 * @param target the the target point
	 * @return MeshEdge the edge between the points or null if they are not connected
	 */
	public MeshEdge getEdge(Point source, Point target) {
		return mesh.getEdge(source, target);
	}
	
	/**
	 * Retrieves the edges going out from the point specified by the given index ix
	 * @param ix the index of the point
	 * @return the set of edges going out from point ix
	 */
	public Set<MeshEdge> getEdgesOf(int ix) {
		return getEdgesOf(points.get(ix));
	}

	/**
	 * Retrieves the edges going out from the given point
	 * @param point the requested point
	 * @return the set of edges going out from the point
	 */
	public Set<MeshEdge> getEdgesOf(Point point) {
		return mesh.outgoingEdgesOf(point);
	}

	/**
	 * Retrieves a vector containing the values of a specific property for all points
	 * <p>
	 * The property is determined by the parameter \c description and the type of the
	 * resulting vector is determined by the class denoted by \c type.</p>
	 * <p>
	 * The vector will contain an entry for all points, nevertheless if a property is
	 * set for that point or not and the properties will be provided by the same
	 * indices as the points. If a point does not contain a property with the given 
	 * description, it will be set to the value denoted by the parameter \c defaultValue
	 * 
	 * @param description the description of the property (e.g. it's name)
	 * @param defaultValue a default value of type T assigned to all points which do not 
	 *        have the property set
	 * @return a vector of type T with a value for each point
	 */
	public <T> Vector<T> getPointPropertyVector(Object description, T defaultValue) {
		return getPropertyVector(points, description, defaultValue);
	}

	/**
	 * Retrieves a vector containing the values of a specific property for all triangles
	 * @see #getPointPropertyVector(Object, Object)
	 */
	public <T> Vector<T> getTrianglePropertyVector(Object description, T defaultValue) {
		return getPropertyVector(triangles, description, defaultValue);	
	}

	@SuppressWarnings("unchecked")
	private <T> Vector<T> getPropertyVector(
			Vector<? extends IPropertyContainer> data, Object description,
			T defaultValue) {
		Vector<T> propertyVector = new Vector<T>(data.size());

		for (IPropertyContainer p : data) {
			T value = (T) p.getProperty(description, defaultValue.getClass());
			if (value == null) {
				value = defaultValue;
			}
			propertyVector.add(value);
		}

		return propertyVector;
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
