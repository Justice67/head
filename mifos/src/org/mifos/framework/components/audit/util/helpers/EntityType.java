/**
 * 
 */
package org.mifos.framework.components.audit.util.helpers;

public class EntityType {
	
	String name;
	
	String classPath;
	
	EntitiesToLog entitiesToLogs;
	
	PropertyName[] propertyNames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PropertyName[] getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(PropertyName[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	public EntitiesToLog getEntitiesToLog() {
		return entitiesToLogs;
	}

	public void setEntitiesToLog(EntitiesToLog entitiesToLog) {
		this.entitiesToLogs = entitiesToLog;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}


}
