/*
MY
SQL
SOME SAMPLE QUERIES
*/
/*
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	DATABASE SCHEMA TABLES MODIFICATIONS:

ALTER TABLE TaskAssignment 
	DROP COLUMN accepted,
	DROP COLUMN done;

ALTER TABLE TaskAssignment ADD COLUMN acceptedTime DATETIME;

CREATE TABLE EmployeeRequest (
	id INTEGER NOT NULL,
	employeeId INTEGER NOT NULL,
	taskId INTEGER NOT NULL,
	hours INTEGER NOT NULL
);

ALTER TABLE EmployeeRequest
	ADD CONSTRAINT EmployeeRequest_PK PRIMARY KEY (id);

ALTER TABLE EmployeeRequest
	ADD CONSTRAINT EmployeeRequest_EmpId_FK
	FOREIGN KEY (employeeId)
	REFERENCES Employee(id);

ALTER TABLE EmployeeRequest
	ADD CONSTRAINT EmployeeRequest_TaskId_FK
	FOREIGN KEY (taskId)
	REFERENCES Task(id);


ALTER TABLE TaskAssignment MODIFY finishTime DATETIME;


!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
*/
/*
  notice null behavior:

select pow(2,null), !null, null * 55;
select null + 4, 234523 - null, 82 * null, MOD(23, null), 72 / null, 28 > null, null > -2;
select null and 0, null and 1, null and 23, null or 0, null or 1, null or 23, not null, not 23, not 0;
select null & 0, null && 0, null & 1, null & 23, null && 23, null || 0, null | 1, null || 1, null | 23, null || 23, ! null, ! 23, ! 0;

  some aliases uses

SELECT e.id AS "employee id",
e.name AS "nick name in company"
FROM `Employee` AS e
WHERE e.email LIKE '%employee%'

  adddate function
SELECT t.id, t.startTime, t.hoursEstimate, ADDDATE(t.startTime,t.hoursEstimate / 24) AS "estimated end time"
FROM Task AS t

  subquery example:
SELECT *
FROM TaskAssignment
WHERE employeeId IN (
    SELECT id
    FROM Employee
    WHERE managerId = 1
    )

  query with inner join in mysql database:

 SELECT * 
FROM Project AS p
INNER JOIN Sprint AS s ON p.id = s.projectId
INNER JOIN Task AS t ON s.id = t.sprintId
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id

 SELECT * 
FROM Project AS p
INNER JOIN Sprint AS s ON p.id = s.projectId
INNER JOIN Task AS t ON s.id = t.sprintId
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE  ( ta.employeeId = 20
	OR s.dependsOn IS NULL )


  notice the difference between inner & left & right joins

SELECT * 
FROM Employee AS e
LEFT JOIN Employee AS m ON e.managerId = m.id

SELECT * 
FROM Employee AS e
RIGHT JOIN Employee AS m ON e.managerId = m.id


 query running statistics

 EXPLAIN SELECT * FROM Employee
 EXPLAIN SELECT * FROM Employee WHERE id = 17;

*/





/*
	VERIFYING QUERIES
*/
/*
	verifying first query:
	sprints executed sequentially
*/
SELECT * 
FROM Sprint AS s1 
INNER JOIN Task AS t1 ON t1.sprintId = s1.id
INNER JOIN Sprint AS s2 ON s1.dependsOn = s2.id
INNER JOIN Task AS t2 ON t2.sprintId = s2.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t2.id
WHERE s1.projectId = 1
AND s2.projectId = 1

SELECT 
s.id,
MIN(t.startTime)
FROM Sprint AS s
INNER JOIN Task AS t ON t.sprintId = s.id
WHERE s.projectId = 1
GROUP BY s.id

