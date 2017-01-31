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

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + employeeId;
		result = prime * result + hours;
		result = prime * result + id;
		result = prime * result + taskId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeRequest other = (EmployeeRequest) obj;
		if (employeeId != other.employeeId)
			return false;
		if (hours != other.hours)
			return false;
		if (id != other.id)
			return false;
		if (taskId != other.taskId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmployeeRequest [id=" + id + ", taskId=" + taskId + ", employeeId=" + employeeId + ", hours=" + hours
				+ "]";
	}
	
	
}
