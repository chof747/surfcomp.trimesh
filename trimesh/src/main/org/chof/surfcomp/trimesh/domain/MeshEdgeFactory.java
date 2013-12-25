package org.chof.surfcomp.trimesh.domain;

import org.chof.surfcomp.trimesh.domain.Triangle.Corner;
import org.jgrapht.EdgeFactory;

public class MeshEdgeFactory  implements
		EdgeFactory<Point, MeshEdge> {
	
	protected Triangle triangle;
	
	/**
	 * @param triangle the triangle to set for edge generation
	 */
	public void setTriangle(Triangle triangle) {
		this.triangle = triangle;
	}

	@Override
	public MeshEdge createEdge(Point sourceVertex, Point targetVertex) {
		Corner startCorner = triangle.getCornerByPoint(sourceVertex);
		if (targetVertex == triangle.getCorner(startCorner.getNext())) {
			return new MeshEdge(triangle, startCorner);
		}
		else {
			throw new 
			  IllegalArgumentException("The source and target vertex must be on the trianlge");
		}
	}

}