SELECT 
s.id,
MAX(ta.finishTime)
FROM Sprint AS s
INNER JOIN Task AS t ON t.sprintId = s.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE s.projectId = 1
GROUP BY s.id
/* projectId should be given!*/
SELECT temp1.sprintId, temp1.dependsOnSprint, temp1.sprintStartTime, temp2.sprintFinishTime
FROM (
	SELECT 
	s.id AS "sprintId",
	s.dependsOn AS "dependsOnSprint",
	MIN(t.startTime) AS "sprintStartTime"
	FROM Sprint AS s
	INNER JOIN Task AS t ON t.sprintId = s.id
	WHERE s.projectId = 1
	GROUP BY s.id
) AS temp1
INNER JOIN (
	SELECT 
	s.id AS "sid",
	MAX(ta.finishTime) AS "sprintFinishTime"
	FROM Sprint AS s
	INNER JOIN Task AS t ON t.sprintId = s.id
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	WHERE s.projectId = 1
	GROUP BY s.id
) AS temp2 ON temp1.sprintId = temp2.sid	
/* more correct: (it based on actual duration)*/
SELECT temp1.sprintId, temp1.dependsOnSprint, temp1.sprintStartTime, temp2.sprintFinishTime AS "dependsOnFinishTime"
FROM (
	SELECT 
	s.id AS "sprintId",
	s.dependsOn AS "dependsOnSprint",
	MIN(t.startTime) AS "sprintStartTime"
	FROM Sprint AS s
	INNER JOIN Task AS t ON t.sprintId = s.id
	WHERE s.projectId = 1
	GROUP BY s.id
) AS temp1
LEFT JOIN (
	SELECT 
	s.id AS "sid",
	MAX(ta.finishTime) AS "sprintFinishTime"
	FROM Sprint AS s
	INNER JOIN Task AS t ON t.sprintId = s.id
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	WHERE s.projectId = 1
	GROUP BY s.id
) AS temp2 ON temp1.dependsOnSprint = temp2.sid
/* another (it based on estimated duration): */
SELECT 
temp1.sprintId AS "sprintId", 
temp1.dependsOnSprint AS "dependencySprint", 
temp1.sprintStartTime AS "sprintStartTime", 
temp2.sprintFinishTime AS "estimatedDependencyFinishTime"
FROM (
	SELECT 
	s.id AS "sprintId",
	s.dependsOn AS "dependsOnSprint",
	MIN(t.startTime) AS "sprintStartTime"
	FROM Sprint AS s
	INNER JOIN Task AS t ON t.sprintId = s.id
	WHERE s.projectId = 1
	GROUP BY s.id
) AS temp1
LEFT JOIN (
	SELECT 
	s.id AS "sid",
	MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "sprintFinishTime"
	FROM Sprint AS s
	INNER JOIN Task AS t ON t.sprintId = s.id
	WHERE s.projectId = 1
	GROUP BY s.id
) AS temp2 ON temp1.dependsOnSprint = temp2.sid
/*
	verifying second query:
	task dependencies executed in right order
*/
SELECT temp1.taskId, temp1.dependencyTaskId, temp1.startTime, temp2.dependencyMaxFinishTime
FROM (
SELECT 
td.taskId AS "taskId" , td.dependencyTaskId AS "dependencyTaskId", t1.startTime AS "startTime"
FROM TaskDependency AS td
INNER JOIN Task AS t1 ON td.taskId = t1.id
) AS temp1
INNER JOIN (
SELECT td2.dependencyTaskId AS "taskId",
MAX(ta.finishTime) AS "dependencyMaxFinishTime"
FROM TaskDependency AS td2
INNER JOIN Task AS t2 ON td2.dependencyTaskId = t2.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t2.id
GROUP BY taskId
) AS temp2 ON temp1.dependencyTaskId = temp2.taskId
/* above probably wrong!*/
/* actual time: */
SELECT  td.taskId, ta.taskId AS "dependencyTaskId", 
t1.startTime AS "taskStartTime", MAX(ta.finishTime) AS "dependencyTasksMaxFinishTime"
FROM TaskDependency AS td
INNER JOIN Task AS t1 ON td.taskId = t1.id
INNER JOIN Task AS t2 ON td.dependencyTaskId = t2.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t2.id
GROUP BY taskId
/* estimated time: */
SELECT  
td.taskId AS "taskId", 
td.dependencyTaskId AS "dependencyTaskId", 
t1.startTime AS "taskStartTime", 
MAX(DATE_ADD(t2.startTime, INTERVAL t2.hoursEstimate HOUR)) AS "dependencyTasksMaxFinishTime"
FROM TaskDependency AS td
INNER JOIN Task AS t1 ON td.taskId = t1.id
INNER JOIN Task AS t2 ON td.dependencyTaskId = t2.id
GROUP BY taskId
/*
	verifying third query:
	sprints & project will be done on time
*/
SELECT
p.id, p.startDate, p.endDate, s.id, MIN(t.startTime) AS "sprintStartTime", MAX(ta.finishTime) AS "sprintFinishTime"
FROM Project AS p
INNER JOIN Sprint AS s ON s.projectId = p.id
INNER JOIN Task AS t ON t.sprintId = s.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
GROUP BY s.id
/* sprint estimation: */
SELECT
s.id AS "sprintId", 
MIN(t.startTime) AS "sprintStartTime", 
MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "sprintEstFinishTime"
FROM Sprint AS s 
INNER JOIN Task AS t ON t.sprintId = s.id
WHERE s.projectId = 1
GROUP BY s.id
/* project estimation: */
SELECT
p.id AS "projectId", 
p.name AS "projectName",
p.startDate AS "projectStart",
p.endDate AS "projectEnd",
MIN(t.startTime) AS "projectTaskStartTime", 
MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "projectTaskEstFinishTime"
FROM Sprint AS s 
INNER JOIN Project AS p ON p.id = s.projectId
INNER JOIN Task AS t ON t.sprintId = s.id
WHERE s.projectId = 1
/*
	verifying fourth query:
	employees have not overtimes and their quantity
*/
SELECT 
t.id, t.startTime, t.hoursEstimate, 
ta.finishTime,
DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR) AS "estimatedFinishTime",
TIMESTAMPDIFF(HOUR, t.startTime, ta.finishTime) AS "hoursActualTime",
TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation",
ta.employeeId
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id;
-- above old(depracated)
/* assume the following works: */
SELECT
WEEKOFYEAR(temp.startTime) AS "weekOfYear", temp.employeeId AS "employeeId", SUM(temp.hoursActualTime) AS "workingHours"
FROM (
	SELECT 
	t.id, t.startTime AS "startTime", t.hoursEstimate, 
	TIMESTAMPDIFF(HOUR, t.startTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursActualTime",
	ta.employeeId AS "employeeId"
	FROM Task AS t
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	INNER JOIN Sprint AS s ON t.sprintId = s.id
	WHERE s.projectId = 1
) AS temp
GROUP BY WEEKOFYEAR(temp.startTime), temp.employeeId
-- HAVING SUM(temp.hoursActualTime) > 40;
/* more detailed (estimated): */
SELECT
WEEKOFYEAR(temp.startTime) AS "weekOfYear", 
temp.employeeId AS "employeeId", 
temp.sprintId AS "sprintId",
temp.projectId AS "projectId",
SUM(temp.hoursEstimatedTime) AS "estWorkingHours"
FROM (
	SELECT 
    t.startTime AS "startTime", 
	TIMESTAMPDIFF(HOUR, t.startTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursEstimatedTime",
	ta.employeeId AS "employeeId",
    t.sprintId AS "sprintId",
    s.projectId AS "projectId"
	FROM Task AS t
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	INNER JOIN Sprint AS s ON t.sprintId = s.id
	WHERE s.projectId = 1
) AS temp
GROUP BY 
WEEKOFYEAR(temp.startTime), 
temp.employeeId
HAVING estWorkingHours > 40;
/* 40 is week working hours threshold !!! */
/* # overtimes by employee */
SELECT 
temp2.employeeId AS "employeeId",
COUNT(*) AS "numOfOvertimes"
FROM ( 
	SELECT
	WEEKOFYEAR(temp.startTime) AS "weekOfYear", 
	temp.employeeId AS "employeeId", 
	temp.sprintId AS "sprintId",
	temp.projectId AS "projectId",
	SUM(temp.hoursEstimatedTime) AS "estWorkingHours"
	FROM (
		SELECT 
	    t.startTime AS "startTime", 
		TIMESTAMPDIFF(HOUR, t.startTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursEstimatedTime",
		ta.employeeId AS "employeeId",
    		t.sprintId AS "sprintId",
    		s.projectId AS "projectId"
		FROM Task AS t
		INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
		INNER JOIN Sprint AS s ON t.sprintId = s.id
		WHERE s.projectId = 1
	) AS temp
	GROUP BY 
	WEEKOFYEAR(temp.startTime), 
	temp.employeeId
	HAVING estWorkingHours > 40
) AS temp2
GROUP BY employeeId
ORDER BY employeeId ASC
/* # overtimes by sprint */
SELECT 
temp2.sprintId AS "sprintId",
COUNT(*) AS "numOfOvertimes"
FROM ( 
	SELECT
	WEEKOFYEAR(temp.startTime) AS "weekOfYear", 
	temp.employeeId AS "employeeId", 
	temp.sprintId AS "sprintId",
	temp.projectId AS "projectId",
	SUM(temp.hoursEstimatedTime) AS "estWorkingHours"
	FROM (
		SELECT 
	    t.startTime AS "startTime", 
		TIMESTAMPDIFF(HOUR, t.startTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursEstimatedTime",
		ta.employeeId AS "employeeId",
    	t.sprintId AS "sprintId",
    	s.projectId AS "projectId"
		FROM Task AS t
		INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
		INNER JOIN Sprint AS s ON t.sprintId = s.id
		WHERE s.projectId = 1
	) AS temp
	GROUP BY 
	WEEKOFYEAR(temp.startTime), 
	temp.employeeId
	HAVING estWorkingHours > 40
) AS temp2
GROUP BY sprintId
ORDER BY sprintId ASC
/* # overtimes by project */
SELECT 
temp2.projectId AS "projectId",
COUNT(*) AS "numOfOvertimes"
FROM ( 
	SELECT
	WEEKOFYEAR(temp.startTime) AS "weekOfYear", 
	temp.employeeId AS "employeeId", 
	temp.sprintId AS "sprintId",
	temp.projectId AS "projectId",
	SUM(temp.hoursEstimatedTime) AS "estWorkingHours"
	FROM (
		SELECT 
	    t.startTime AS "startTime", 
		TIMESTAMPDIFF(HOUR, t.startTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursEstimatedTime",
		ta.employeeId AS "employeeId",
    	t.sprintId AS "sprintId",
    	s.projectId AS "projectId"
		FROM Task AS t
		INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
		INNER JOIN Sprint AS s ON t.sprintId = s.id
		WHERE s.projectId = 1
	) AS temp
	GROUP BY 
	WEEKOFYEAR(temp.startTime), 
	temp.employeeId
	HAVING estWorkingHours > 40
) AS temp2
GROUP BY projectId
ORDER BY projectId ASC

/*
	PROJECT, SPRINT, TASK STATISTICS QUERIES
*/
/*
	first query:
	how many sprints, tasks were and weren't done on time!
*/
/* # tasks done on time */
SELECT 
COUNT(*) AS "#taskDoneOnTime"
FROM (
	SELECT 
	t.id, t.startTime, t.hoursEstimate, 
	ta.acceptedTime, ta.finishTime,
	DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR) AS "estimatedFinishTime",
	TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursActualTime",
	TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation"
	FROM Task AS t
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
    ) AS temp
WHERE temp.hoursDeviation >= 0
/* prev edited: */
SELECT 
COUNT(*) AS "numOfTasksDoneOnTime"
FROM (
	SELECT 
	t.id,
	TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation"
	FROM Task AS t
    INNER JOIN Sprint AS s ON t.sprintId = s.id
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
    WHERE s.projectId = 1
    ) AS temp
WHERE temp.hoursDeviation >= 0
/* prev not done on time(with dilation): */
SELECT 
COUNT(*) AS "numOfTasksNotDoneOnTime"
FROM (
	SELECT 
	t.id,
	TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation"
	FROM Task AS t
    INNER JOIN Sprint AS s ON t.sprintId = s.id
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
    WHERE s.projectId = 1
    ) AS temp
WHERE temp.hoursDeviation < 0

/* # sprints on time */
SELECT 
COUNT(temp.sprintId) AS "#sprintDoneOnTime"
FROM (
	SELECT 
	s.id AS "sprintId",
	MIN(t.startTime) AS "sprintStart",
	MAX(ta.finishTime) AS "sprintEnd",
	DATE_ADD(MIN(t.startTime), INTERVAL SUM(t.hoursEstimate) HOUR) AS "estimatedFinishTime",
	TIMESTAMPDIFF(HOUR, MIN(ta.acceptedTime), MAX(ta.finishTime)) AS "hoursActualTime",
	TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime), DATE_ADD(MIN(t.startTime), INTERVAL SUM(t.hoursEstimate) HOUR)) AS "hoursDeviation"
	FROM Task AS t
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	INNER JOIN Sprint AS s ON t.sprintId = s.id
	GROUP BY s.id
    ) AS temp
WHERE temp.hoursDeviation >= 0;
/* edited: */
SELECT 
COUNT(temp.sprintId) AS "numOfSprintsDoneOnTime"
FROM (
	SELECT 
	s.id AS "sprintId",
	TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime), MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))) AS "hoursDeviation"
	FROM Task AS t
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	INNER JOIN Sprint AS s ON t.sprintId = s.id
    WHERE s.projectId = 1
	GROUP BY s.id
    ) AS temp
