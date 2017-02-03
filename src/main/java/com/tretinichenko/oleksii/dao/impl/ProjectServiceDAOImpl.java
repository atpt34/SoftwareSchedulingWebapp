package com.tretinichenko.oleksii.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tretinichenko.oleksii.dao.ProjectServiceDAO;
import com.tretinichenko.oleksii.model.EmployeeOvertimeActual;
import com.tretinichenko.oleksii.model.EmployeeOvertimeEstimated;
import com.tretinichenko.oleksii.model.HumanHoursBySprint;

@Service
@Transactional
public class ProjectServiceDAOImpl extends JdbcDaoSupport implements ProjectServiceDAO {

	@Autowired
	public ProjectServiceDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	
	private static final String EMPLOYEE_OVERTIME_ESTIMATE_BY_PROJECTID = 
			"SELECT \n" + 
			"\"weekOfYear\",\n" + 
			"\"employeeId\",\n" + 
			"\"workingHours\"\n" + 
			"FROM EmployeeOvertimeEstimateFunction(?) \n" + 
			"ORDER BY \"weekOfYear\" ASC, \"employeeId\" ASC"; 	
	
	private static final class EmployeeOvertimeEstimatedRowMapper 
			implements RowMapper<EmployeeOvertimeEstimated> {
		public EmployeeOvertimeEstimated mapRow(ResultSet rs, int rowNum) 
				throws SQLException {
			int weekOfYear = rs.getInt("weekOfYear");
			int employeeId = rs.getInt("employeeId");
			int workingHours = rs.getInt("workingHours");
			return new EmployeeOvertimeEstimated(weekOfYear, employeeId, workingHours);
		}
	}
	
	@Override
	public List<EmployeeOvertimeEstimated>
			employeeOvertimeEstimated(int projectId) {
		return this.getJdbcTemplate().query(
				EMPLOYEE_OVERTIME_ESTIMATE_BY_PROJECTID,
				new EmployeeOvertimeEstimatedRowMapper(), projectId);
	}

	private static final String HUMAN_HOURS_SPRINT_BY_PROJECTID = 
			"SELECT \n" + 
			"\"sprintId\", \n" + 
			"\"totalHumanHours\" \n" + 
			"FROM SprintHumanHoursFunction(?)"
			+ "ORDER BY \"sprintId\"";
	
	@Override
	public List<HumanHoursBySprint> humanHoursBySprint(int projectId) {
		return this.getJdbcTemplate().query(
				HUMAN_HOURS_SPRINT_BY_PROJECTID,
				new RowMapper<HumanHoursBySprint>() {
					@Override
					public HumanHoursBySprint mapRow(ResultSet rs, int rowNum) 
							throws SQLException {
						int sprintId = rs.getInt("sprintId");
						int totalHumanHours = rs.getInt("totalHumanHours");
						return new HumanHoursBySprint(sprintId, totalHumanHours);
					}
				}, 
				projectId);
	}

	private static final int OVERTIME_HOURS_THRESHOLD = 40;
	private static final String EMPLOYEE_OVERTIME_ACTUAL_BY_PROJECTID = 
			"SELECT  \n" + 
			"\"weekOfYear\",  \n" + 
			"\"employeeId\",  \n" + 
			"\"overtimeHours\"  \n" + 
			"FROM employeeOvertimeActualFunction(?, "
			+ OVERTIME_HOURS_THRESHOLD + ")";
	
	@Override
	public List<EmployeeOvertimeActual> employeeOvertimeActual(int projectId) {
		return this.getJdbcTemplate().query(
				EMPLOYEE_OVERTIME_ACTUAL_BY_PROJECTID,
				new RowMapper<EmployeeOvertimeActual>() {
					@Override
					public EmployeeOvertimeActual mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						int employeeId = rs.getInt("employeeId");
						int weekOfYear = rs.getInt("weekOfYear");
						int overtimeHours = rs.getInt("overtimeHours");
						return new EmployeeOvertimeActual(weekOfYear, 
								employeeId, overtimeHours);
					}
				}, 
				projectId);
	}

	private static final String SPRINT_SEQUENCE_ESTIMATE = 
			"SELECT *\n" + 
			"FROM sprintSequenceEstimateFunction(?)\n" + 
			"ORDER BY \"sprintId\"";
	
	private static final String TASKDEPENDENCY_SEQUENCE_ESTIMATE = 
			"SELECT * \n" + 
			"FROM taskDependencySequenceFunction(?)\n" + 
			"ORDER BY \"taskId\"";

	private static final String SPRINT_ON_TIME_ESTIMATE = 
			"SELECT * \n" + 
			"FROM sprintOnTimeEstimateFunction(?)\n" + 
			"ORDER BY \"sprintId\"";
	
	private static final String PROJECT_ON_TIME_ESTIMATE = 
			"SELECT * \n" + 
			"FROM projectOnTimeEstimateFunction(?)";
	
	private static final String TOTAL_EMPLOYEE_OVERTIME_ESTIMATE = 
			"SELECT *\n" + 
			"FROM totalEmployeeOvertimeFunction(?, " + OVERTIME_HOURS_THRESHOLD + ")\n" + 
			"ORDER BY \"employeeId\"";
	
	private static final String TOTAL_SPRINT_OVERTIME_ESTIMATE = 
			"SELECT * \n" + 
			"FROM totalSprintOvertimeFunction(?, " + OVERTIME_HOURS_THRESHOLD + ")\n" + 
			"ORDER BY \"sprintId\"";
	
	private static final String TOTAL_PROJECT_OVERTIME_ESTIMATE = 
			"SELECT \"numOfOvertimes\"\n" + 
			"FROM totalProjectOvertimeFunction(?, " + OVERTIME_HOURS_THRESHOLD + ")";
	
	
	private static final String TOTAL_TASK_DONE_ON_TIME = 
			"";
	
	private static final String TOTAL_TASK_DONE_WITH_DILATION = 
			"";
	
	private static final String TOTAL_SPRINT_DONE_ON_TIME =
			"";
	
	private static final String TOTAL_SPRINT_DONE_WITH_DILATION =
			"";
	
	private static final String TASK_ACTUAL_DURATION = 
			"";
	
	private static final String SPRINT_ACTUAL_DURATION =
			"";
	
	private static final String PROJECT_ACTUAL_DURATION = 
			""; 
	
	private static final String PROJECT_HUMAN_HOURS_BY_PROJECTID =
			"";
	
	private static final String TASK_ESTIMATE_ACTUAL_DEVIATION = 
			"";
	
	private static final String SPRINT_ESTIMATE_ACTUAL_DEVIATION =
			"";
	
	
	private static final String EMPLOYEES_ACTIVITY_AT_GIVEN_TIME =
			"";
	
	private static final String EMPLOYEE_TASK_DEVIATION =
			"";
	
}
