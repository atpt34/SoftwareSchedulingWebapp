package com.tretinichenko.oleksii.entity;

import java.util.Date;

public class Task {

	private int id;
	private int sprintId; // or type Sprint
	private String name;
	private Date startTime;
	private int hoursEstimate;  // estimate time
	
	public Task(int id, int sprintId, String name, 
			Date startTime, int hoursEstimate) {
		this.id = id;
		this.sprintId = sprintId;
		this.name = name;
		this.startTime = startTime;
		this.hoursEstimate = hoursEstimate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSprintId() {
		return sprintId;
	}

	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getHoursEstimate() {
		return hoursEstimate;
	}

	public void setHoursEstimate(int hoursEstimate) {
		this.hoursEstimate = hoursEstimate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hoursEstimate;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + sprintId;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		Task other = (Task) obj;
		if (hoursEstimate != other.hoursEstimate)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sprintId != other.sprintId)
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", sprintId=" + sprintId + ", name=" + name + ", startTime=" + startTime
				+ ", hoursEstimate=" + hoursEstimate + "]";
	}
	
	
	
}
