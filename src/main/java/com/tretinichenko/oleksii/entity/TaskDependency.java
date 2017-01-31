package com.tretinichenko.oleksii.entity;

public class TaskDependency {

	private int id;
	private int taskId;  // or type Task?
	private int dependencyTaskId;
	

	public TaskDependency(int id, int taskId, int dependencyTaskId) {
		this.id = id;
		this.taskId = taskId;
		this.dependencyTaskId = dependencyTaskId;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public int getDependencyTaskId() {
		return dependencyTaskId;
	}
	
	public void setDependencyTaskId(int dependcyTaskId) {
		this.dependencyTaskId = dependcyTaskId;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dependencyTaskId;
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
		TaskDependency other = (TaskDependency) obj;
		if (dependencyTaskId != other.dependencyTaskId)
			return false;
		if (id != other.id)
			return false;
		if (taskId != other.taskId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskDependency [id=" + id + ", taskId=" + taskId + ", dependencyTaskId=" + dependencyTaskId + "]";
	}
	
	
	
}