WHERE temp.hoursDeviation >= 0;
/* #sprints with dilation */
SELECT 
COUNT(temp.sprintId) AS "numOfSprintsDoneWithDilation"
FROM (
	SELECT 
	s.id AS "sprintId",
	TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime), MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))) AS "hoursDeviation"
	FROM Task AS t
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	INNER JOIN Sprint AS s ON t.sprintId = s.id
    WHERE s.projectId = 1
	GROUP BY s.id
    ) AS temp
WHERE temp.hoursDeviation < 0;
/*
	second query:
	what is the duration of sprint, tasks
*/
/* task actual durations in human-hours (consider different ta for the same task)!*/
SELECT 
t.id AS "taskId", 
TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursTaskDuration"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1
/* sprint actual durations in human-hours */
SELECT 
t.sprintId AS "sprintId", 
SUM(TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime)) AS "humanHoursSprintDuration"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1
GROUP BY s.id
/* sprint actual duration in hours*/
SELECT 
t.sprintId AS "sprintId", 
TIMESTAMPDIFF(HOUR, MIN(ta.acceptedTime), MAX(ta.finishTime)) AS "hoursSprintDuration"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1
GROUP BY s.id
/* project actual duration in human-hours */
SELECT 
SUM(TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime)) AS "humanHoursProjectDuration"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1
/* project duration in hours */
SELECT 
TIMESTAMPDIFF(HOUR, MIN(ta.acceptedTime), MAX(ta.finishTime)) AS "hoursProjecDuration"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1

