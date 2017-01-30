package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.Task;

public interface TaskDAO {
	
	Task findTaskById(int taskId);

	void saveTask(Task task);
	
	void updateTask(Task task);
	
	void deleteTaskById(int taskId);
	
	List<Task> listAllTasks();
	
//	List<Task> listTasksBySprintId(int sprintId);
	
}
