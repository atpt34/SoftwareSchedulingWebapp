package com.tretinichenko.oleksii.model;

public class EmployeeOvertimeEstimated {

	private int weekOfYear;
	private int employeeId;
	private int workingHours;
	
	
	
	public EmployeeOvertimeEstimated(int weekOfYear, int employeeId, int workingHours) {
		super();
		this.weekOfYear = weekOfYear;
		this.employeeId = employeeId;
		this.workingHours = workingHours;
	}
	
	public int getWeekOfYear() {
		return weekOfYear;
	}
	public void setWeekOfYear(int weekOfYear) {
		this.weekOfYear = weekOfYear;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getWorkingHours() {
		return workingHours;
	}
	public void setWorkingHours(int workingHours) {
		this.workingHours = workingHours;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + employeeId;
		result = prime * result + weekOfYear;
		result = prime * result + workingHours;
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
		EmployeeOvertimeEstimated other = (EmployeeOvertimeEstimated) obj;
		if (employeeId != other.employeeId)
			return false;
		if (weekOfYear != other.weekOfYear)
			return false;
		if (workingHours != other.workingHours)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmployeeOvertimeEstimated [weekOfYear=" + weekOfYear + ", employeeId=" + employeeId + ", workingHours="
				+ workingHours + "]";
	}
	
	
	
}
