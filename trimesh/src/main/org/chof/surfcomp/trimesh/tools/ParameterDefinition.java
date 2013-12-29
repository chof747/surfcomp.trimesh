package org.chof.surfcomp.trimesh.tools;

public class ParameterDefinition implements Comparable<ParameterDefinition>{

	protected String definition;
	protected Class<? extends Object> type;
	protected boolean mandatory = true;
	protected Object defaultValue;
	protected String description;

	/**
	 * Simple constructor constructing a parameter definition with a definition
	 * (name), type and a flag for a mandatory or optional parameter.
	 * 
	 * @param definition the definition or name of the parameter definition
	 * @param type the parameters type
	 * @param mandatory a flag indicating if it is mandatory or not
	 * 
	 * @throws IllegalArgumentException
	 */
	public ParameterDefinition(String definition, Class<? extends Object> type,
			boolean mandatory) {
		if ((definition != null) && (type != null)) {
			this.definition = definition;
			this.type = type;
			this.mandatory = mandatory;
			this.defaultValue = null;
			this.description = "";
		} else {
			throw new IllegalArgumentException("definition and type must not be null!");
		}
	}

	/**
	 * Extended constructor catering for an (textual) additional description of the parameter
	 * @param description
	 * @see #ParameterDefinition(String, Class, boolean)
	 */
	public ParameterDefinition(String definition, Class<? extends Object> type,
			boolean mandatory, String description) {
		this(definition, type, mandatory);
		this.description = description;
	}

	/**
	 * Extended constructor building a parameter with description and a defaultValue
	 * @param defaultValue
	 * @throws ClassCastException 
	 */
	public ParameterDefinition(String definition, Class<? extends Object> type,
			boolean mandatory, String description, Object defaultValue) {
		this(definition, type, mandatory, description);
		
		if(type.isInstance(defaultValue)) {
			this.defaultValue = defaultValue;
		} else {
			throw new ClassCastException("Default Value has wrong type. expected: " + 
					type.getSimpleName() + " but got " + 
					defaultValue.getClass().getSimpleName()); 
		}
	}
	
	@Override
	public boolean equals(Object o) {
	  if (o instanceof ParameterDefinition) {
		if (((ParameterDefinition) o).getDefinition() == this.definition) {
			return true;
		}
	  } 
	  
	  return false;
	}
	
	@Override
	public int compareTo(ParameterDefinition o) {
		return definition.compareTo(o.definition);
	}

	/**
	 * @return String the definition (name) of the parameter
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Sets the definition of the parameter
	 * @param definition a new definition (name)
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * @return Class the type of the parameter
	 */
	public Class<? extends Object> getType() {
		return type;
	}

	/**
	 * Sets a new type for the parameter definition
	 * @param type a new type for the parameter definition
	 */
	public void setType(Class<? extends Object> type) {
		this.type = type;
	}

	/**
	 * @return boolean returns wether the parameter is mandatory or not
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * Sets if the parameter is mandatory or not 
	 * @param mandatory a flag indicating if the parameter is mandatory
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * @return retrieves the default value
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets a new default value for the parameter definition
	 * @param defaultValue a new default value
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return retrieves the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description sets a new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
