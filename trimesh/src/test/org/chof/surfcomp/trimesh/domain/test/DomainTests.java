package org.chof.surfcomp.trimesh.domain.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PointTest.class, 
				TriangleTest.class, 
				MeshEdgeTest.class,
				MeshTest.class})
public class DomainTests {

	static double doubleDelta = Double.MIN_NORMAL;

}
