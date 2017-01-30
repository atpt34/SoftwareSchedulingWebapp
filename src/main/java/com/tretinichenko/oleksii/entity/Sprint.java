package com.tretinichenko.oleksii.entity;

public class Sprint {

	private int id;
	private int projectId;  // type Project ?
	private String name;
	private int dependsOn;  // single dependency, maybe SprintDependency ? id and type Sprint ?
	
	public Sprint(int id, int projectId, String name, int dependsOn) {
		this.id = id;
		this.projectId = projectId;
		this.name = name;
		this.dependsOn = dependsOn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(int dependsOn) {
		this.dependsOn = dependsOn;
	}
	
	
	
}
