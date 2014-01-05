package org.chof.surfcomp.trimesh.calculator;

import java.util.Set;
import java.util.Vector;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.chof.surfcomp.trimesh.algorithms.LimitedDepthFirstIterator;
import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.domain.MeshEdge;
import org.chof.surfcomp.trimesh.domain.Point;
import org.chof.surfcomp.trimesh.tools.LUSolve;
import org.chof.surfcomp.trimesh.tools.ParameterDefinition;
import org.jblas.Decompose;
import org.jblas.Decompose.LUDecomposition;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.jblas.Solve;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 * A curvature calculator
 * <p>
 * The curvature calculation is based on a paper by Zachmann et. al. in
 * 1992. It calculates approximated canonical curvatures for each point of the
 * surface based on the Hessian matrix of an elliptical or hyperbolical
 * paraboloid which are fitted to the neighborhood of the point in a least
 * squares sence.</p> 
 * <p>
 * The calculator has only one specific parameter:<(p>
 * <p>
 * <code>CutOff</code> the cutoff radius around each point which is used to 
 * define the reference points for the paraboloid. The default value is -1.0 
 * which means, that only the direct neighbors are used as reference points
 */
public class CanonicalCurvature extends DefaultCalculator {
	
	private Vector3d en;
	private Vector3d eu;
	private Vector3d ev;
	private DoubleMatrix matrixA;
	private DoubleMatrix vectorB;

	public CanonicalCurvature() {
		super();
		initializeParameters();
	}
	
	private void initializeParameters() {
		ParameterDefinition cutOffProperty = new ParameterDefinition(
				"CutOff", Double.class, true, 
				"Defines the cutoff radius for the determination of the calculating paraboloid", 
				-1.0);
		parameterDefinitions.put(cutOffProperty.getDefinition(), cutOffProperty);
		parameters.put(cutOffProperty, -1.0);		
	}

	@Override
	public Object getPropertyDefinition() {
		return "CanonicalCurvature";
	}

	@Override
	public Class<? extends Object> getPropertyType() {
		return Vector3d.class;
	}

	@SuppressWarnings("unchecked")
	public <T> Vector<T> calculate(Mesh mesh) {
		Vector<Vector3d> result = new Vector<Vector3d>(mesh.sizePoints());
		double cutoff = (Double) getParameter("CutOff"); 

		for(int i=0;i<mesh.sizePoints();++i) {
			Point p = mesh.getPoint(i);

			Set<Point> rim = getParaboloidRim(mesh, cutoff, p);			
			setupCoordinateSystem(p, rim);
			
			setupEquations(transformCoordinates(rim, p));
			Vector3d curvatures = 
				determineCurvatures(LUSolve.solve(Decompose.lu(matrixA), vectorB));
					
			result.add(curvatures);			
			storePropertyInContainer(p, curvatures);
			
			//System.out.println(i + " " + curvatures.toString());
		}
		return (Vector<T>) result;
	}

	private Vector3d determineCurvatures(DoubleMatrix solution) {
		Vector3d curvatures = new Vector3d();
		curvatures.x = eigenvalue(solution, 1);
		curvatures.y = eigenvalue(solution, -1);
		curvatures.z = calculateSTI(curvatures.x, curvatures.y);
		return curvatures;
	}
	
	private double eigenvalue(DoubleMatrix matrix, double sign) {
		  double diff02 = matrix.get(0) - matrix.get(2);
	
			if ((matrix.get(0)==Double.NaN) ||
			    (matrix.get(1)==Double.NaN) ||
		        (matrix.get(2)==Double.NaN))
			{
				return 0;
			}
			
		  return ((matrix.get(0) + matrix.get(2)) +
							sign * Math.sqrt(4*matrix.get(1)*matrix.get(1) + diff02*diff02));
	
	}

	private double calculateSTI(double cc1, double cc2) {
		if (((cc1>0) && (cc2>0)) || 
				((cc1>0) && (cc2<Double.MIN_VALUE) && (Math.abs(cc1)>Math.abs(cc2))))
			return (cc1-cc2)/cc1;
		else if (((cc1<0) && (cc2<0)) || 
						 ((cc1>0) && (cc2<=Double.MIN_VALUE) && (Math.abs(cc1)<=Math.abs(cc2))))
			return (cc1 + 3*cc2)/cc2;
	  else
			return -1.0;		
	}

	private void setupEquations(DoubleMatrix coordinates) {
		//necessary values
		double u4   = 0;
		double u3v  = 0;
		double u2v2 = 0;
		double uv3  = 0;
		double v4   = 0;
		double nu2  = 0;
		double nuv  = 0;
		double nv2  = 0;
		
		matrixA = DoubleMatrix.zeros(3,3);
		vectorB = DoubleMatrix.zeros(3, 1);


		  for(int r=0;r<coordinates.rows;++r)
		  {
		    double u = coordinates.get(r, 0);
		    double v = coordinates.get(r, 1);
		    double n = coordinates.get(r, 2);

		    double u2 = u * u;
		    double v2 = v * v;

		    u4   += u2 * u2;
		    u3v  += u2 * u * v;
		    u2v2 += u2 * v2;
		    uv3  += u * v2 * v;
		    v4   += v2 * v2;
		    nu2  += n * u2;
		    nuv  += n * v * u;
		    nv2  += n * v2;
		  }
		  
		  matrixA.put(0,0, u4);
		  matrixA.put(0,1, 2*u3v);
		  matrixA.put(0,2, u2v2);
		  matrixA.put(1,0, u3v);
		  matrixA.put(1,1, 2*u2v2);
		  matrixA.put(1,2, uv3);
		  matrixA.put(2,0, u2v2);
		  matrixA.put(2,1, 2*uv3);
		  matrixA.put(2,2, v4);

		  vectorB.put(0, nu2);
		  vectorB.put(1, nuv);
		  vectorB.put(2, nv2);
	}

	private DoubleMatrix transformCoordinates(Set<Point> rim, Point center) {
		DoubleMatrix coordinates = DoubleMatrix.zeros(rim.size(), 3);
		int i=0;
		for(Point b : rim) {
			Vector3d pos = new Vector3d(b.getCoordinates());
			pos.sub(center.getCoordinates());
			coordinates.put(i, 0, pos.dot(eu));
			coordinates.put(i, 1, pos.dot(ev));
			coordinates.put(i, 2, pos.dot(en));
			i++;
		}
		return coordinates;
	}

	private void setupCoordinateSystem(Point p, Set<Point> rim) {
		//setup the coordinate system by the surface normale of the point p and two axis
		//within the plane perpendicular to the surface normal at the point
		
		Vector3d b1 = new Vector3d();
		b1.sub(p.getCoordinates());

		en = new Vector3d(p.getNormale());
		en.normalize();
		
		eu = new Vector3d();
		eu.cross(en, b1);
		eu.normalize();
		
		ev = new Vector3d();
		ev.cross(en, eu);
		ev.normalize();
	}

	private Set<Point> getParaboloidRim(Mesh mesh, double cutoff, Point p) {
		Set<Point> rim;
		
		if (cutoff>0) {
			rim = selectRimAround(mesh, p, cutoff);
		} else {
			rim = mesh.getNeighbors(p);
		}
		return rim;
	}

	private Set<Point> selectRimAround(Mesh mesh, Point p, double cutoff) {
		LimitedDepthFirstIterator<Point, MeshEdge> iterator = 
			mesh.getLimitedDepthFirstIterator(p, cutoff);
		iterator.traverse();
		
		return iterator.getCompleteBorder();
	}
	
}
