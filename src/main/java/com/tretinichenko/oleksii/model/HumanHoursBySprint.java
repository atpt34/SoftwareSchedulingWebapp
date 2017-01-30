package com.tretinichenko.oleksii.model;

public class HumanHoursBySprint {
	
	private int sprintId;
	private int totalHumanHours;
	
	
	
	public HumanHoursBySprint(int sprintId, int totalHumanHours) {
		this.sprintId = sprintId;
		this.totalHumanHours = totalHumanHours;
	}
	
	public int getSprintId() {
		return sprintId;
	}
	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}
	public int getTotalHumanHours() {
		return totalHumanHours;
	}
	public void setTotalHumanHours(int totalHumanHours) {
		this.totalHumanHours = totalHumanHours;
	}

	
}
