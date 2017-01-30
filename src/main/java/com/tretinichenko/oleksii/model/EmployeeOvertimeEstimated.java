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
	
	
}
