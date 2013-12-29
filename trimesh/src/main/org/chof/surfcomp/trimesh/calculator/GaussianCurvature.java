package org.chof.surfcomp.trimesh.calculator;

import java.util.Set;
import java.util.Vector;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.MeshEdge;
import org.hamcrest.core.IsInstanceOf;

/**
 * Calculates the gaussian curvature of a point on the surface
 * <p>
 * The gaussian curvature is caluclated by:</p>
 * <p>
 * curvature = 2*Pi - SUM(alpha_i) over all triangle angles at the point
 * @author chof
 */
public class GaussianCurvature extends DefaultCalculator {

	@Override
	public Object getPropertyDefinition() {
		return "GaussianCurvature";
	}


	@Override
	public Class<? extends Object> getPropertyType() {
		return Double.class;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Vector<T> calculate(Mesh mesh) {
		
		Vector<Double> result = new Vector<Double>(mesh.sizePoints());
		
		for(int i=0;i<mesh.sizePoints();++i) {
			Set<MeshEdge> edges = mesh.getEdgesOf(i);
		    double anglesum = 0;
		    
		    for(MeshEdge edge : edges) {
		    	anglesum += edge.getTriangle().getAngle(edge.getStart());
		    }
		    
		    double curvature = 2 * Math.PI - anglesum; 
		    result.add(curvature);
		    storePropertyInContainer(mesh.getPoint(i), curvature);
		}
		
		return (Vector<T>) result;
	}

}
