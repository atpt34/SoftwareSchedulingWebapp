package com.tretinichenko.oleksii.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tretinichenko.oleksii.dao.TaskDAO;
import com.tretinichenko.oleksii.entity.Task;

@Service
@Transactional
public class TaskDAOImpl extends JdbcDaoSupport implements TaskDAO {
	
	private static final String SELECT_ALL_TASKS = 
			"SELECT * FROM Task";
	
	private static final String SELECT_TASK_BY_ID = 
			"SELECT * FROM Task WHERE id = ?";
	
	private static final String DELETE_TASK_BY_ID = 
			"DELETE FROM Task WHERE id = ?";
	
	private static final String INSERT_TASK = 
			"INSERT INTO Task "
			+ "(id, sprintId, name, startTime, hoursEstimate) "
			+ "VALUES (?, ?, ?, ?, ?)";
	
	private static final String UPDATE_TASK = 
			"UPDATE Task SET "
			+ "sprintId = ?, name = ?, startTime = ?, hoursEstimate = ? "
			+ "WHERE id = ?";
	
	private static final String SELECT_ALL_TASKS_BY_SPRINT_ID =
			"SELECT * FROM Task WHERE sprintId = ?";
	
	@Autowired
	public TaskDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Override
	public Task findTaskById(int taskId) {
		try {
			return this.getJdbcTemplate().queryForObject(SELECT_TASK_BY_ID,
					new TaskRowMapper(), taskId);
		} catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public void saveTask(Task task) {
		this.getJdbcTemplate().update(INSERT_TASK, task.getId(), 
				task.getSprintId(), task.getName(), task.getStartTime(), 
				task.getHoursEstimate());
	}

	@Override
	public void updateTask(Task task) {
		this.getJdbcTemplate().update(UPDATE_TASK,  
				task.getSprintId(), task.getName(), task.getStartTime(),
				task.getHoursEstimate(), task.getId()); 		
	}

	@Override
	public void deleteTaskById(int taskId) {
		this.getJdbcTemplate().update(DELETE_TASK_BY_ID, taskId);
	}

	@Override
	public List<Task> listAllTasks() {
		return this.getJdbcTemplate().query(SELECT_ALL_TASKS, 
				new TaskRowMapper());
		
	}

	private static final class TaskRowMapper implements RowMapper<Task> {
		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int sprintId = rs.getInt("sprintId");
			String name = rs.getString("name");
			Date startTime = rs.getTimestamp("startTime");
			int hoursEstimate = rs.getInt("hoursEstimate");
			return new Task(id, sprintId, name, startTime, hoursEstimate);
		}
	}
}
