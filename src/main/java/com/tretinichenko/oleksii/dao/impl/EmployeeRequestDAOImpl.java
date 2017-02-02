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

import com.tretinichenko.oleksii.dao.EmployeeRequestDAO;
import com.tretinichenko.oleksii.entity.EmployeeRequest;


@Service
@Transactional
public class EmployeeRequestDAOImpl extends JdbcDaoSupport 
		implements EmployeeRequestDAO {
	
	private static final String SELECT_ALL_EMPLOYEEREQUESTS = 
			"SELECT * FROM EmployeeRequest";
	
	private static final String SELECT_EMPLOYEEREQUEST_BY_ID = 
			"SELECT * FROM EmployeeRequest WHERE id = ?";
	
	private static final String DELETE_EMPLOYEEREQUEST_BY_ID = 
			"DELETE FROM EmployeeRequest WHERE id = ?";
	
	private static final String INSERT_EMPLOYEEREQUEST = 
			"INSERT INTO EmployeeRequest "
			+ "(id, taskId, employeeId, hours) "
			+ "VALUES ( ?, ?, ?, ? )";
	
	private static final String UPDATE_EMPLOYEEREQUEST = 
			"UPDATE EmployeeRequest SET "
			+ "taskId = ?, employeeId = ?, hours = ? WHERE id = ?";
	
	private static final String SELECT_ALL_EMPLOYEEREQUESTS_BY_EMPLOYEEID = 
			"SELECT * FROM EmployeeRequest WHERE employeeId = ?";
	
	private static final String DELETE_ALL_EMPLOYEEREQUESTS_BY_EMPLOYEEID = 
			"DELETE FROM EmployeeRequest WHERE employeeId = ?";
	
	private static final String SELECT_ALL_EMPLOYEEREQUESTS_BY_MANAGERID =
			"SELECT * FROM EmployeeRequest "
			+ "WHERE employeeId IN ("
			+ " SELECT id"
			+ " FROM Employee"
			+ " WHERE managerId = ? )";
	
	private static final String SELECT_ALL_EMPLOYEEREQUESTS_BY_MANAGERID2 = 
			"SELECT * FROM EmployeeRequest AS er "
			+ "INNER JOIN Employee AS e "
			+ " ON e.id = er.employeeId"
			+ "WHERE e.managerId = ? ";
	
	private static final String DELETE_ALL_EMPLOYEEREQUESTS_BY_TASKID =
			"DELETE FROM EmployeeRequest WHERE taskId = ?";
	
	private static final String DELETE_ALL_EMPLOYEEREQUESTS_BY_MANAGERID = 
			"DELETE FROM EmployeeRequest "
			+ "WHERE employeeId IN ("
			+ " SELECT id "
			+ " FROM Employee"
			+ " WHERE managerId = ? )";
	
	@Autowired
	public EmployeeRequestDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Override
	public EmployeeRequest findEmployeeRequestById(int requestId) {
		try {
			return this.getJdbcTemplate()
					.queryForObject(SELECT_EMPLOYEEREQUEST_BY_ID,
					new EmployeeRequestRowMapper(), requestId);
		} catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public void saveEmployeeRequest(EmployeeRequest request) {
		this.getJdbcTemplate().update(INSERT_EMPLOYEEREQUEST, 
				request.getId(), request.getTaskId(),
				request.getEmployeeId(), request.getHours());
	}

	@Override
	public void deleteEmployeeRequestById(int requestId) {
		this.getJdbcTemplate().update(DELETE_EMPLOYEEREQUEST_BY_ID, requestId);
	}

	@Override
	public void updateEmployeeRequest(EmployeeRequest request) {
		this.getJdbcTemplate().update(UPDATE_EMPLOYEEREQUEST,
				request.getTaskId(), request.getEmployeeId(),
				request.getHours(), request.getId());
	}

	@Override
	public List<EmployeeRequest> listAllEmployeeRequests() {
		return this.getJdbcTemplate().query(SELECT_ALL_EMPLOYEEREQUESTS,
				new EmployeeRequestRowMapper());
	}

	private static final class EmployeeRequestRowMapper 
			implements RowMapper<EmployeeRequest> {
		public EmployeeRequest mapRow(ResultSet rs, int rowNum) 
				throws SQLException {
			int id = rs.getInt("id");
			int taskId = rs.getInt("taskId");
			int employeeId = rs.getInt("employeeId");
			int hours = rs.getInt("hours");
			return new EmployeeRequest(id, taskId, employeeId, hours);
		}
	}
}
