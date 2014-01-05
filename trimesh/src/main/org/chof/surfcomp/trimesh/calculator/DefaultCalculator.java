package org.chof.surfcomp.trimesh.calculator;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.chof.surfcomp.trimesh.interfaces.IPropertyContainer;
import org.chof.surfcomp.trimesh.tools.ParameterDefinition;

public abstract class DefaultCalculator implements ICalculator {

	protected HashMap<String, ParameterDefinition> parameterDefinitions;
	protected HashMap<ParameterDefinition, Object> parameters;
	
	private boolean storeProperties = true;

	public DefaultCalculator() {
		super();
		parameters = new HashMap<ParameterDefinition, Object>();
		parameterDefinitions = new HashMap<String, ParameterDefinition>();
		
		intializeDefaultParameters();
	}

	private void intializeDefaultParameters() {
		ParameterDefinition storeProperty = new ParameterDefinition(
				"StoreProperty", Boolean.class, true, 
				"Denotes if a calculated value should be stored as a property", 
				true);
		parameterDefinitions.put(storeProperty.getDefinition(), storeProperty);
		parameters.put(storeProperty, storeProperty.getDefaultValue());
	}
	
	/**
	 * @see ICalculator
	 */
	@Override
	public abstract Object getPropertyDefinition();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.chof.surfcomp.trimesh.calculator.ICalculator#setParameter(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public boolean setParameter(String description, Object parameter) {
		ParameterDefinition paramDef = parameterDefinitions.get(description);
		if (paramDef != null) {
			parameters.put(paramDef, parameter);
			cacheStoreProperties();
			return true;
		} else {
			return false;
		}
	}

	private void cacheStoreProperties() {
		Boolean storeProperties = (Boolean) getParameter("StoreProperty");
		if (storeProperties != null) {
			this.storeProperties = storeProperties;
		} 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.chof.surfcomp.trimesh.calculator.ICalculator#getParameter(java.lang
	 * .String)
	 */
	@Override
	public Object getParameter(String description) {
		ParameterDefinition paramDef = parameterDefinitions.get(description);
		if (paramDef != null) {
			return parameters.get(paramDef);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.chof.surfcomp.trimesh.calculator.ICalculator#getParameterDefinitions
	 * ()
	 */
	@Override
	public Collection<ParameterDefinition> getParameterDefinitions() {
		return parameterDefinitions.values();
	}
	
	/**
	 * Method used to control the storage of calculated properties in the
	 * container. The method also performs a type check on the parameter and 
	 * the given value and throws a ClassCastException if they do not match.
	 * 
	 * @param container the property container to receive the calculated value
	 * @param value the calculated value
	 * @throws ClassCastException
	 */
	protected void storePropertyInContainer(IPropertyContainer container,
			Object value)  throws ClassCastException {
		if (storeProperties) {
			if (getPropertyType().isInstance(value)) {
				container.setProperty(getPropertyDefinition(), value);
			} else {
				throw new ClassCastException("Cannot cast "
						+ value.getClass().getSimpleName()
						+ " to the property type of "
						+ getPropertyType().getSimpleName());
			}
		}
	}
}