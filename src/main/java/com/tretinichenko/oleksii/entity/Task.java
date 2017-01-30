package com.tretinichenko.oleksii.entity;

import java.util.Date;

public class Task {

	private int id;
	private int sprintId; // or type Sprint
	private String name;
	private Date startTime;
	private int hoursEstimate;  // estimate time
	
	public Task(int id, int sprintId, String name, Date startTime, int hoursEstimate) {
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
	
	
	
}
