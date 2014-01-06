package org.chof.surfcomp.trimesh.domain;

import java.util.HashMap;
import java.util.Map;

import org.chof.surfcomp.trimesh.interfaces.IPropertyContainer;

public abstract class SimpleSurfaceElement implements IPropertyContainer {
	
	protected HashMap<Object, Object> propertyMap = null;
	
	/**
	 * Default constructor
	 */
	public SimpleSurfaceElement() {
		super();
	}

	/**
	 * Copy constructor duplicating all properties from the source to this
	 * object
	 * @param source the source simple surface element
	 */
	public SimpleSurfaceElement(SimpleSurfaceElement source) {
		lazyProperties().putAll(source.getProperties());
	}

	@Override
	public void setProperty(Object description, Object property) {
		lazyProperties().put(description, property);
	}

	private HashMap<Object, Object> lazyProperties() {
		if (propertyMap == null) {
			propertyMap = new HashMap<Object, Object>();
		}
		
		return propertyMap;
	}

	@Override
	public void removeProperty(Object description) {
		lazyProperties().remove(description);
	}

	@Override
	public <T> T getProperty(Object description) {
        // can't check the type
        @SuppressWarnings("unchecked")
        T value = (T) lazyProperties().get(description);
        return value;
	}

	@Override
	public <T> T getProperty(Object description, Class<T> c) {
		Object property = lazyProperties().get(description);
		if (c.isInstance(property)) {
	        @SuppressWarnings("unchecked")
	        T value = (T) lazyProperties().get(description);
	        return value;
		}
		else if (property != null) {
			 throw new IllegalArgumentException("Wrong type expected: " + c
	                    .getSimpleName() + " got " + property.getClass().getSimpleName());
		} else {
			return null;
		}
	}

	@Override
	public Map<Object, Object> getProperties() {
		return lazyProperties();
	}

	@Override
	public void setProperties(Map<Object, Object> properties) {
		lazyProperties().clear();
		lazyProperties().putAll(properties);
	}

}
