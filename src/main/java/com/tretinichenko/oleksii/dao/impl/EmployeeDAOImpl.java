package com.tretinichenko.oleksii.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tretinichenko.oleksii.dao.EmployeeDAO;
import com.tretinichenko.oleksii.entity.Employee;

@Service
@Transactional
public class EmployeeDAOImpl extends JdbcDaoSupport implements EmployeeDAO {

	private static final String SELECT_ALL_EMPLOYEES = 
			"SELECT \n "
			+ "id, name, email, managerId \n"
			+ "FROM Employee";
	
	private static final String SELECT_EMPLOYEE_BY_ID = 
			"SELECT * FROM Employee WHERE id = ?";
	
	private static final String INSERT_EMPLOYEE = 
			"INSERT INTO Employee(id, name, email, managerId) "
			+ "VALUES(?, ?, ?, ?)";
	
	private static final String UPDATE_EMPLOYEE_BY_ID = 
			"UPDATE Employee "
			+ "SET name = ?, email = ?, managerId = ? WHERE id = ?";
	
	private static final String DELETE_EMPLOYEE_BY_ID = 
			"DELETE FROM Employee WHERE id = ?";
	
	private static final String SELECT_ALL_EMPLOYEES_BY_MANAGER_ID = 
			"SELECT id, name email, managerId "
			+ "FROM Employee WHERE managerId = ?";
	
	private static final String SELECT_ALL_EMPLOYEES_WHERE_MANAGER_ID_IS_NULL =
			"SELECT * FROM Employee WHERE managerId IS NULL";
	
	private static final String SELECT_ALL_EMPLOYEES_WHERE_NAME_LIKE =
			"SELECT * FROM Employee WHERE name LIKE '%?%'";
	
	private static final String DELETE_ALL_EMPLOYEES_BY_MANAGER_ID = 
			"DELETE FROM Employee WHERE managerId = ?";
	
	@Autowired
	public EmployeeDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	@Override
	public Employee findEmployeeById(int employeeId) {
//		String sql = "SELECT * FROM Employee WHERE id = ?";
		try {
//			return this.getJdbcTemplate().queryForObject(sql, new EmployeeRowMapper(), employeeId);
			return this.getJdbcTemplate().queryForObject(SELECT_EMPLOYEE_BY_ID,
					new EmployeeRowMapper(), employeeId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void saveEmployee(Employee employee) {
//		String sql = "INSERT INTO Employee(id, name, email, managerId) VALUES(?, ?, ?, ?)";
		int managerId = employee.getManagerId();
//		this.getJdbcTemplate().update(sql, employee.getId(), employee.getName(),
//				employee.getEmail(), managerId == 0 ? null : managerId);
		this.getJdbcTemplate().update(INSERT_EMPLOYEE,
				employee.getId(), employee.getName(),
				employee.getEmail(), managerId == 0 ? null : managerId);
		/*SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(this.getJdbcTemplate()).withTableName("Employee");
		jdbcInsert.setGeneratedKeyName("id");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("managerId", employee.getManagerId());
		args.put("name", employee.getName());
		args.put("email", employee.getEmail());
		long spittleId = jdbcInsert.executeAndReturnKey(args).longValue();
		return spittleId;*/
		
	}
	
	@Override
	public void deleteEmployeeById(int employeeId) {
//			this.getJdbcTemplate().update("DELETE FROM Employee WHERE id = ?", employeeId);
			this.getJdbcTemplate().update(DELETE_EMPLOYEE_BY_ID, employeeId);		
	}

	@Override
	public void updateEmployee(Employee employee) {
		int managerId = employee.getManagerId();
//		this.getJdbcTemplate().update("UPDATE Employee SET name = ?, email = ?, managerId = ? WHERE id = ?", 
//				employee.getName(), employee.getEmail(), employee.getManagerId(), employee.getId());
		this.getJdbcTemplate().update(UPDATE_EMPLOYEE_BY_ID, 
				employee.getName(),	employee.getEmail(), 
				managerId == 0 ? null : managerId, employee.getId());
	}
	
	@Override
	public List<Employee> listAllEmployees() {
//		String sql = "SELECT * FROM Employee";
//		return this.getJdbcTemplate().query(sql, new EmployeeRowMapper());
		return this.getJdbcTemplate().query(SELECT_ALL_EMPLOYEES,
				new EmployeeRowMapper());
	}

	private static final class EmployeeRowMapper 
			implements RowMapper<Employee> {
		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
//			String message = rs.getString("message");
//			Date postedTime = rs.getTimestamp("postedTime");
			String name = rs.getString("name");
			String email = rs.getString("email");
//			boolean updateByEmail = rs.getBoolean("updateByEmail");
			int managerId = rs.getInt("managerId");

			return new Employee(id, name, email, managerId);
		}
	}

	
}
