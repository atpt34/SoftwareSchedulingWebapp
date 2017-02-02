package com.tretinichenko.oleksii.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tretinichenko.oleksii.dao.TaskDependencyDAO;
import com.tretinichenko.oleksii.entity.TaskDependency;

@Service
@Transactional
public class TaskDependencyDAOImpl extends JdbcDaoSupport 
		implements TaskDependencyDAO {
	
	private static final String SELECT_ALL_TASKDEPENDENCIES = 
			"SELECT * FROM TaskDependency";
	
	private static final String SELECT_TASKDEPENDENCY_BY_ID = 
			"SELECT * FROM TaskDependency WHERE id = ?";
	
	private static final String DELETE_TASKDEPENDENCY_BY_ID = 
			"DELETE FROM TaskDependency WHERE id = ?";
	
	private static final String UPDATE_TASKDEPENDENCY = 
			"UPDATE TaskDependency "
			+ "SET taskId = ?, dependencyTaskId = ? "
			+ "WHERE id = ?";
	
	private static final String INSERT_TASKDEPENDENCY = 
			"INSERT INTO TaskDependency "
			+ "(id, taskId, dependencyTaskId) "
			+ "VALUES (?, ?, ?)";
	
	private static final String SELECT_ALL_TASKDEPENDENCIES_BY_TASKID = 
			"SELECT * FROM TaskDependency WHERE taskId = ?";
	
	private static final String DELETE_ALL_TASKDEPENDENCIES_BY_TASKID = 
			"DELETE FROM TaskDependency WHERE taskId = ?";
	
	private static final String 
		SELECT_ALL_TASKDEPENDENCIES_BY_DEPENDENCYTASKID = 
		"SELECT * FROM TaskDependency WHERE dependencyTaskId = ?";
	private static final String 
		DELETE_ALL_TASKDEPENDENCIES_BY_DEPENDENCYTASKID = 
		"DELETE FROM TaskDependency WHERE dependencyTaskId = ?";
	
	@Autowired
	public TaskDependencyDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Override
	public TaskDependency findTaskDependencyById(int taskDepId) {
		try {
			return this.getJdbcTemplate()
					.queryForObject(SELECT_TASKDEPENDENCY_BY_ID, 
							new TaskDependencyRowMapper(), taskDepId);
		} catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public void saveTaskDependency(TaskDependency taskDep) {
		this.getJdbcTemplate().update(INSERT_TASKDEPENDENCY, taskDep.getId(), 
				taskDep.getTaskId(), taskDep.getDependencyTaskId());
	}

	@Override
	public void updateTaskDependency(TaskDependency taskDep) {
		this.getJdbcTemplate().update(UPDATE_TASKDEPENDENCY,  
				taskDep.getTaskId(), taskDep.getDependencyTaskId(), 
				taskDep.getId());
	}

	@Override
	public void deleteTaskDependencyById(int taskDepId) {
		this.getJdbcTemplate().update(DELETE_TASKDEPENDENCY_BY_ID, taskDepId);
	}

	@Override
	public List<TaskDependency> listAllTaskDependecies() {
		return this.getJdbcTemplate().query(SELECT_ALL_TASKDEPENDENCIES, 
				new TaskDependencyRowMapper());
	}

	private static final class TaskDependencyRowMapper 
			implements RowMapper<TaskDependency> {
		public TaskDependency mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			int id = rs.getInt("id");
			int taskId = rs.getInt("taskId");
			int dependencyTaskId = rs.getInt("dependencyTaskId");
			return new TaskDependency(id, taskId, dependencyTaskId);
		}
	}
}