/*
	third query:
	how many human-hours spend
*/
/* per sprint */
SELECT temp.sprintId AS "sprintId", SUM(temp.hoursActualTime) AS "totalHumanHours"
FROM (
	SELECT
	t.sprintId AS "sprintId",
	TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursActualTime"
	FROM TaskAssignment AS ta
	INNER JOIN Task AS t ON t.id = ta.taskId
	INNER JOIN Sprint AS s ON t.sprintId = s.id
	WHERE s.projectId = 1
) AS temp
GROUP BY temp.sprintId


/*
	fourth query:
	deviation of estimation & duration of tasks

template:
SELECT 
s.projectId AS "sprojectId",
s.id AS "sprintId",
t.id AS "taskId", 
t.startTime AS "taskStartTime", 
t.hoursEstimate AS "taskHoursEstimation", 
ta.acceptedTime AS "taskActualAcceptedTime",
ta.finishTime AS "taskActualFinishTime",
DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR) AS "estimatedFinishTime",
TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursActualTime",
TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1


select *, 
SUM(hoursEstimate), MIN(t.startTime), MAX(ta.finishTime)
from Sprint as s 
inner join Task as t on t.sprintId = s.id
inner join TaskAssignment as ta on ta.taskId = t.id
group by s.id
*/
/* by task (old)*/
SELECT 
t.id, t.startTime, t.hoursEstimate, 
ta.finishTime,
DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR) AS "estimatedFinishTime",
TIMESTAMPDIFF(HOUR, t.startTime, ta.finishTime) AS "hoursActualTime",
TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id;
/* task hours deviation from estimated */
SELECT 
t.id AS "taskId", 
TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1
/* by sprint (old)*/
SELECT 
s.id,
MIN(t.startTime) AS "sprintStart",
MAX(ta.finishTime) AS "sprintEnd",
DATE_ADD(MIN(t.startTime), INTERVAL SUM(t.hoursEstimate) HOUR) AS "estimatedFinishTime",
TIMESTAMPDIFF(HOUR, MIN(t.startTime), MAX(ta.finishTime)) AS "hoursActualTime",
TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime), DATE_ADD(MIN(t.startTime), INTERVAL SUM(t.hoursEstimate) HOUR)) AS "hoursDeviation"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
GROUP BY s.id;
/* sprint hours deviation from estimated */
SELECT 
s.id AS "sprintId",
TIMESTAMPDIFF(HOUR,  MAX(ta.finishTime), MAX(DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR))) AS "hoursDeviation"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON t.sprintId = s.id
WHERE s.projectId = 1
GROUP BY s.id;

