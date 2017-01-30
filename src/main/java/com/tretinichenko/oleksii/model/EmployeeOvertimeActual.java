package com.tretinichenko.oleksii.model;

public class EmployeeOvertimeActual {

	private int weekOfYear;
	private int employeeId;
	private int overtimeHours;
	
	public EmployeeOvertimeActual(int weekOfYear, int employeeId, int overtimeHours) {
		this.weekOfYear = weekOfYear;
		this.employeeId = employeeId;
		this.overtimeHours = overtimeHours;
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
	public int getOvertimeHours() {
		return overtimeHours;
	}
	public void setOvertimeHours(int overtimeHours) {
		this.overtimeHours = overtimeHours;
	}
	
	
}
