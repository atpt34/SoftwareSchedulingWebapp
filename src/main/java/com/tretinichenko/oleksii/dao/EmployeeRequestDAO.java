package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.EmployeeRequest;

public interface EmployeeRequestDAO {

	EmployeeRequest findEmployeeRequestById(int requestId);
	
	void saveEmployeeRequest(EmployeeRequest employeeRequest);
	
	void deleteEmployeeRequestById(int requestId);
	
	void updateEmployeeRequest(EmployeeRequest employeeRequest);
	
	List<EmployeeRequest> listAllEmployeeRequests();
}