/*
	EMPLOYEE STATISTICS QUERIES
*/
/*
	employee statistics first query:
	projects, sprints and tasks on what employee works in a given time
*/
SELECT * 
FROM Project AS p
INNER JOIN Sprint AS s ON p.id = s.projectId
INNER JOIN Task AS t ON s.id = t.sprintId
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE  ( ta.employeeId = 20
	AND '2017-01-01 00:00:00' BETWEEN t.startTime AND ta.finishTime )

UPDATE `TaskAssignment` SET `accepted`=1,`done`=1,`finishTime`='2017-01-03 10:00:00' WHERE id = 1;
UPDATE `TaskAssignment` SET `accepted`=1,`done`=1,`finishTime`='2017-01-04 20:00:00' WHERE id = 2;
UPDATE `TaskAssignment` SET `accepted`=1,`done`=1,`finishTime`='2017-01-07 16:00:00' WHERE id = 3;

/* employeeId & datetime should be given !*/
SELECT 
p.id, p.name, p.startDate, p.endDate, p.projectManagerId, 
s.id, s.name, 
t.id, t.name,
t.startTime,
ta.finishTime
FROM Project AS p
INNER JOIN Sprint AS s ON p.id = s.projectId
INNER JOIN Task AS t ON s.id = t.sprintId
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE  ( ta.employeeId = 2
	AND '2017-01-02 11:59:59' BETWEEN t.startTime AND ta.finishTime )
