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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sprintId;
		result = prime * result + totalHumanHours;
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
		HumanHoursBySprint other = (HumanHoursBySprint) obj;
		if (sprintId != other.sprintId)
			return false;
		if (totalHumanHours != other.totalHumanHours)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HumanHoursBySprint [sprintId=" + sprintId + ", totalHumanHours=" + totalHumanHours + "]";
	}

	
	
}
