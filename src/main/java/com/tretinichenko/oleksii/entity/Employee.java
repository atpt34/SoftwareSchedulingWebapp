package com.tretinichenko.oleksii.entity;

public class Employee {

	private int id;
	private String name;
	private String email;
//	private Employee manager;
	private int managerId;
	
//	public Employee(){
//		id = 0;
//		name = "";
//		email = "";
//		managerId = 0;
//	}
	
	public Employee(int id, String name, String email, int managerId) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.managerId = managerId;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getManagerId() {
		return managerId;
	}


	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	
	
}