/* above is old. try this: */
SELECT 
p.id, p.name, p.startDate, p.endDate, p.projectManagerId, 
s.id, s.name, 
t.id, t.name,
t.startTime,
ta.acceptedTime,
ta.finishTime
FROM Project AS p
INNER JOIN Sprint AS s ON p.id = s.projectId
INNER JOIN Task AS t ON s.id = t.sprintId
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE  ( ta.employeeId = 2
	AND '2017-01-02 11:59:59' BETWEEN ta.acceptedTime AND ta.finishTime )
/* more specific: for given employeeId & datetime */
SELECT 
p.id AS "projectId", 
p.name AS "projectName", 
s.id AS "sprintId", 
s.name AS "sprintName", 
t.id AS "taskId", 
t.name AS "taskName"
FROM Project AS p
INNER JOIN Sprint AS s ON p.id = s.projectId
INNER JOIN Task AS t ON s.id = t.sprintId
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE ( 
ta.employeeId = 2
AND ('2017-01-02 11:59:59' BETWEEN ta.acceptedTime AND ta.finishTime)
)
-- WHERE ( ? BETWEEN ta.acceptedTime AND ta.finishTime )
/* more general: for any employeeId & given datetime */
SELECT 
ta.employeeId AS "employeeId",
p.id AS "projectId", 
p.name AS "projectName", 
s.id AS "sprintId", 
s.name AS "sprintName", 
t.id AS "taskId", 
t.name AS "taskName"
FROM Project AS p
INNER JOIN Sprint AS s ON p.id = s.projectId
INNER JOIN Task AS t ON s.id = t.sprintId
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE ( 
('2017-01-02 11:59:59' BETWEEN ta.acceptedTime AND ta.finishTime)
)
/*
	employee statistics second query:
	overtimes of employee
*/
/* assume this: */
SELECT
WEEKOFYEAR(temp.acceptedTime), temp.employeeId, SUM(temp.hoursActualTime)
FROM (
	SELECT 
	t.id, t.startTime, t.hoursEstimate, 
	ta.acceptedTime AS "acceptedTime", ta.finishTime,
	TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursActualTime",
	ta.employeeId AS "employeeId"
	FROM Task AS t
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
) AS temp
GROUP BY WEEKOFYEAR(temp.acceptedTime), temp.employeeId
HAVING SUM(temp.hoursActualTime) > 40;
   
