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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + employeeId;
		result = prime * result + overtimeHours;
		result = prime * result + weekOfYear;
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
		EmployeeOvertimeActual other = (EmployeeOvertimeActual) obj;
		if (employeeId != other.employeeId)
			return false;
		if (overtimeHours != other.overtimeHours)
			return false;
		if (weekOfYear != other.weekOfYear)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmployeeOvertimeActual [weekOfYear=" + weekOfYear + ", employeeId=" + employeeId + ", overtimeHours="
				+ overtimeHours + "]";
	}
	
	
	
}
