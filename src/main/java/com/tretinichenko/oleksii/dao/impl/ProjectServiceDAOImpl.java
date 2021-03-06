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
			"SELECT "
			+ "\"sprintId\",\n" + 
			"\"dependencySprint\",\n" + 
			"\"sprintStartTime\",\n" + 
			"\"estimateDependencyFinishTime\" \n" + 
			"FROM sprintSequenceEstimateFunction(?)\n" + 
			"ORDER BY \"sprintId\"";
	
	private static final String TASKDEPENDENCY_SEQUENCE_ESTIMATE = 
			"SELECT "
			+ "\"taskId\",\n" + 
			"\"dependencyTaskId\",\n" + 
			"\"taskStartTime\",\n" + 
			"\"dependencyTaskMaxFinishTime\" \n" + 
			"FROM taskDependencySequenceFunction(?)\n" + 
			"ORDER BY \"taskId\"";

	private static final String SPRINT_ON_TIME_ESTIMATE = 
			"SELECT "
			+ "\"sprintId\",\n" + 
			"\"sprintStartTime\",\n" + 
			"\"sprintEstFinishTime\" \n" + 
			"FROM sprintOnTimeEstimateFunction(?)\n" + 
			"ORDER BY \"sprintId\"";
	
	private static final String PROJECT_ON_TIME_ESTIMATE = 
			"SELECT "
			+ "\"projectId\",\n" + 
			"\"projectName\",\n" + 
			"\"projectStart\",\n" + 
			"\"projectEnd\",\n" + 
			"\"projectTaskStartTime\",\n" + 
			"\"projectTaskEstFinishTime\" \n" + 
			"FROM projectOnTimeEstimateFunction(?)";
	
	private static final String TOTAL_EMPLOYEE_OVERTIME_ESTIMATE = 
			"SELECT "
			+ "\"employeeId\",\n" + 
			"\"numOfOvertimes\" \n" + 
			"FROM totalEmployeeOvertimeFunction(?, " + OVERTIME_HOURS_THRESHOLD + ")\n" + 
			"ORDER BY \"employeeId\"";
	
	private static final String TOTAL_SPRINT_OVERTIME_ESTIMATE = 
			"SELECT \n" + 
			"\"sprintId\", \n" + 
			"\"numOfOvertimes\" \n" + 
			"FROM totalSprintOvertimeFunction(?, " + OVERTIME_HOURS_THRESHOLD + ")\n" + 
			"ORDER BY \"sprintId\"";
	
	private static final String TOTAL_PROJECT_OVERTIME_ESTIMATE = 
			"SELECT \"numOfOvertimes\"\n" + 
			"FROM totalProjectOvertimeFunction(?, " + OVERTIME_HOURS_THRESHOLD + ")";
	
	
	
	private static final String TOTAL_TASK_DONE_ON_TIME = 
			"SELECT \n" + 
			"\"numOfTasksDoneOnTime\"\n" + 
			"FROM numOfTaskDoneOnTimeFunction(?)";
	
	private static final String TOTAL_TASK_DONE_WITH_DILATION = 
			"SELECT \n" + 
			"\"numOfTaskDoneWithDilation\"\n" + 
			"FROM numOfTaskDoneWithDilationFunction(?)";
	
	private static final String TOTAL_SPRINT_DONE_ON_TIME =
			"SELECT \n" + 
			"\"numOfSprintsDoneOnTime\"\n" + 
			"FROM numOfSprintDoneOnTimeFunction(?);";
	
	private static final String TOTAL_SPRINT_DONE_WITH_DILATION =
			"SELECT\n" + 
			"\"numOfSprintsDoneWithDilation\"\n" + 
			"FROM numOfSprintDoneWithDilationFunction(?)";
	
	private static final String TASK_ACTUAL_DURATION = 
			"SELECT \n" + 
			"\"taskId\",\n" + 
			"\"taskHoursDuration\"\n" + 
			"FROM taskActualDurationFunction(?)";
	
	private static final String SPRINT_ACTUAL_DURATION =
			"SELECT\n" + 
			"\"sprintId\",\n" + 
			"\"sprintHoursActualDuration\"\n" + 
			"FROM sprintActualDurationFunction(?)";
	
	private static final String PROJECT_ACTUAL_DURATION = 
			"SELECT\n" + 
			"\"projectId\",\n" + 
			"\"projectHoursActualDuration\"\n" + 
			"FROM projectActualDurationFunction(?);"; 
	
	private static final String PROJECT_HUMAN_HOURS_BY_PROJECTID =
			"SELECT \n" + 
			"\"projectId\",\n" + 
			"\"totalHumanHours\"\n" + 
			"FROM projectHumanHoursFunction(?);";
	
	private static final String TASK_ESTIMATE_ACTUAL_DEVIATION = 
			"SELECT \n" + 
			"\"taskId\",\n" + 
			"\"taskHoursDeviation\"\n" + 
			"FROM taskEstimateActualDeviationFunction(?)";
	
	private static final String SPRINT_ESTIMATE_ACTUAL_DEVIATION =
			"SELECT\n" + 
			"\"sprintId\",\n" + 
			"\"sprintHoursDeviation\"\n" + 
			"FROM sprintEstimateActualDeviationFunction(?);";
	
	
	private static final String EMPLOYEES_ACTIVITY_AT_GIVEN_TIME =
			"SELECT \n" + 
			"\"employeeId\",\n" + 
			"\"projectId\",\n" + 
			"\"projectName\",\n" + 
			"\"sprintId\",\n" + 
			"\"sprintName\",\n" + 
			"\"taskId\",\n" + 
			"\"taskName\",\n" + 
			"FROM employeeActivityAtGivenTimeFunction(?);";
	
	private static final String EMPLOYEE_TASK_DEVIATION =
			"SELECT \n" + 
			"\"employeeId\",\n" + 
			"\"taskId\",\n" + 
			"\"taskHoursDeviation\"\n" + 
			"FROM employeeTaskDeviationFunction(?);";
	
}
