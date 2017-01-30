package com.tretinichenko.oleksii.entity;

import java.util.Date;

public class TaskAssignment {

	private int id;
	private int taskId;  // or type Task
	private int employeeId; // type Employee
	private Date acceptedTime;
	private Date finishTime;
	
	public TaskAssignment(int id, int taskId, int employeeId, Date acceptedTime, Date finishTime) {
		this.id = id;
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.acceptedTime = acceptedTime;
		this.finishTime = finishTime;
	}

	public Date getAcceptedTime() {
		return acceptedTime;
	}
	public void setAcceptedTime(Date acceptedTime) {
		this.acceptedTime = acceptedTime;
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
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	
	
}
