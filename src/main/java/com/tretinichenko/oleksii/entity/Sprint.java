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

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dependsOn;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + projectId;
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
		Sprint other = (Sprint) obj;
		if (dependsOn != other.dependsOn)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (projectId != other.projectId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sprint [id=" + id + ", projectId=" + projectId + ", name=" + name + ", dependsOn=" + dependsOn + "]";
	}
	
	
	
}
