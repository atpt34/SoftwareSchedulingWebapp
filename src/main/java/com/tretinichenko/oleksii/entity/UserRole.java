package com.tretinichenko.oleksii.entity;

public enum UserRole {

	ADMINISTRATOR, MANAGER, EMPLOYEE, CUSTOMER;
	
	public static UserRole getUserRole(String value) {
		// switch not supported
		if ("admin".equals(value)) { 
			return UserRole.ADMINISTRATOR;
		} else if ("manager".equals(value)) {
			return UserRole.MANAGER;
		} else if ("employee".equals(value)) {
			return UserRole.EMPLOYEE;
		} else if ("customer".equals(value)) {
			return UserRole.CUSTOMER;
		} else {
			return null;
		}
	}
	
}
