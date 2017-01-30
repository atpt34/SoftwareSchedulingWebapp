package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.TaskAssignment;

public interface TaskAssignmentDAO {

	void saveTaskAssignment(TaskAssignment taskAssign);
	
	void updateTaskAssignment(TaskAssignment taskAssign);
	
	void deleteTaskAssignmentById(int taskAssignId);
	
	TaskAssignment findTaskAssignmentById(int taskAssignId);
	
	List<TaskAssignment> listAllTaskAssignments();
	
//	List<TaskAssignment> listTaskAssignmentsByTaskId();
	
//	List<TaskAssignment> listTaskAssignmentsByEmployeeId();
}
