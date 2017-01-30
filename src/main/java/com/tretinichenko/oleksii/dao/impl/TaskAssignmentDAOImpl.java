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

import com.tretinichenko.oleksii.dao.TaskAssignmentDAO;

import com.tretinichenko.oleksii.entity.TaskAssignment;

@Service
@Transactional
public class TaskAssignmentDAOImpl extends JdbcDaoSupport implements TaskAssignmentDAO {
	
	private static final String SELECT_ALL_TASKASSIGNMENTS = 
			"SELECT * FROM TaskAssignment";
	
	private static final String SELECT_TASKASSIGNMENT_BY_ID = 
			"SELECT * FROM TaskAssignment WHERE id = ?";
	
	private static final String INSERT_TASKASSIGNMENT = 
			"INSERT INTO TaskAssignment "
			+ "(id, taskId, employeeId, acceptedTime, finishTime) "
			+ "VALUES (?, ?, ?, ?, ?)";
	
	private static final String UPDATE_TASKASSIGNMENT_BY_ID = 
			"UPDATE TaskAssignment SET "
			+ "taskId = ?, employeeId = ?, acceptedTime = ?, finishTime = ? "
			+ "WHERE id = ?";
	
	private static final String DELETE_TASKASSIGNMENT_BY_ID = 
			"DELETE FROM TaskAssignment WHERE id = ?";
	
	private static final String SELECT_ALL_TASKASSIGNMENTS_BY_EMPLOYEE_ID = 
			"SELECT * FROM TaskAssignment WHERE employeeId = ?";
	
	private static final String SELECT_ALL_TASKASSIGNMENTS_BY_TASK_ID = 
			"SELECT * FROM TaskAssignment WHERE taskId = ?";
	
	private static final String SELECT_ALL_TASKASSIGNMENTS_BY_MANAGER_ID =
			"SELECT * \n" + 
			"FROM TaskAssignment \n" + 
			"WHERE employeeId IN ( \n" + 
			"    SELECT id \n" + 
			"    FROM Employee \n" + 
			"    WHERE managerId = ? )";

	@Autowired
	public TaskAssignmentDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	@Override
	public void saveTaskAssignment(TaskAssignment taskAssign) {
		this.getJdbcTemplate().update(INSERT_TASKASSIGNMENT, taskAssign.getId(), 
				taskAssign.getTaskId(),	taskAssign.getEmployeeId(), 
				taskAssign.getAcceptedTime(), taskAssign.getFinishTime());
	}

	@Override
	public void updateTaskAssignment(TaskAssignment taskAssign) {
		this.getJdbcTemplate().update(UPDATE_TASKASSIGNMENT_BY_ID, taskAssign.getTaskId(), 
				taskAssign.getEmployeeId(), taskAssign.getAcceptedTime(), 
				taskAssign.getFinishTime(), taskAssign.getId());
	}

	@Override
	public void deleteTaskAssignmentById(int taskAssignId) {
		this.getJdbcTemplate().update(DELETE_TASKASSIGNMENT_BY_ID, taskAssignId);
	}

	@Override
	public List<TaskAssignment> listAllTaskAssignments() {
		return this.getJdbcTemplate().query(SELECT_ALL_TASKASSIGNMENTS, 
				new TaskAssignmentRowMapper());
	}

	@Override
	public TaskAssignment findTaskAssignmentById(int id) {
		try {
			return this.getJdbcTemplate().queryForObject(SELECT_TASKASSIGNMENT_BY_ID,
					new TaskAssignmentRowMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private static final class TaskAssignmentRowMapper implements RowMapper<TaskAssignment> {
		public TaskAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int taskId = rs.getInt("taskId");
			int employeeId = rs.getInt("employeeId");
			Date acceptedTime = rs.getTimestamp("acceptedTime");
			Date finishTime = rs.getTimestamp("finishTime");
			return new TaskAssignment(id, taskId, employeeId, acceptedTime, finishTime);
		}
	}
}
