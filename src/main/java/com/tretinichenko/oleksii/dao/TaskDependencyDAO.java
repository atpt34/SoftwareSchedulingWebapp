package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.TaskDependency;

public interface TaskDependencyDAO {

	TaskDependency findTaskDependencyById(int taskDepId);
	
	void saveTaskDependency(TaskDependency taskDep);
	
	void updateTaskDependency(TaskDependency taskDep);
	
	void deleteTaskDependencyById(int taskDepId);
	
	List<TaskDependency> listAllTaskDependecies();
	
//	List<TaskDependency> listTaskDependenciesByTaskId(int taskId);
	
}