/* more concrete */    
SELECT 
WEEKOFYEAR(temp.acceptedTime) AS "weekOfYear",
temp.employeeId AS "employeeId", 
(40 - SUM(temp.hoursActualTime)) AS "overtimeHours"
FROM (
	SELECT  
	ta.acceptedTime AS "acceptedTime",
	TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursActualTime",
	ta.employeeId AS "employeeId"
	FROM Task AS t
    INNER JOIN Sprint AS s ON t.sprintId = s.id
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	WHERE s.projectId = 1
) AS temp
GROUP BY WEEKOFYEAR(temp.acceptedTime), temp.employeeId
HAVING SUM(temp.hoursActualTime) > 40;
/* above failed once */
SELECT 
WEEKOFYEAR(temp.acceptedTime) AS "weekOfYear",
temp.employeeId AS "employeeId", 
(SUM(temp.hoursActualTime) - 40) AS "overtimeHours"
FROM (
	SELECT  
	ta.acceptedTime AS "acceptedTime",
	TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursActualTime",
	ta.employeeId AS "employeeId"
	FROM Task AS t
    INNER JOIN Sprint AS s ON t.sprintId = s.id
	INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
	WHERE s.projectId = 1
) AS temp
GROUP BY WEEKOFYEAR(temp.acceptedTime), temp.employeeId
HAVING SUM(temp.hoursActualTime) > 40;

/*
	employee statistics third query:
	employee's not done tasks in estimated time
*/
SELECT 
t.id, t.startTime, t.hoursEstimate, ADDDATE(t.startTime,t.hoursEstimate / 24) AS "estimated end time",
ta.finishTime AS "finish time"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id

SELECT 
t.id AS "task id", t.startTime, t.hoursEstimate, ADDDATE(t.startTime,t.hoursEstimate / 24) AS "estimated end time",
ta.finishTime AS "finish time", ta.employeeId AS "employee Id"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
/* this works (maybe group by employee NO NO NO): */
SELECT 
t.id, t.startTime, t.hoursEstimate, 
ta.acceptedTime, ta.finishTime, ta.employeeId,
DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR) AS "estimatedFinishTime",
TIMESTAMPDIFF(HOUR, ta.acceptedTime, ta.finishTime) AS "hoursActualTime",
TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursDeviation"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id;
/* concise: */
SELECT 
ta.employeeId AS "employeeId",
t.id AS "taskId", 
TIMESTAMPDIFF(HOUR,  ta.finishTime, DATE_ADD(t.startTime, INTERVAL t.hoursEstimate HOUR)) AS "hoursTaskDeviation"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
