package org.chof.surfcomp.trimesh.tools;

public class DataFeatures {
	
	/**
	 * Specifies if a format has point coordinates for each surface vertex
	 */
	public static final int HAS_POINT_COORDINATES = 1<<0;
	
	/**
	 * Specifies if a format has normal vector information for each surface vertex
	 */
	public static final int HAS_POINT_NORMALS = 1<<1;
	
	/**
	 * Specifies if a format contains triangulation information
	 */
	public static final int HAS_TRIANGULATION = 1<<2;
	
	/**
	 * Specifies if a format contains triangle normals
	 */
	public static final int HAS_TRIANGLE_NORMALS = 1<<3;
	
	/**
	 * Specifies if a format contains curvature values for each vertex point
	 */
	public static final int HAS_CURVATURES = 1<<4;

}
