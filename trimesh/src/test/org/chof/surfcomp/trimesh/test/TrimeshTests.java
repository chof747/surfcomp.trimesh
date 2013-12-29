package org.chof.surfcomp.trimesh.test;

import org.chof.surfcomp.trimesh.domain.test.DomainTests;
import org.chof.surfcomp.trimesh.io.test.IOTests;
import org.chof.surfcomp.trimesh.tools.test.ToolTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({DomainTests.class, IOTests.class, ToolTests.class})
public class TrimeshTests {

}
