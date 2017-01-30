package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.model.EmployeeOvertimeActual;
import com.tretinichenko.oleksii.model.EmployeeOvertimeEstimated;
import com.tretinichenko.oleksii.model.HumanHoursBySprint;

public interface ProjectServiceDAO {

	
	
	// verifying estimation
	
	// verify sprint sequence estimated
	// task dependency execution correctness estimated
	// sprints will be done on time
	// project will be done on time
	// employees have no overtimes(work per week less than 40 hours) estimated
	List<EmployeeOvertimeEstimated> employeeOvertimeEstimated(int projectId);
	// overtimes quantity by employee
	// overtimes quantity by sprint
	// overtimes quantity by project
	
	
	
	// project, sprint, task statistics
	
	// how many tasks done on time
	// how many tasks done with dilation
	// how many sprints done on time
	// how many sprints done with dilation
	// tasks actual duration
	// sprint actual duration
	// project actual duration
	// how many human-hours spent by sprint
	List<HumanHoursBySprint> humanHoursBySprint(int projectId);
	// how many human-hours spent by project
	// deviation of task estimation
	// deviation of sprint estimation
	
	
	
	// employee statistics
	
	// projects, sprints, tasks on which a given employee worked in a given time
	// employee's overtimes
	List<EmployeeOvertimeActual> employeeOvertimeActual(int projectId);
	// deviation of task estimation by employee
	
}
