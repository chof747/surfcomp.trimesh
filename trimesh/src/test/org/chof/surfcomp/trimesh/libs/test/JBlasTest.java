package org.chof.surfcomp.trimesh.libs.test;

import static org.junit.Assert.*;

import org.jblas.DoubleMatrix;
import org.junit.Test;

public class JBlasTest {

	@Test
	public void test() {
		DoubleMatrix A = new DoubleMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
            });
		DoubleMatrix x = new DoubleMatrix(3, 1, 1.0, 2.0, 3.0);
		DoubleMatrix y = new DoubleMatrix();

		y = A.mmul(x);
		assertEquals(14.0, y.get(0, 0), Double.MIN_VALUE);
		assertEquals(32.0, y.get(1, 0), Double.MIN_VALUE);
		assertEquals(50.0, y.get(2, 0), Double.MIN_VALUE);
	}

}
