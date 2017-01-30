package com.tretinichenko.oleksii.entity;

import java.io.Serializable;

public class Account implements Serializable {

	private static final long serialVersionUID = -2934138836765761526L;
	
	private String userName;
	private String password;
	private boolean active;
	private UserRole userRole;  // maybe Enum of roles necessary
	
	private int employeeId;  // type?
	
}
