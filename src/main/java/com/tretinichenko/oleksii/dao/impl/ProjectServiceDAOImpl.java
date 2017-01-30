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
	
	
	private static final String EMPLOYEE_OVERTIMES_ESTIMATED_BY_PROJECTID = 
			"SELECT "
			+ "WEEKOFYEAR(temp.startTime) AS \"weekOfYear\", "
			+ "temp.employeeId AS \"employeeId\", "
			+ "SUM(temp.hoursActualTime) AS \"workingHours\" "
			+ "FROM ( "
			+ "	SELECT "
			+ " t.startTime AS \"startTime\", "
			+ " t.hoursEstimate, "
			+ "	TIMESTAMPDIFF(HOUR, t.startTime, "
			+ "   DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) "
			+ "    AS \"hoursActualTime\",	"
			+ " ta.employeeId AS \"employeeId\"	"
			+ " FROM Task AS t	"
			+ " INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id	"
			+ " INNER JOIN Sprint AS s ON t.sprintId = s.id	"
			+ " WHERE s.projectId = ? "
			+ " ) AS temp "
			+ "GROUP BY "
			+ "WEEKOFYEAR(temp.startTime), "
			+ "temp.employeeId "; 	
	
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
				EMPLOYEE_OVERTIMES_ESTIMATED_BY_PROJECTID,
				new EmployeeOvertimeEstimatedRowMapper(), projectId);
	}

	private static final String HUMAN_HOURS_SPRINT_BY_PROJECTID = 
			"SELECT "
			+ "temp.sprintId AS \"sprintId\", "
			+ "SUM(temp.hoursActualTime) AS \"totalHumanHours\" "
			+ "FROM ( "
			+ "	SELECT "
			+ " t.sprintId AS \"sprintId\", "
			+ " TIMESTAMPDIFF(HOUR, ta.acceptedTime, "
			+ "  ta.finishTime) AS \"hoursActualTime\" "
			+ "	FROM TaskAssignment AS ta	"
			+ " INNER JOIN Task AS t ON t.id = ta.taskId "
			+ " INNER JOIN Sprint AS s ON t.sprintId = s.id	"
			+ " WHERE s.projectId = ? "
			+ " ) AS temp "
			+ "GROUP BY "
			+ "temp.sprintId";
	
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
			"SELECT \n" + 
			"WEEKOFYEAR(temp.acceptedTime) AS \"weekOfYear\",\n" + 
			"temp.employeeId AS \"employeeId\", \n" + 
			"(SUM(temp.hoursActualTime) - " + OVERTIME_HOURS_THRESHOLD +
			"  ) AS \"overtimeHours\" \n" + 
			"FROM (\n" + 
			"	SELECT  \n" + 
			"	ta.acceptedTime AS \"acceptedTime\",\n" + 
			"	TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS \"hoursActualTime\",\n" + 
			"	ta.employeeId AS \"employeeId\"\n" + 
			"	FROM Task AS t\n" + 
			"    INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"	WHERE s.projectId = ?\n" + 
			") AS temp\n" + 
			"GROUP BY WEEKOFYEAR(temp.acceptedTime), temp.employeeId\n" + 
			"HAVING SUM(temp.hoursActualTime) > " + OVERTIME_HOURS_THRESHOLD;
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

	private static final String SPRINT_SEQUENCE_ESTIMATED = 
			"SELECT \n" + 
			"temp1.sprintId AS \"sprintId\", \n" + 
			"temp1.dependsOnSprint AS \"dependencySprint\", \n" + 
			"temp1.sprintStartTime AS \"sprintStartTime\", \n" + 
			"temp2.sprintFinishTime AS \"estimatedDependencyFinishTime\"\n" + 
			"FROM (\n" + 
			"	SELECT \n" + 
			"	s.id AS \"sprintId\",\n" + 
			"	s.dependsOn AS \"dependsOnSprint\",\n" + 
			"	MIN(t.startTime) AS \"sprintStartTime\"\n" + 
			"	FROM Sprint AS s\n" + 
			"	INNER JOIN Task AS t ON t.sprintId = s.id\n" + 
			"	WHERE s.projectId = ?\n" + 
			"	GROUP BY s.id\n" + 
			") AS temp1\n" + 
			"LEFT JOIN (\n" + 
			"	SELECT \n" + 
			"	s.id AS \"sid\",\n" + 
			"	MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))" + 
			" 		AS \"sprintFinishTime\"\n" + 
			"	FROM Sprint AS s\n" + 
			"	INNER JOIN Task AS t ON t.sprintId = s.id\n" + 
			"	WHERE s.projectId = ?\n" + 
			"	GROUP BY s.id\n" + 
			") AS temp2 ON temp1.dependsOnSprint = temp2.sid";
	
	private static final String TASKDEPENDENCY_SEQUENCE_ESTIMATED = 
			"SELECT  \n" + 
			"td.taskId AS \"taskId\", \n" + 
			"td.dependencyTaskId AS \"dependencyTaskId\", \n" + 
			"t1.startTime AS \"taskStartTime\", \n" + 
			"MAX(DATE_ADD(t2.startTime, INTERVAL t2.hoursEstimate HOUR)) "
			+ " AS \"dependencyTasksMaxFinishTime\" \n" + 
			"FROM TaskDependency AS td \n" + 
			"INNER JOIN Task AS t1 ON td.taskId = t1.id \n" + 
			"INNER JOIN Task AS t2 ON td.dependencyTaskId = t2.id \n" + 
			"GROUP BY taskId";

	private static final String SPRINT_ON_TIME_ESTIMATED = 
			"SELECT \n" + 
			"s.id AS \"sprintId\", \n" + 
			"MIN(t.startTime) AS \"sprintStartTime\", \n" + 
			"MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))"
			+ " AS \"sprintEstFinishTime\"\n" + 
			"FROM Sprint AS s \n" + 
			"INNER JOIN Task AS t ON t.sprintId = s.id\n" + 
			"WHERE s.projectId = ?\n" + 
			"GROUP BY s.id";
	
	private static final String PROJECT_ON_TIME_ESTIMATED = 
			"SELECT \n" + 
			"p.id AS \"projectId\", \n" + 
			"p.name AS \"projectName\", \n" + 
			"p.startDate AS \"projectStart\",\n" + 
			"p.endDate AS \"projectEnd\",\n" + 
			"MIN(t.startTime) AS \"projectTaskStartTime\", \n" + 
			"MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) "
			+ "AS \"projectTaskEstFinishTime\" \n" + 
			"FROM Sprint AS s \n" + 
			"INNER JOIN Project AS p ON p.id = s.projectId \n" + 
			"INNER JOIN Task AS t ON t.sprintId = s.id \n" + 
			"WHERE s.projectId = ?";
	
	private static final String TOTAL_EMPLOYEE_OVERTIMES_ESTIMATED = 
			"SELECT \n" + 
			"temp2.employeeId AS \"employeeId\",\n" + 
			"COUNT(*) AS \"numOfOvertimes\"\n" + 
			"FROM ( \n" + 
			"	SELECT\n" + 
			"	WEEKOFYEAR(temp.startTime) AS \"weekOfYear\", \n" + 
			"	temp.employeeId AS \"employeeId\", \n" + 
			"	temp.sprintId AS \"sprintId\",\n" + 
			"	temp.projectId AS \"projectId\",\n" + 
			"	SUM(temp.hoursEstimatedTime) AS \"estWorkingHours\"\n" + 
			"	FROM (\n" + 
			"		SELECT \n" + 
			"	    t.startTime AS \"startTime\", \n" + 
			"		TIMESTAMPDIFF(HOUR, t.startTime, "
			+ "		 DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))"
			+ "		 AS \"hoursEstimatedTime\",\n" + 
			"		ta.employeeId AS \"employeeId\",\n" + 
			"    		t.sprintId AS \"sprintId\",\n" + 
			"    		s.projectId AS \"projectId\"\n" + 
			"		FROM Task AS t\n" + 
			"		INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"		INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"		WHERE s.projectId = ?\n" + 
			"	) AS temp\n" + 
			"	GROUP BY \n" + 
			"	WEEKOFYEAR(temp.startTime), \n" + 
			"	temp.employeeId\n" + 
			"	HAVING estWorkingHours > " + OVERTIME_HOURS_THRESHOLD + "\n" + 
			") AS temp2\n" + 
			"GROUP BY employeeId\n" + 
			"ORDER BY employeeId ASC";
	
	private static final String TOTAL_SPRINT_OVERTIMES_ESTIMATED = 
			"SELECT \n" + 
			"temp2.sprintId AS \"sprintId\",\n" + 
			"COUNT(*) AS \"numOfOvertimes\"\n" + 
			"FROM ( \n" + 
			"	SELECT\n" + 
			"	WEEKOFYEAR(temp.startTime) AS \"weekOfYear\", \n" + 
			"	temp.employeeId AS \"employeeId\", \n" + 
			"	temp.sprintId AS \"sprintId\",\n" + 
			"	temp.projectId AS \"projectId\",\n" + 
			"	SUM(temp.hoursEstimatedTime) AS \"estWorkingHours\"\n" + 
			"	FROM (\n" + 
			"		SELECT \n" + 
			"	    t.startTime AS \"startTime\", \n" + 
			"		TIMESTAMPDIFF(HOUR, t.startTime, "
			+ "		 DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))"
			+ "		 AS \"hoursEstimatedTime\",\n" + 
			"		ta.employeeId AS \"employeeId\",\n" + 
			"    	t.sprintId AS \"sprintId\",\n" + 
			"    	s.projectId AS \"projectId\"\n" + 
			"		FROM Task AS t\n" + 
			"		INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"		INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"		WHERE s.projectId = ?\n" + 
			"	) AS temp\n" + 
			"	GROUP BY \n" + 
			"	WEEKOFYEAR(temp.startTime), \n" + 
			"	temp.employeeId\n" + 
			"	HAVING estWorkingHours > " + OVERTIME_HOURS_THRESHOLD + "\n" + 
			") AS temp2\n" + 
			"GROUP BY sprintId\n" + 
			"ORDER BY sprintId ASC";
	
	private static final String TOTAL_PROJECT_OVERTIMES_ESTIMATED = 
			"SELECT \n" + 
			"temp2.projectId AS \"projectId\",\n" + 
			"COUNT(*) AS \"numOfOvertimes\"\n" + 
			"FROM ( \n" + 
			"	SELECT\n" + 
			"	WEEKOFYEAR(temp.startTime) AS \"weekOfYear\", \n" + 
			"	temp.employeeId AS \"employeeId\", \n" + 
			"	temp.sprintId AS \"sprintId\",\n" + 
			"	temp.projectId AS \"projectId\",\n" + 
			"	SUM(temp.hoursEstimatedTime) AS \"estWorkingHours\"\n" + 
			"	FROM (\n" + 
			"		SELECT \n" + 
			"	    t.startTime AS \"startTime\", \n" + 
			"		TIMESTAMPDIFF(HOUR, t.startTime, "
			+ "		 DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) "
			+ "		 AS \"hoursEstimatedTime\",\n" + 
			"		ta.employeeId AS \"employeeId\",\n" + 
			"    	t.sprintId AS \"sprintId\",\n" + 
			"    	s.projectId AS \"projectId\"\n" + 
			"		FROM Task AS t\n" + 
			"		INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"		INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"		WHERE s.projectId = ?\n" + 
			"	) AS temp\n" + 
			"	GROUP BY \n" + 
			"	WEEKOFYEAR(temp.startTime), \n" + 
			"	temp.employeeId\n" + 
			"	HAVING estWorkingHours > " + OVERTIME_HOURS_THRESHOLD + "\n" + 
			") AS temp2\n" + 
			"GROUP BY projectId\n" + 
			"ORDER BY projectId DESC";
	
	
	private static final String TOTAL_TASK_DONE_ON_TIME = 
			"SELECT \n" + 
			"COUNT(*) AS \"numOfTasksDoneOnTime\"\n" + 
			"FROM (\n" + 
			"	SELECT \n" + 
			"	t.id,\n" + 
			"	TIMESTAMPDIFF(HOUR,  ta.finishTime, "
			+ "  DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))"
			+ "  AS \"hoursDeviation\"\n" + 
			"	FROM Task AS t\n" + 
			"    INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"    WHERE s.projectId = ? \n" + 
			"    ) AS temp\n" + 
			"WHERE temp.hoursDeviation >= 0";
	
	private static final String TOTAL_TASK_DONE_WITH_DILATION = 
			"SELECT \n" + 
			"COUNT(*) AS \"numOfTasksNotDoneOnTime\" \n" + 
			"FROM (\n" + 
			"	SELECT \n" + 
			"	t.id,\n" + 
			"	TIMESTAMPDIFF(HOUR,  ta.finishTime,"
			+ "  DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))"
			+ "  AS \"hoursDeviation\" \n" + 
			"	FROM Task AS t\n" + 
			"    INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"    WHERE s.projectId = ? \n" + 
			"    ) AS temp\n" + 
			"WHERE temp.hoursDeviation < 0";
	
	private static final String TOTAL_SPRINT_DONE_ON_TIME =
			"SELECT \n" + 
			"COUNT(temp.sprintId) AS \"numOfSprintsDoneOnTime\"\n" + 
			"FROM (\n" + 
			"	SELECT \n" + 
			"	s.id AS \"sprintId\",\n" + 
			"	TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime),"
			+ "  MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)))"
			+ "  AS \"hoursDeviation\"\n" + 
			"	FROM Task AS t\n" + 
			"	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"	INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"    WHERE s.projectId = ?\n" + 
			"	GROUP BY s.id\n" + 
			"    ) AS temp\n" + 
			"WHERE temp.hoursDeviation >= 0";
	
	private static final String TOTAL_SPRINT_DONE_WITH_DILATION =
			"SELECT \n" + 
			"COUNT(temp.sprintId) AS \"numOfSprintsDoneWithDilation\" \n" + 
			"FROM (\n" + 
			"	SELECT \n" + 
			"	s.id AS \"sprintId\",\n" + 
			"	TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime),"
			+ "  MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)))"
			+ "  AS \"hoursDeviation\"\n" + 
			"	FROM Task AS t\n" + 
			"	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"	INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"    WHERE s.projectId = ?\n" + 
			"	GROUP BY s.id\n" + 
			"    ) AS temp\n" + 
			"WHERE temp.hoursDeviation < 0";
	
	private static final String TASK_ACTUAL_DURATIONS = 
			"SELECT \n" + 
			"t.id AS \"taskId\", \n" + 
			"TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime)"
			+ " AS \"hoursTaskDuration\"\n" + 
			"FROM Task AS t\n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"WHERE s.projectId = ?";
	
	private static final String SPRINT_ACTUAL_DURATIONS =
			"SELECT \n" + 
			"t.sprintId AS \"sprintId\", \n" + 
			"TIMESTAMPDIFF(HOUR, MIN(ta.acceptedTime), MAX(ta.finishTime))"
			+ " AS \"hoursSprintDuration\"\n" + 
			"FROM Task AS t\n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"WHERE s.projectId = ? \n" + 
			"GROUP BY s.id";
	
	private static final String PROJECT_ACTUAL_DURATION = 
			"SELECT \n" + 
			"TIMESTAMPDIFF(HOUR, MIN(ta.acceptedTime), MAX(ta.finishTime))"
			+ " AS \"hoursProjecDuration\"\n" + 
			"FROM Task AS t\n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"WHERE s.projectId = ? \n"; 
	
	private static final String PROJECT_HUMAN_HOURS_BY_PROJECTID =
			"SELECT \n" + 
			"SUM(TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime))"
			+ " AS \"humanHoursProjectDuration\"\n" + 
			"FROM Task AS t\n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"INNER JOIN Sprint AS s ON t.sprintId = s.id\n" + 
			"WHERE s.projectId = ? ";
	
	private static final String TASK_ESTIMATED_ACTUAL_DEVIATIONS = 
			"SELECT \n" + 
			"t.id AS \"taskId\", \n" + 
			"TIMESTAMPDIFF(HOUR,  ta.finishTime,"
			+ " DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))"
			+ " AS \"hoursDeviation\" \n" + 
			"FROM Task AS t \n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id \n" + 
			"INNER JOIN Sprint AS s ON t.sprintId = s.id \n" + 
			"WHERE s.projectId = ? \n";
	
	private static final String SPRINT_ESTIMATED_ACTUAL_DEVIATIONS =
			"SELECT \n" + 
			"s.id AS \"sprintId\", \n" + 
			"TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime),"
			+ " MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)))"
			+ " AS \"hoursDeviation\"\n" + 
			"FROM Task AS t \n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id \n" + 
			"INNER JOIN Sprint AS s ON t.sprintId = s.id \n" + 
			"WHERE s.projectId = ? \n" + 
			"GROUP BY s.id";
	
	
	private static final String EMPLOYEES_ACTIVITY_AT_GIVEN_TIME =
			"SELECT \n" + 
			"ta.employeeId AS \"employeeId\",\n" + 
			"p.id AS \"projectId\", \n" + 
			"p.name AS \"projectName\", \n" + 
			"s.id AS \"sprintId\", \n" + 
			"s.name AS \"sprintName\", \n" + 
			"t.id AS \"taskId\", \n" + 
			"t.name AS \"taskName\"\n" + 
			"FROM Project AS p\n" + 
			"INNER JOIN Sprint AS s ON p.id = s.projectId\n" + 
			"INNER JOIN Task AS t ON s.id = t.sprintId\n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id\n" + 
			"WHERE ( \n" + 
			"( ? BETWEEN ta.acceptedTime AND ta.finishTime) \n" + 
			")";
	
	private static final String TASKS_DEVIATIONS_BY_EMPLOYEEID =
			"SELECT \n" + 
			"ta.employeeId AS \"employeeId\", \n" + 
			"t.id AS \"taskId\", \n" + 
			"TIMESTAMPDIFF(HOUR,  ta.finishTime,"
			+ " DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) "
			+ "AS \"hoursTaskDeviation\" \n" + 
			"FROM Task AS t \n" + 
			"INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id";
	
}
