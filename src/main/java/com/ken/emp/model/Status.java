package com.ken.emp.model;

import com.fasterxml.jackson.annotation.JsonValue;
/**
 * This enumeration encapsulates all possible status of employees
 * 
 * @author cmenerip
 *
 */
public enum Status {
	
	ACTIVE, INACTIVE;
	
	@JsonValue
	public String value() {
		return name();
	}
}
