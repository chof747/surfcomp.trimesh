package org.chof.surfcomp.trimesh.tools.test;

import static org.junit.Assert.*;

import org.chof.surfcomp.trimesh.tools.TrigomFunction;
import org.junit.Test;

public class TrigomFunctionTest {

	@Test
	public void testCalculateFromCos() {
		double cos = 0.5;

		assertEquals(Math.sqrt(1-cos*cos), 
				     TrigomFunction.SIN.calculateFromCos(cos), Double.MIN_VALUE);		
		assertEquals(Math.sqrt(1-cos*cos)/cos,
				     TrigomFunction.TAN.calculateFromCos(cos), Double.MIN_VALUE);
		assertEquals(cos/Math.sqrt(1-cos*cos),
			         TrigomFunction.COTAN.calculateFromCos(cos), Double.MIN_VALUE);
		assertEquals(cos, 
					 TrigomFunction.COS.calculateFromCos(cos), Double.MIN_VALUE);
	}

}
