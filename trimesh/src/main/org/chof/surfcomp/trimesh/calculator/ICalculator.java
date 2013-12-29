package org.chof.surfcomp.trimesh.calculator;

import java.util.Collection;
import java.util.Vector;

import org.chof.surfcomp.trimesh.domain.Mesh;
import org.chof.surfcomp.trimesh.tools.ParameterDefinition;

public interface ICalculator {

	public boolean setParameter(String description, Object parameter);
	public Object getParameter(String description);
	public Collection<ParameterDefinition> getParameterDefinitions();
	
	public Object getPropertyDefinition();
	public Class<? extends Object> getPropertyType();
	
	public<T> Vector<T> calculate(Mesh mesh);
	
}