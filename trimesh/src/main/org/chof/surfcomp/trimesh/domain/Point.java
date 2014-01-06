package org.chof.surfcomp.trimesh.domain;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Point extends SimpleSurfaceElement {

	protected Point3d coordinates;
	protected Vector3d normale;
	
	/**
	 * Standard constructor setting the coordinates to the origin of the 
	 * coordinate system and pointing the normale straight up the z axe
	 */
	public Point() {
		setCoordinates(new Point3d(0,0,0));
		setNormale(new Vector3d(0,0,1));
	}
	
	/**
	 * Constructor setting the coordinates to the specified parameter and 
	 * pointing the normale straight up the z axe
	 * 
	 * @param coordinates the coordinates of the point
	 */
	public Point(Point3d coordinates) {
		setCoordinates(coordinates);
		setNormale(new Vector3d(0,0,1));
	}
	
	/**
	 * Constructor setting the coordinates to the specified parameter and 
	 * the sets the normale to the specified normals
	 * 
	 * @param coordinates the coordinates of the point
	 * @param normale the vector indicating the outside of the surface
	 */
	public Point(Point3d coordinates, Vector3d normale) {
		setCoordinates(coordinates);
		setNormale(normale);
	}
	
	/**
	 * copy constructor duplicating the information of the source to a new point
	 * @param source a source point
	 */
	public Point(Point source) {
		super(source);
		this.coordinates = new Point3d(source.coordinates);
		this.normale = new Vector3d(source.normale);
	}

	/**
	 * @return the coordinates of the surface point
	 */
	public Point3d getCoordinates() {
		return coordinates;
	}
	/**
	 * @param coordinates the new coordinates of the surface point
	 */
	public void setCoordinates(Point3d coordinates) {
		if (coordinates != null) { 
		  this.coordinates = coordinates;
		} else {
			throw new IllegalArgumentException("Surface point coordinates must not be 0");
		}
	}
	/**
	 * @return the vector representing the surface normal
	 */
	public Vector3d getNormale() {
		return normale;
	}
	/**
	 * @param normal a new  surface normal for the point
	 */
	public void setNormale(Vector3d normal) {
		if (normal != null) { 
			  this.normale = normal;
			} else {
				throw new IllegalArgumentException("Surface normal must not be 0");
			}
	}
}
