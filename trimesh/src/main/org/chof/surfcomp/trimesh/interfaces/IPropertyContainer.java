package org.chof.surfcomp.trimesh.interfaces;

import java.util.Map;

public interface IPropertyContainer  {

	/**
	 * Sets a property
	 *
	 * @param  description  An object description of the property (most likely a
	 *                      unique string)
	 * @param  property     An object with the property itself
	 * @see                 #getProperty
	 * @see                 #removeProperty
	 */
	public void setProperty(Object description, Object property);
	
	/**
	 * Removes a property
	 *
	 * @param  description  The object description of the property (most likely a
	 *                      unique string)
	 * @see                 #setProperty
	 * @see                 #getProperty
	 */
	public void removeProperty(Object description);
	
	/**
	 * Returns a property - the object is automatically
     * cast to the required type. This does however mean if the wrong type is
     * provided then a runtime ClassCastException will be thrown.
     *
	 * <p/>
     * @param  description  An object description of the property (most likely a
	 *                      unique string)
     * @param  <T>          generic return type
	 * @return              The object containing the property. Returns null if
	 *                      property is not set.
	 * @see                 #setProperty
     * @see                 #getProperty(Object, Class)
	 * @see                 #removeProperty
	 */
	public <T> T getProperty(Object description);

    /**
     * Access a property of the given description and cast the specified class.
     * <p/>
     * @param description description of a property (normally a string)
     * @param c           type of the value to be returned
     * @param <T>         generic type (of provided class)
     * @return the value stored for the specified description.
     * @see #getProperty(Object)
     * @see #setProperties(java.util.Map)
     */
    public <T> T getProperty(Object description, Class<T> c);

	/**
	 *  Returns a Map with the properties.
	 *
	 *@return    The object's properties as an Map
	 *@see       #setProperties
	 */
	public Map<Object,Object> getProperties();

	/**
	 * Sets the properties of this object.
	 *
	 * @param  properties  a Map specifying the property values
	 * @see                #getProperties
	 */
	public void setProperties(Map<Object,Object> properties);
    
}


