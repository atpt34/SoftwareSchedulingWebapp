package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.Employee;

public interface EmployeeDAO {

	Employee findEmployeeById(int employeeId);
	
	void saveEmployee(Employee employee);
	
	void deleteEmployeeById(int employeeId);
	
	void updateEmployee(Employee employee);
	
	List<Employee> listAllEmployees();
	
//	List<Employee> listEmployeesByManagerId(int managerId);
	
}
