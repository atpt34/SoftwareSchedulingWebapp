package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.Project;

public interface ProjectDAO {

	// CRUD Project
	
	Project findProjectById(int projectId);
	
	void saveProject(Project project);
	
	void updateProject(Project project);
	
	void deleteProjectById(int projectId);
	
	List<Project> listAllProjects(); 
	
//	List<Project> listProjectByManagerId(int managerId);
	
}
