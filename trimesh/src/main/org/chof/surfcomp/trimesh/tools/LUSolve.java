package org.chof.surfcomp.trimesh.tools;

import org.jblas.Decompose.LUDecomposition;
import org.jblas.DoubleMatrix;

public class LUSolve {

	static public DoubleMatrix solve(LUDecomposition<DoubleMatrix> lu, 
									 DoubleMatrix vectorB) {
		  if (lu.l.rows == vectorB.rows) {
		 
		  int i = 0;
		  int j = 0;
		  int n = vectorB.rows;
		  double[] x = new double[n];
		  double[] y = new double[n];
		  // Forward solve Ly = b
		  for (i = 0; i < n; i++)
		  {
		    y[i] = vectorB.get(i);
		    for (j = 0; j < i; j++)
		    {
		      y[i] -= lu.l.get(i,j) * y[j];
		    }
		    y[i] /= lu.l.get(i,i);
		  }
		  // Backward solve Ux = y
		  for (i = n - 1; i >= 0; i--)
		  {
		    x[i] = y[i];
		    for (j = i + 1; j < n; j++)
		    {
		      x[i] -= lu.u.get(i,j) * x[j];
		    }
		    x[i] /= lu.u.get(i,i);
		  }
		  return new DoubleMatrix(x);		
		  }
		  else {
			  throw new IllegalArgumentException("The LU Matrices and the B vector must have the same number of rows!");
		  }
	}

}
