package org.chof.surfcomp.trimesh.domain;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.exception.TrianglePointMissing;
import org.chof.surfcomp.trimesh.tools.TrigomFunction;

/**
 * Class representation of a surface triangle
 * <p>
 * The mesh is represented as a graph and its edges are the direct connections between 
 * two adjacent points. Each edge is referenced by the starting point and the triangle
 * residing counter-clock-wise adjacent to the edge (i.e. the edge is a mathematical side of the triangle</p>
 * 
 * @author chof
 */
public class Triangle extends SimpleSurfaceElement {
	
	public enum Corner {
		A(),
		B(),
		C();
		
		private Corner next;
		private Corner prev;
		
		private Corner() {
			next = null;
			prev = null;
		}
		
		public Corner getNext() {
			if (next == null) {
				next = (this == A) 
						? B 
						: (this == B) 
						  ? C
					      : A;
			}
			
			return next;
		}

		public Corner getPrev() {
			if (prev == null) {
				prev = (this == A) 
						? C 
						: (this == B) 
						  ? A
					      : B;
			}
			
			return prev;
		}
	}

	/**
	 * Reference to the corner points of the triangle
	 */
	protected EnumMap<Corner, Point> corners;
	
	/**
	 * Cash for the edges of the triangle
	 */
	protected EnumMap<Corner, Vector3d> edges;

	/**
	 * cache value containing the area of the triangle.
	 * 
	 * This value is always regenerated when the point references of the triangle change
	 */
	private Double area;
	
	/**
	 * The surface normal of the triangle
	 * 
	 * This value is always regenerated when the point references of the triangles change
	 */
	private Vector3d normale;
	
	/**
	 * Standard constructor initializing corners and setting area and normale to null
	 */
	public Triangle() {
		initialize();
	}

	/**
	 * Constructs a triangle with all 3 points
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	public Triangle(Point a, Point b, Point c) {
		
		initialize();
		setCorners(a, b, c);
	}
	
	/**
	 * Copy Constructor taking all information from one triangle to setup a new triangle
	 * with the given point mapping
	 * <p>
	 * The constructor also considers, that for a new triangle also the points have to be 
	 * copied and new points - which correspond to the original points via a 1:1 mapping -
	 * need to be used as corners of the new triangle.</p>
	 * <p>
	 * <b>Note:</p> It is assumed, that the positions of the new points in space are equal
	 * to the positions of the old points, thus any calculated edge and area are also 
	 * copied</p>
	 * 
	 * @param source the source triangle
	 * @param pointMap the mapping from old to new points
	 * @throws TrianglePointMissing if one of the source triangle points is not 
	 *         present in the point map
	 */
	public Triangle(Triangle source, Map<Point, Point> pointMap) throws TrianglePointMissing {
		super(source);
		
		Point a = pointMap.get(source.corners.get(Corner.A));
		Point b = pointMap.get(source.corners.get(Corner.B));
		Point c = pointMap.get(source.corners.get(Corner.C));
		
		if ((a!=null) && (b!=null) && (c!=null)) {
			initialize();
			setCorners(a, b, c);
			
			this.area = source.area;
			if (source.normale != null) {
				normale = new Vector3d(source.normale);
			}
			
			for(Corner x : Corner.values()) {
				Vector3d edge = source.edges.get(x);
				if (edge != null) {
					edges.put(x, new Vector3d(edge));
				}
			}
			
		} else {
			throw new TrianglePointMissing("One or more of the source triangle points are missing in the point map", 
					a == null, b == null, c == null);
		}
	}
	
	private void initialize() {
		area = null;
		normale = null;
		corners = new EnumMap<Corner, Point>(Corner.class);
		edges = new EnumMap<Corner, Vector3d>(Corner.class);
	}
	

	/**
	 * Sets all corner points of the triangle at the same time
	 * 
	 * All points must contain non-null values
	 * 
	 * @param a corner point A
	 * @param b corner point B
	 * @param c corner point C
	 */
	public void setCorners(Point a, Point b, Point c) {
		setCorner(Corner.A, a);
		setCorner(Corner.B, b);
		setCorner(Corner.C, c);
	}

	/**
	 * Sets a specific corner point
	 * 
	 * The parameter p must be non-null
	 * 
	 * @param corner the corner to set
	 * @param p the point reference for the triangles corner
	 */
	public void setCorner(Corner corner, Point p) {
		if (p != null) {
			corners.put(corner, p);
			area = null;
			edges.remove(corner);
			edges.remove(corner.getPrev());
		} else {
			throw new IllegalArgumentException("A triangle point must not be null");
		}
	}
	
	/**
	 * Retrieves the corner point of the triangle
	 * @param corner the corner point of the triangle required
	 * @return the reference to the Point3d object representing the corner point
	 */
	public Point getCorner(Corner corner) {
		return corners.get(corner);
	}
	
