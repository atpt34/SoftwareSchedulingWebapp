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
	
	
}
