package org.chof.surfcomp.trimesh.domain;

import org.chof.surfcomp.trimesh.domain.Triangle.Corner;
import org.jgrapht.graph.DefaultWeightedEdge;

public class MeshEdge extends DefaultWeightedEdge{
	

	private static final long serialVersionUID = 6555475706879038285L;

	protected Triangle triangle;
	protected Corner start;
	protected double weight;
	
	/**
	 * Constructs a MeshEdge with the triangle residing to the mesh and the
	 * corner of the triangle that is the origin of the edge
	 * 
	 * @param triangle the triangle that resides counterclockwise to the edge
	 * @param start the starting corner
	 */
	public MeshEdge(Triangle triangle, Corner start) {
		initialize(triangle, start);
	}
	
	/**
	 * Constructs a MeshEdge with the triangle residing to it ccw and the 
	 * point (which has to be a corner of the triangle) which is the origin of the
	 * edge
	 * 
	 * @param triangle the triangle that resides cc-wise to the edge
	 * @param point the point which is part of the triangle and is the origin of 
	 *              the mesh
	 */
	public MeshEdge(Triangle triangle, Point point) {
		initialize(triangle, triangle.getCornerByPoint(point));
	}

	/**
	 * Initializes the edge
	 */
	private void initialize(Triangle triangle, Corner start) {
		if ((triangle != null) && (start != null)) {
			this.triangle = triangle;
			this.start = start;
			this.weight = -1.0;
		} else {
			throw new IllegalArgumentException("triangle and start corner must not be null");
		}
	}

	/**
	 * @return the triangle which resides cc-wise to the edge
	 */
	public Triangle getTriangle() {
		return triangle;
	}

	/**
	 * @return the start corner of the edge on the triangle
	 */
	public Corner getStart() {
		return start;
	}
	
	/**
	 * @return the starting point of the edge 
	 */
	public Point getStartPoint() {
		return triangle.getCorner(getStart());
	}

	/**
	 * 
	 * @return the end point of the edge
	 */
	public Corner getEnd() {
		return start.getNext();
	}
	
	/**
	 * @return the end point of the edge
	 */
	public Point getEndPoint() {
		return triangle.getCorner(getEnd());
	}
	
	/**
	 * @return the weight of the edge which is equal to the euclidean distance 
	 *         of the start and end corner of the edge
	 */
	public double getWeight() {
		if (weight<0) {
			weight = triangle.getEdge(getStart()).length();
		}
		return weight;
	}
}
