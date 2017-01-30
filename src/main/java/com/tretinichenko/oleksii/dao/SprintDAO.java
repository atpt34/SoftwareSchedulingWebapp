package com.tretinichenko.oleksii.dao;

import java.util.List;

import com.tretinichenko.oleksii.entity.Sprint;

public interface SprintDAO {
	
	Sprint findSprintById(int sprintId);

	void saveSprint(Sprint sprint);
	
	void updateSprint(Sprint sprint);
	
	void deleteSprintById(int sprintId);
	
	List<Sprint> listAllSprints();
	
//	List<Sprint> listSprintsByProjectId(int projectId);
	
}
