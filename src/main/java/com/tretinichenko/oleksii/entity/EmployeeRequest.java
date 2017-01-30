package com.tretinichenko.oleksii.entity;

public class EmployeeRequest {

	private int id;
	private int taskId;
	private int employeeId;
	private int hours;
	
	
	
	public EmployeeRequest(int id, int taskId, int employeeId, int hours) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.hours = hours;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	
	
}
