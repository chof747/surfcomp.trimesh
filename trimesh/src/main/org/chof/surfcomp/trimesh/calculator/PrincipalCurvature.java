package org.chof.surfcomp.trimesh.calculator;

import java.util.Vector;

import javax.vecmath.Vector2d;

import org.chof.surfcomp.trimesh.domain.Mesh;

/**
 * A curvature calculator
 * <p>
 * The curvature calculation is based on a paper by Zachmann et. al. in
 * 1992. It calculates approximated canonical curvatures for each point of the
 * surface based on the Hessian matrix of an elliptical or hyperbolical
 * paraboloid which are fitted to the neighborhood of the point in a least
 * squares sence.</p> 
 */
public class PrincipalCurvature extends DefaultCalculator {
	
	@Override
	public Object getPropertyDefinition() {
		return "PrincipalCurvature";
	}

	@Override
	public Class<? extends Object> getPropertyType() {
		return Vector2d.class;
	}

	public <T> Vector<T> calculate(Mesh mesh) {
		return null;
	}
	
}