	/**
	 * Retrieves the corresponding corner of a triangle represented by the point 
	 */
	public Corner getCornerByPoint(Point point) {
		if (point != null) {
		
			for (Entry<Corner, Point> entry : corners.entrySet()) {
				if (entry.getValue() == point) {
					return entry.getKey();
				}
			}
		}
		
		return null;
	}	
	/**
	 * Retrieves the side opposite the provided corner
	 * <p>
	 * Retrieves the mathematically associated side of the triangle for the given
	 * corner:</p>
	 * <p><code>
	 * A -> side a = edge between B and C
	 * B -> side b = edge between C and A
	 * C -> side c = edge between A and B</code></p>
	 * 
	 * @param corner the corner opposite the side wanted
	 * @return Vector3d the vector describing the side of the triangle
	 */
	public Vector3d getSide(Corner corner) {
		return getEdge(corner.getNext());
	}
	
	/**
	 * Retrieves the vector for the edge starting at the given corner
	 * <p>
	 * The edge is the triangle side starting at the corner and reaching to the 
	 * next point in counter-clockwise order.</p>
	 * <p>
	 * The difference between edge and side is that an edge starts at the corner and the
	 * side lies opposite the corner</p>
	 * 
	 * @param corner the respective start of the edge
	 * @return the side starting at that corner = edge
	 */
	public Vector3d getEdge(Corner corner) {
		Vector3d edge = edges.get(corner); 
		if (edge == null) {
			Point start = corners.get(corner);
		    if (start != null) {
		    	Point end = corners.get(corner.getNext());
		    	if (end != null) {
		    		edge = new Vector3d(end.getCoordinates());
		    		edge.sub(start.getCoordinates());
		    		edges.put(corner, edge);
		    	} 
		    } 
		}
		return (Vector3d) edge.clone();
	}


	/**
	 * Returns the area of the triangle
	 * 
	 * <p>The area is calculated by herons formula:</p>
	 * 
	 * <p><code>
	 * A = sqrt(s(s-a)(s-b)(s-c))
	 * with 
	 *   s = (a+b+c)/2</code></p>
	 * <p>  
	 * Once calculated the area is cached but recalculated whenever a new point is assigned.</p>
	 * <p>
	 * <b>Note</b>: No recalculation is done if the point changes the position as it is 
	 * assumed that triangles used by this model are static at least in their internal 
	 * settings</p>
	 * 
	 * @return the area of the triangle
	 */
	public Double getArea() {
		if (area == null) {
			//herons formula sqrt[s(s-a)(s-b)(s-c)] when s = (a+b+c)/2
			double c = getEdge(Corner.A).length();
			double a = getEdge(Corner.B).length();
			double b = getEdge(Corner.C).length();
			
			double s = (a+b+c)/2;
			area = new Double(Math.sqrt(s*(s-a)*(s-b)*(s-c)));
		}
		
		return area;
	}
	
	/**
	 * Retrieves the surface normal of the triangle pointing away from the face 
	 * in counter-clock order.
	 * <p><code>
	 * normale = -(side_c x side_a) / | side_c x side_a |</code></p>
	 * <p>
	 * The value is cached but recalculated whenever the references to the point change
	 * @see #getArea() </p>
	 * 
	 * @return Vector3d describing the surface normal pointing outward
	 */
	public Vector3d getNormale() {
		if (normale == null) {
			Vector3d sideA = getSide(Corner.A);
			Vector3d sideC = getSide(Corner.C);
			if ((sideA != null) && (sideC != null)) {
				sideC.scale(-1);
				normale = new Vector3d();
				normale.cross(sideA, sideC);
				normale.normalize();
			} 
		}
		
		return normale;
	}

	/**
	 * Retrieves the angle at the corner of the triangle
	 * @param corner the triangle corner
	 * @return the angle in radiant
	 */
	public double getAngle(Corner corner) {
		double cos = getCosAngle(corner);
		return Math.acos(cos);
	}
	
	/**
	 * Retrieves a trigonometric function of the angle at the corner
	 * @param corner the requested corner
	 * @param trigom the trigonometric function
	 * @return the result of the trigonometric function of the angle at that corner
	 */
	public double getAngleFunction(Corner corner, TrigomFunction trigom) {
		return trigom.calculateFromCos(getCosAngle(corner));
	}
	
	/**
	 * Retrieves the corner with an obtuse angle, if there is any. An obtuse
	 * angle is any angle in the triangle with more than 90 degree or pi/2 radiant.
	 * As the sum of angles in a triangle is pi or 180 degrees there can only be one
	 * obtuse angle 
	 * 
	 * @return the corner of the obtuse angle, if any or null if 
	 */
	public Corner getObtuseAngle() {
		return (getCosAngle(Corner.A) < 0) 
			   ? Corner.A
		       : (getCosAngle(Corner.B) < 0) 
		       		? Corner.B
		       	    : (getCosAngle(Corner.C) < 0)
		       	    	? Corner.C
		       	    	: null;
	}

	private double getCosAngle(Corner corner) {
		Vector3d side_a = getEdge(corner.getPrev());
		side_a.negate();
		Vector3d side_b = getEdge(corner);
		
	    return side_a.dot(side_b) / (side_a.length() * 	side_b.length());
	}	
}