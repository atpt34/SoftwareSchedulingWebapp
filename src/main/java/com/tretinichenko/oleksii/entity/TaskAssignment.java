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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acceptedTime == null) ? 0 : acceptedTime.hashCode());
		result = prime * result + employeeId;
		result = prime * result + ((finishTime == null) ? 0 : finishTime.hashCode());
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
		TaskAssignment other = (TaskAssignment) obj;
		if (acceptedTime == null) {
			if (other.acceptedTime != null)
				return false;
		} else if (!acceptedTime.equals(other.acceptedTime))
			return false;
		if (employeeId != other.employeeId)
			return false;
		if (finishTime == null) {
			if (other.finishTime != null)
				return false;
		} else if (!finishTime.equals(other.finishTime))
			return false;
		if (id != other.id)
			return false;
		if (taskId != other.taskId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskAssignment [id=" + id + ", taskId=" + taskId + ", employeeId=" + employeeId + ", acceptedTime="
				+ acceptedTime + ", finishTime=" + finishTime + "]";
	}
	
	
}
