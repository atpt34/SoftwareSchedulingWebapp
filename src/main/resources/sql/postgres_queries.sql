/*
 * date & time functions:
 * 
 * 
 * 

SELECT e
FROM Employee as E

select 
startTime, 
to_char(hoursEstimate,'99 hours'),

-- startTime + to_timestamp(to_char(hoursEstimate,'99'),'HH24'),

startTime + interval '1 day' as "plus day",
startTime + interval '1 hour' as "plus hour",
chr(hoursEstimate),
hoursEstimate || ' hours' as "concatenate",
now() as "now function",
current_timestamp,
age(now(), startTime),
(now() - startTime) as "difference",
extract(hour from startTime) as "hour in startTime",

extract(day from age(now(), startTime))*24 as "difference's days",
extract(hour from age(now(), startTime)) as "difference's hour",
extract(day from age(now(), startTime))*24 + extract(hour from age(now(), startTime)) as "difference in hours",

extract(week from startTime) as "extract weekofyear"
from Task;

select
startTime,
hoursEstimate,
startTime + interval '1h' * hoursEstimate AS "startTime + hoursEstimate"
from Task;

select
now()-startTime as "diff",
extract(epoch from current_timestamp) as "epoch of now",
floor(extract(epoch from (now() - startTime)) / 3600) as "difference in hours",


startTime,
hoursEstimate,
startTime + interval '1h' * hoursEstimate AS "startTime + hoursEstimate"
from Task;

sample pgsql function:
CREATE OR REPLACE FUNCTION "ManagerEmployeesFunction"  (managerId INT)
 RETURNS TABLE ("empid" INT, "ename" TEXT, "email" TEXT) AS 
$BODY$
SELECT 
 id AS "empid",
 name AS "ename",
 email AS "email"
FROM Employee AS e
WHERE e.managerId = $1
$BODY$ LANGUAGE SQL;

or 
CREATE OR REPLACE FUNCTION ManagerEmployeesFunction  (INT)
 RETURNS TABLE ("empid" INT, "ename" TEXT, "email" TEXT) AS 
$BODY$
SELECT 
 id AS "empid",
 name AS "ename",
 email AS "email"
FROM Employee AS e
WHERE e.managerId = $1
$BODY$ LANGUAGE SQL;

get function result:
SELECT * 
FROM "ManagerEmployeesFunction"(1);

SELECT ename, empid, email
FROM "ManagerEmployeesFunction"(1);

for the second function: 
SELECT ename, empid, email
FROM manageremployeesfunction(1);

 * 
 * 
 */
/*
 * VIEWs:
 * 
 */
CREATE OR REPLACE VIEW EmployeeActualWorkingHoursView AS (
SELECT 
EXTRACT(WEEK FROM temp.acceptedTime) AS "weekOfYear",
temp.employeeid AS "employeeId",
SUM(temp.actualHours) AS "actualWorkingHours"
FROM (
 SELECT 
 ta.acceptedTime AS acceptedTime,
 ta.finishTime AS finishTime,
 FLOOR(EXTRACT(EPOCH FROM (
  ta.finishTime - ta.acceptedTime ))  / 3600) AS actualHours,
 ta.employeeId AS employeeid
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
) AS temp
GROUP BY 
"weekOfYear",
"employeeId"
ORDER BY
"weekOfYear",
"employeeId"
)

CREATE OR REPLACE VIEW TotalTaskHumanHoursView AS (
SELECT 
t.id AS "taskId",
SUM(FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600)) AS "actualHumanHours"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
GROUP BY t.id
ORDER BY t.id 
)

CREATE OR REPLACE VIEW numOfTaskPerProjectSprintView AS (
SELECT
s.projectId AS "projectId",
s.id AS "sprintId",
COUNT(*) AS "numOfTasks"
FROM Sprint AS s
INNER JOIN Task AS t ON t.sprintId = s.id
GROUP BY s.projectId, s.id
ORDER BY s.projectId, s.id
)
DROP VIEW NumOfTaskPerProjectSprint;

CREATE OR REPLACE VIEW  ActualTaskDurationView AS  (
SELECT 
s.projectId AS "projectId",
s.id AS "sprintId",
t.id AS "taskId",
FLOOR(EXTRACT(EPOCH FROM (MAX(ta.finishTime) - MIN(ta.acceptedTime))) / 3600) AS "taskActualHourDuration"
FROM Task AS t
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
INNER JOIN Sprint AS s ON s.id = t.sprintId
GROUP BY s.projectId, s.id, t.id
ORDER BY s.projectId, s.id, t.id
)

CREATE OR REPLACE VIEW TaskEstimateDeviationView AS ( 
SELECT
 s.projectId AS "projectId",
 s.id AS "sprintId",
 t.id AS "taskId",
 t.startTime AS "taskEstStartTime",
 t.startTime + INTERVAL '1h' * t.hoursEstimate AS "taskEstEndTime",
 MAX(ta.finishTime) AS "actualFinishTime",
 ((t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) AS "estimateDeviation"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
GROUP BY s.projectId, s.id, t.id
ORDER BY s.projectId, s.id, t.id
)

/*
 * Queries, functions, views:
 */
/* EMPLOYEE OVERTIMES ESTIMATED: */
/* more readable: */
SELECT 
EXTRACT(WEEK FROM temp."startTime") AS "weekOfYear",
temp."employeeId" AS "employeeId",
SUM(temp."hoursEstimatedTime") AS "workingHours"
FROM (
 SELECT 
 t.startTime AS "startTime",
 t.hoursEstimate AS "hoursEstimatedTime",
 ta.employeeId AS "employeeId"
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = 1
) AS temp
GROUP BY 
"weekOfYear",
"employeeId"
/* more concise: */
SELECT 
EXTRACT(WEEK FROM temp.starttime) AS "weekOfYear",
temp.employeeid AS "employeeId",
SUM(temp.hoursestimate) AS "workingHours"
FROM (
 SELECT 
 t.startTime AS starttime,
 t.hoursEstimate AS hoursestimate,
 ta.employeeId AS employeeid
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = 1
) AS temp
GROUP BY 
"weekOfYear",
"employeeId"
/* VIEW */
CREATE OR REPLACE VIEW "EmployeeOvertimeEstimateView" AS (
SELECT 
EXTRACT(WEEK FROM temp.starttime) AS "weekOfYear",
temp.employeeid AS "employeeId",
SUM(temp.hoursestimate) AS "workingHours"
FROM (
 SELECT 
 t.startTime AS starttime,
 t.hoursEstimate AS hoursestimate,
 ta.employeeId AS employeeid
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = 1
) AS temp
GROUP BY 
"weekOfYear",
"employeeId"
)

DROP VIEW "EmployeeOvertimeActualView";
DROP VIEW "EmployeeOvertimeEstimateView";
DROP VIEW "SprintHumanHoursView";

/* function: */
CREATE OR REPLACE FUNCTION employeeOvertimeEstimateFunction (projectId INT)
RETURNS TABLE ("weekOfYear" INT, "employeeId" INT, "workingHours" INT) AS 
$BODY$
SELECT 
CAST(EXTRACT(WEEK FROM temp.starttime) AS INT) AS "weekOfYear",
temp.employeeid AS "employeeId",
CAST(SUM(temp.hoursestimate) AS INT) AS "workingHours"
FROM (
 SELECT 
 t.startTime AS starttime,
 t.hoursEstimate AS hoursestimate,
 ta.employeeId AS employeeid
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = $1
) AS temp
GROUP BY 
"weekOfYear",
"employeeId"
$BODY$ LANGUAGE SQL;

DROP FUNCTION IF EXISTS employeeovertimeestimate(INT);

SELECT 
"weekOfYear",
"employeeId",
"workingHours"
FROM EmployeeOvertimeEstimateFunction(?)
ORDER BY "weekOfYear" ASC, "employeeId" ASC;

/* HUMAN-HOURS BY SPRINT: */
SELECT 
temp.sprintid AS "sprintId",
SUM(temp.hoursactualtime) AS "totalHumanHours"
FROM (
 SELECT 
 t.sprintId AS sprintid,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime
 FROM TaskAssignment AS ta
 INNER JOIN Task AS t ON t.id = ta.taskId
 INNER JOIN Sprint AS s ON s.id = t.sprintId
 WHERE s.projectId = 1
) AS temp
GROUP BY temp.sprintid
/* VIEW */
CREATE OR REPLACE VIEW "SprintHumanHoursView" AS (
SELECT 
temp.sprintid AS "sprintId",
SUM(temp.hoursactualtime) AS "totalHumanHours"
FROM (
 SELECT 
 t.sprintId AS sprintid,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime
 FROM TaskAssignment AS ta
 INNER JOIN Task AS t ON t.id = ta.taskId
 INNER JOIN Sprint AS s ON s.id = t.sprintId
 WHERE s.projectId = 1
) AS temp
GROUP BY temp.sprintid
)

/* FUNCTION: */
CREATE OR REPLACE FUNCTION SprintHumanHoursFunction (projectId INT)
RETURNS TABLE ("sprintId" INT, "totalHumanHours" INT) AS 
$BODY$
SELECT 
CAST(temp.sprintid AS INT) AS "sprintId",
CAST(SUM(temp.hoursactualtime) AS INT) AS "totalHumanHours"
FROM (
 SELECT 
 t.sprintId AS sprintid,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime
 FROM TaskAssignment AS ta
 INNER JOIN Task AS t ON t.id = ta.taskId
 INNER JOIN Sprint AS s ON s.id = t.sprintId
 WHERE s.projectId = $1
) AS temp
GROUP BY temp.sprintid
$BODY$ LANGUAGE SQL;

drop function if exists SprintHumanHours(int);

SELECT 
"sprintId",
"totalHumanHours"
FROM SprintHumanHoursFunction(1);

/* EMPLOYEE OVERTIMES ACTUAL: */
SELECT 
EXTRACT (WEEK FROM temp.acceptedtime) AS "weekOfYear",
temp.employeeid AS "employeeId",
(SUM(temp.hoursactualtime) - 40) AS "overtimeHours"
FROM (
 SELECT
 ta.acceptedTime AS acceptedtime,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime,
 ta.employeeId AS employeeid
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
) AS temp
GROUP BY "weekOfYear", "employeeId"
HAVING SUM(temp.hoursactualtime) > 40
ORDER BY "weekOfYear", "employeeId" ASC
/* VIEW: */
CREATE OR  REPLACE VIEW "EmployeeOvertimeActualView" AS (
SELECT 
EXTRACT (WEEK FROM temp.acceptedtime) AS "weekOfYear",
temp.employeeid AS "employeeId",
(SUM(temp.hoursactualtime) - 40) AS "overtimeHours"
FROM (
 SELECT
 ta.acceptedTime AS acceptedtime,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime,
 ta.employeeId AS employeeid
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
) AS temp
GROUP BY "weekOfYear", "employeeId"
HAVING SUM(temp.hoursactualtime) > 40
)
/* FUNCTION: */
CREATE OR REPLACE FUNCTION employeeOvertimeActualFunction (projectId INT, overtimeHours INT)
RETURNS TABLE ("weekOfYear" INT, "employeeId" INT, "overtimeHours" INT) AS 
$BODY$
SELECT 
CAST(EXTRACT (WEEK FROM temp.acceptedtime) AS INT)AS "weekOfYear",
CAST(temp.employeeid AS INT) AS "employeeId",
CAST((SUM(temp.hoursactualtime) - $2) AS INT) AS "overtimeHours"
FROM (
 SELECT
 ta.acceptedTime AS acceptedtime,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime,
 ta.employeeId AS employeeid
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
) AS temp
GROUP BY "weekOfYear", "employeeId"
HAVING SUM(temp.hoursactualtime) > $2
$BODY$ LANGUAGE SQL;

SELECT
"weekOfYear",
"employeeId",
"overtimeHours"
FROM employeeOvertimeActualFunction(1, 40);


/* SPRINT SEQUENCE ESTIMATE */
SELECT 
temp1.sprintId AS "sprintId",
temp1.dependsOnSprint AS "dependencySprint",
temp1.sprintStartTime AS "sprintStartTime",
temp2.sprintFinishTime AS "estimateDependencyFinishTime"
FROM (
 SELECT 
 s.id AS sprintId,
 s.dependsOn AS dependsOnSprint,
 MIN(t.startTime) AS sprintStartTime
 FROM Sprint AS s
 INNER JOIN Task AS t ON t.sprintId = s.id
 WHERE s.projectId = 1
 GROUP BY s.id
) AS temp1
LEFT JOIN (
 SELECT
 s.id AS sid,
 MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) AS sprintFinishTime
 FROM Sprint AS s
 INNER JOIN Task AS t ON t.sprintId = s.id
 WHERE s.projectId = 1
 GROUP BY sid
) AS temp2 ON temp1.dependsOnSprint = temp2.sid
/* sprintSequenceEstimateFunction: */
CREATE OR REPLACE FUNCTION sprintSequenceEstimateFunction (projectId INT)
RETURNS TABLE ("sprintId" INT, "dependencySprint" INT, "sprintStartTime" TIMESTAMP, "estimateDependencyFinishTime" TIMESTAMP) AS 
$BODY$
SELECT 
temp1.sprintId AS "sprintId",
temp1.dependsOnSprint AS "dependencySprint",
temp1.sprintStartTime AS "sprintStartTime",
temp2.sprintFinishTime AS "estimateDependencyFinishTime"
FROM (
 SELECT 
 s.id AS sprintId,
 s.dependsOn AS dependsOnSprint,
 MIN(t.startTime) AS sprintStartTime
 FROM Sprint AS s
 INNER JOIN Task AS t ON t.sprintId = s.id
 WHERE s.projectId = $1
 GROUP BY s.id
) AS temp1
LEFT JOIN (
 SELECT
 s.id AS sid,
 MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) AS sprintFinishTime
 FROM Sprint AS s
 INNER JOIN Task AS t ON t.sprintId = s.id
 WHERE s.projectId = $1
 GROUP BY sid
) AS temp2 ON temp1.dependsOnSprint = temp2.sid
$BODY$ LANGUAGE SQL;

SELECT 
"sprintId",
"dependencySprint",
"sprintStartTime",
"estimateDependencyFinishTime"
FROM sprintSequenceEstimateFunction(1)
ORDER BY "sprintId"

/* TASK DEPENDENCY SEQUENCE ESTIMATE */
SELECT 
td.taskId AS "taskId",
td.dependencyTaskId AS "dependencyTaskId",
t1.startTime AS "taskStartTime",
MAX(t2.startTime + INTERVAL '1h' * t2.hoursEstimate) AS "dependencyTaskMaxFinishTime"
FROM TaskDependency AS td
INNER JOIN Task AS t1 ON td.taskId = t1.id
INNER JOIN Task AS t2 ON td.dependencyTaskId = t2.id
INNER JOIN Sprint AS s ON t1.sprintId = s.id
WHERE s.projectId = 1
GROUP BY td.taskId, td.dependencyTaskId, t1.startTime
/* taskDependencySequenceFunction: */
CREATE OR REPLACE FUNCTION taskDependencySequenceFunction (projectId INT)
RETURNS TABLE ("taskId" INT, "dependencyTaskId" INT, "taskStartTime" TIMESTAMP, "dependencyTaskMaxFinishTime" TIMESTAMP) AS 
$BODY$
SELECT 
td.taskId AS "taskId",
td.dependencyTaskId AS "dependencyTaskId",
t1.startTime AS "taskStartTime",
MAX(t2.startTime + INTERVAL '1h' * t2.hoursEstimate) AS "dependencyTaskMaxFinishTime"
FROM TaskDependency AS td
INNER JOIN Task AS t1 ON td.taskId = t1.id
INNER JOIN Task AS t2 ON td.dependencyTaskId = t2.id
INNER JOIN Sprint AS s ON t1.sprintId = s.id
WHERE s.projectId = $1
GROUP BY td.taskId, td.dependencyTaskId, t1.startTime
$BODY$ LANGUAGE SQL;

SELECT 
"taskId",
"dependencyTaskId",
"taskStartTime",
"dependencyTaskMaxFinishTime"
FROM taskDependencySequenceFunction(1)
ORDER BY "taskId"

/* SPRINT ON TIME ESTIMATE */
SELECT 
s.id AS "sprintId",
MIN(t.startTime) AS "sprintStartTime",
MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) AS "sprintEstFinishTime"
FROM Sprint AS s
INNER JOIN Task AS t ON t.sprintId = s.id
WHERE s.projectId = 1
GROUP BY s.id
/* sprintOnTimeEstimateFunction */
CREATE OR REPLACE FUNCTION sprintOnTimeEstimateFunction (projectId INT)
RETURNS TABLE ("sprintId" INT, "sprintStartTime" TIMESTAMP, "sprintEstFinishTime" TIMESTAMP) AS 
$BODY$
SELECT 
s.id AS "sprintId",
MIN(t.startTime) AS "sprintStartTime",
MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) AS "sprintEstFinishTime"
FROM Sprint AS s
INNER JOIN Task AS t ON t.sprintId = s.id
WHERE s.projectId = $1
GROUP BY s.id
$BODY$ LANGUAGE SQL;

SELECT 
"sprintId",
"sprintStartTime",
"sprintEstFinishTime"
FROM sprintOnTimeEstimateFunction(1)
ORDER BY "sprintId"

/* PROJECT ON TIME ESTIMATE */
SELECT
p.id AS "projectId",
p.name AS "projectName",
p.startDate AS "projectStart",
p.endDate AS "projectEnd",
MIN(t.startTime) AS "projectTaskStartTime",
MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) AS "projectTaskEstFinishTime"
FROM Sprint AS s
INNER JOIN Project AS p ON p.id = s.projectId
INNER JOIN Task AS t ON t.sprintId = s.id
WHERE s.projectId = 1
GROUP BY p.id
/* projectOnTimeEstimateFunction */
CREATE OR REPLACE FUNCTION projectOnTimeEstimateFunction (projectId INT)
RETURNS TABLE ("projectId" INT, "projectName" TEXT, "projectStart" TIMESTAMP, "projectEnd" TIMESTAMP,
 "projectTaskStartTime" TIMESTAMP, "projectTaskEstFinishTime" TIMESTAMP) AS 
$BODY$
SELECT
p.id AS "projectId",
p.name AS "projectName",
p.startDate AS "projectStart",
p.endDate AS "projectEnd",
MIN(t.startTime) AS "projectTaskStartTime",
MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) AS "projectTaskEstFinishTime"
FROM Sprint AS s
INNER JOIN Project AS p ON p.id = s.projectId
INNER JOIN Task AS t ON t.sprintId = s.id
WHERE s.projectId = $1
GROUP BY p.id
$BODY$ LANGUAGE SQL;

SELECT 
"projectId",
"projectName",
"projectStart",
"projectEnd",
"projectTaskStartTime",
"projectTaskEstFinishTime"
FROM projectOnTimeEstimateFunction(1)


/* TOTAL EMPLOYEE OVERTIMES */
SELECT
temp.employeeId AS "employeeId",
COUNT(*) AS "numOfOvertimes"
FROM (
 SELECT 
 ta.employeeId AS employeeId,
 EXTRACT (WEEK FROM t.startTime) AS weekOfYear,
 SUM(t.hoursEstimate) AS estWorkingHours
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = 1
 GROUP BY ta.employeeId, weekOfYear
 HAVING  SUM(t.hoursEstimate) > 10
) AS temp
GROUP BY "employeeId"
/* totalEmployeeOvertimeFunction: */
CREATE OR REPLACE FUNCTION totalEmployeeOvertimeFunction (projectId INT, overtimeHours INT)
RETURNS TABLE ("employeeId" INT, "numOfOvertimes" INT) AS 
$BODY$
SELECT
temp.employeeId AS "employeeId",
CAST( COUNT(*) AS INT) AS "numOfOvertimes"
FROM (
 SELECT 
 ta.employeeId AS employeeId,
 EXTRACT (WEEK FROM t.startTime) AS weekOfYear,
 SUM(t.hoursEstimate) AS estWorkingHours
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = $1
 GROUP BY ta.employeeId, weekOfYear
 HAVING  SUM(t.hoursEstimate) > $2
) AS temp
GROUP BY "employeeId"
$BODY$ LANGUAGE SQL;

SELECT 
"employeeId",
"numOfOvertimes"
FROM totalEmployeeOvertimeFunction(1, 10)
ORDER BY "employeeId"


/* TOTAL SPRINT OVERTIME */
SELECT
temp.sprintId AS "sprintId",
COUNT(*) AS "numOfOvertimes"
FROM (
 SELECT 
 s.id AS sprintId,
 ta.employeeId AS employeeId,
 EXTRACT (WEEK FROM t.startTime) AS weekOfYear,
 SUM(t.hoursEstimate) AS estWorkingHours
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = 1
 GROUP BY s.id, ta.employeeId, weekOfYear
 HAVING  SUM(t.hoursEstimate) > 10
) AS temp
GROUP BY "sprintId"
/* totalSprintOvertimeFunction: */
CREATE OR REPLACE FUNCTION totalSprintOvertimeFunction (projectId INT, overtimeHours INT)
RETURNS TABLE ("sprintId" INT, "numOfOvertimes" INT) AS 
$BODY$
SELECT
temp.sprintId AS "sprintId",
CAST( COUNT(*) AS INT) AS "numOfOvertimes"
FROM (
 SELECT 
 s.id AS sprintId,
 ta.employeeId AS employeeId,
 EXTRACT (WEEK FROM t.startTime) AS weekOfYear,
 SUM(t.hoursEstimate) AS estWorkingHours
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = $1
 GROUP BY s.id, ta.employeeId, weekOfYear
 HAVING  SUM(t.hoursEstimate) > $2
) AS temp
GROUP BY "sprintId"
$BODY$ LANGUAGE SQL;

SELECT 
"sprintId", 
"numOfOvertimes" 
FROM totalSprintOvertimeFunction(1, 10)
ORDER BY "sprintId"

/* TOTAL PROJECT OVERTIME */
SELECT
COUNT(*) AS "numOfOvertimes"
FROM (
 SELECT 
 ta.employeeId AS employeeId,
 EXTRACT (WEEK FROM t.startTime) AS weekOfYear,
 SUM(t.hoursEstimate) AS estWorkingHours
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = 1
 GROUP BY ta.employeeId, weekOfYear
 HAVING  SUM(t.hoursEstimate) > 7
) AS temp
/* totalProjectOvertimeFunction */
CREATE OR REPLACE FUNCTION totalProjectOvertimeFunction (projectId INT, overtimeHours INT)
RETURNS TABLE ("numOfOvertimes" INT) AS 
$BODY$
SELECT
CAST( COUNT(*) AS INT) AS "numOfOvertimes"
FROM (
 SELECT 
 ta.employeeId AS employeeId,
 EXTRACT (WEEK FROM t.startTime) AS weekOfYear,
 SUM(t.hoursEstimate) AS estWorkingHours
 FROM Task AS t
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 WHERE s.projectId = $1
 GROUP BY ta.employeeId, weekOfYear
 HAVING  SUM(t.hoursEstimate) > $2
) AS temp
$BODY$ LANGUAGE SQL;

SELECT "numOfOvertimes"
FROM totalProjectOvertimeFunction(1, 40)


/* NUM OF TASKS DONE ON TIME */
SELECT 
COUNT(*) AS "numOfTasksDoneOnTime"
FROM (
 SELECT
 t.id,
 FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
 GROUP BY t.id
) AS temp
WHERE temp.hoursDeviation >= 0

/* numOfTaskDoneOnTimeFunction */
CREATE OR REPLACE FUNCTION numOfTaskDoneOnTimeFunction (projectId INT)
RETURNS TABLE ("numOfTasksDoneOnTime" INT) AS 
$BODY$
SELECT 
CAST(COUNT(*) AS INT) AS "numOfTasksDoneOnTime"
FROM (
 SELECT
 t.id,
 FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
 GROUP BY t.id
) AS temp
WHERE temp.hoursDeviation >= 0
$BODY$ LANGUAGE SQL;

SELECT 
"numOfTasksDoneOnTime"
FROM numOfTaskDoneOnTimeFunction(1);


/* NUM OF TASKS DONE WITH DILATION */
SELECT 
COUNT(*) AS "numOfTasksDoneWithDilation"
FROM (
 SELECT
 t.id,
 FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
 GROUP BY t.id
) AS temp
WHERE temp.hoursDeviation < 0

/* numOfTaskDoneWithDilationFunction */
CREATE OR REPLACE FUNCTION numOfTaskDoneWithDilationFunction (projectId INT)
RETURNS TABLE ("numOfTaskDoneWithDilation" INT) AS 
$BODY$
SELECT 
CAST(COUNT(*) AS INT) AS "numOfTasksDoneWithDilation"
FROM (
 SELECT
 t.id,
 FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
 GROUP BY t.id
) AS temp
WHERE temp.hoursDeviation < 0
$BODY$ LANGUAGE SQL;

SELECT 
"numOfTaskDoneWithDilation"
FROM numOfTaskDoneWithDilationFunction(1);


/* NUM OF SPRINTS DONE ON TIME */
SELECT 
COUNT(*) AS "numOfSprintsDoneOnTime"
FROM (
 SELECT
 s.id,
 FLOOR(EXTRACT(EPOCH FROM (
  MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
 GROUP BY s.id
) AS temp
WHERE temp.hoursDeviation >= 0
/* numOfSprintDoneOnTimeFunction */
CREATE OR REPLACE FUNCTION numOfSprintDoneOnTimeFunction (projectId INT)
RETURNS TABLE ("numOfSprintsDoneOnTime" INT) AS 
$BODY$
SELECT 
CAST(COUNT(*) AS INT) AS "numOfSprintsDoneOnTime"
FROM (
 SELECT
 s.id,
 FLOOR(EXTRACT(EPOCH FROM (
  MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
 GROUP BY s.id
) AS temp
WHERE temp.hoursDeviation >= 0
$BODY$ LANGUAGE SQL;

SELECT 
"numOfSprintsDoneOnTime"
FROM numOfSprintDoneOnTimeFunction(1);


/* NUM OF SPRINTS DONE WITH DILATION */
SELECT 
COUNT(*) AS "numOfSprintsDoneWithDilation"
FROM (
 SELECT
 s.id,
 FLOOR(EXTRACT(EPOCH FROM (
  MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
 GROUP BY s.id
) AS temp
WHERE temp.hoursDeviation < 0
/* numOfSprintDoneWithDilationFunction */
CREATE OR REPLACE FUNCTION numOfSprintDoneWithDilationFunction (projectId INT)
RETURNS TABLE ("numOfSprintsDoneWithDilation" INT) AS 
$BODY$
SELECT 
CAST(COUNT(*) AS INT) AS "numOfSprintsDoneWithDilation"
FROM (
 SELECT
 s.id,
 FLOOR(EXTRACT(EPOCH FROM (
  MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS hoursDeviation
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
 GROUP BY s.id
) AS temp
WHERE temp.hoursDeviation < 0
$BODY$ LANGUAGE SQL;

SELECT
"numOfSprintsDoneWithDilation"
FROM numOfSprintDoneWithDilationFunction(1);

/* TASK ACTUAL DURATION */
 SELECT
 t.id AS "taskId",
 FLOOR(EXTRACT(EPOCH FROM (
  MAX(ta.finishTime) - MIN(ta.acceptedTime) ) )  / 3600) AS "taskHoursDuration"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
 GROUP BY t.id
ORDER BY t.id
/* taskActualDurationFunction */
DROP FUNCTION taskActualDurationFunction(INT);
CREATE OR REPLACE FUNCTION taskActualDurationFunction (projectId INT)
RETURNS TABLE ("taskId" INT, "taskHoursDuration" INT) AS 
$BODY$
 SELECT
 t.id AS "taskId",
 CAST( FLOOR(EXTRACT(EPOCH FROM (
  MAX(ta.finishTime) - MIN(ta.acceptedTime) ) )  / 3600) AS INT) AS "taskHoursDuration"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
 GROUP BY t.id
ORDER BY t.id
$BODY$ LANGUAGE SQL;

SELECT 
"taskId",
"taskHoursDuration"
FROM taskActualDurationFunction(1);




/* SPRINT ACTUAL DURATION */
 SELECT
 s.id AS "sprintId",
 FLOOR(EXTRACT(EPOCH FROM (
  MAX(ta.finishTime) - MIN(ta.acceptedTime) ) )  / 3600) AS "sprintHoursActualDuration"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
 GROUP BY s.id
 ORDER BY s.id
/* sprintActualDurationFunction */
CREATE OR REPLACE FUNCTION sprintActualDurationFunction (projectId INT)
RETURNS TABLE ("sprintId" INT, "sprintHoursActualDuration" INT) AS 
$BODY$
 SELECT
 s.id AS "sprintId",
CAST( FLOOR(EXTRACT(EPOCH FROM (
  MAX(ta.finishTime) - MIN(ta.acceptedTime) ) )  / 3600) AS INT) AS "sprintHoursActualDuration"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
 GROUP BY s.id
 ORDER BY s.id
$BODY$ LANGUAGE SQL;

SELECT
"sprintId",
"sprintHoursActualDuration"
FROM sprintActualDurationFunction(1);


/* PROJECT ACTUAL DURATION */
SELECT
 s.projectId AS "projectId",
 FLOOR(EXTRACT(EPOCH FROM (
  MAX(ta.finishTime) - MIN(ta.acceptedTime) ) )  / 3600) AS "projectHoursActualDuration"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
GROUP BY s.projectId
/* projectActualDurationFunction  */
CREATE OR REPLACE FUNCTION projectActualDurationFunction (projectId INT)
RETURNS TABLE ("projectId" INT, "projectHoursActualDuration" INT) AS 
$BODY$
SELECT
 s.projectId AS "projectId",
 CAST( FLOOR(EXTRACT(EPOCH FROM (
  MAX(ta.finishTime) - MIN(ta.acceptedTime) ) )  / 3600) AS INT) AS "projectHoursActualDuration"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
GROUP BY s.projectId
$BODY$ LANGUAGE SQL;

SELECT
"projectId",
"projectHoursActualDuration"
FROM projectActualDurationFunction(1);


/* PROJECT HUMAN HOURS */
SELECT 
temp.projectId AS "projectId",
SUM(temp.hoursactualtime) AS "totalHumanHours"
FROM (
 SELECT 
 ta.id,
 s.projectId AS projectId,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime
 FROM TaskAssignment AS ta
 INNER JOIN Task AS t ON t.id = ta.taskId
 INNER JOIN Sprint AS s ON s.id = t.sprintId
 WHERE s.projectId = 1
) AS temp
GROUP BY temp.projectId
/* projectHumanHoursFunction */
CREATE OR REPLACE FUNCTION projectHumanHoursFunction (projectId INT)
RETURNS TABLE ("projectId" INT, "totalHumanHours" INT) AS 
$BODY$
SELECT 
temp.projectId AS "projectId",
CAST( SUM(temp.hoursactualtime) AS INT) AS "totalHumanHours"
FROM (
 SELECT 
 ta.id,
 s.projectId AS projectId,
 FLOOR(EXTRACT(EPOCH FROM (ta.finishTime - ta.acceptedTime)) / 3600) AS hoursactualtime
 FROM TaskAssignment AS ta
 INNER JOIN Task AS t ON t.id = ta.taskId
 INNER JOIN Sprint AS s ON s.id = t.sprintId
 WHERE s.projectId = $1
) AS temp
GROUP BY temp.projectId
$BODY$ LANGUAGE SQL;

SELECT 
"projectId",
"totalHumanHours"
FROM projectHumanHoursFunction(1);


/* TASK ESTIMATE ACTUAL DEVIATION */
 SELECT
 t.id AS "taskId",
 FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS "taskHoursDeviation"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
 GROUP BY t.id
ORDER BY t.id
/* taskEstimateActualDeviationFunction */
CREATE OR REPLACE FUNCTION taskEstimateActualDeviationFunction (projectId INT)
RETURNS TABLE ("taskId" INT, "taskHoursDeviation" INT) AS 
$BODY$
SELECT
 t.id AS "taskId",
 CAST( FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS INT) AS "taskHoursDeviation"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
 GROUP BY t.id
ORDER BY t.id
$BODY$ LANGUAGE SQL;

SELECT 
"taskId",
"taskHoursDeviation"
FROM taskEstimateActualDeviationFunction(1);


/* SPRINT ESTIMATE ACTUAL DEVIATION */
SELECT
s.id AS "sprintId",
FLOOR(EXTRACT(EPOCH FROM (
 MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS "sprintHoursDeviation"
FROM Task AS t
INNER JOIN Sprint AS s ON t.sprintId = s.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE s.projectId = 1
GROUP BY s.id
ORDER BY s.id
/* sprintEstimateActualDeviationFunction */
CREATE OR REPLACE FUNCTION sprintEstimateActualDeviationFunction (projectId INT)
RETURNS TABLE ("sprintId" INT, "sprintHoursDeviation" INT) AS 
$BODY$
SELECT
s.id AS "sprintId",
CAST( FLOOR(EXTRACT(EPOCH FROM (
 MAX(t.startTime + INTERVAL '1h' * t.hoursEstimate) - MAX(ta.finishTime) ) )  / 3600) AS INT) AS "sprintHoursDeviation"
FROM Task AS t
INNER JOIN Sprint AS s ON t.sprintId = s.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE s.projectId = $1
GROUP BY s.id
ORDER BY s.id
$BODY$ LANGUAGE SQL;

SELECT
"sprintId",
"sprintHoursDeviation"
FROM sprintEstimateActualDeviationFunction(1);


/* EMPLOYEES ACTIVITY AT GIVEN TIME */
SELECT 
ta.employeeId AS "employeeId",
p.id AS "projectId",
p.name AS "projectName",
s.id AS "sprintId",
s.name AS "sprintName",
t.id AS "taskId",
t.name AS "taskName"
FROM Project AS p
INNER JOIN Sprint AS s ON s.projectId = p.id
INNER JOIN Task AS t ON t.sprintId = s.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE ( '2017-01-01 12:0:0' BETWEEN ta.acceptedTime AND ta.finishTime)
ORDER BY "employeeId"
/* employeesActivityAtGivenTimeFunction */
CREATE OR REPLACE FUNCTION employeeActivityAtGivenTimeFunction ("datetime" TIMESTAMP)
RETURNS TABLE ("employeeId" INT, "projectId" INT, "projectName" TEXT, "sprintId" INT, "sprintName" TEXT, "taskId" INT, "taskName" TEXT) AS 
$BODY$
SELECT 
ta.employeeId AS "employeeId",
p.id AS "projectId",
p.name AS "projectName",
s.id AS "sprintId",
s.name AS "sprintName",
t.id AS "taskId",
t.name AS "taskName"
FROM Project AS p
INNER JOIN Sprint AS s ON s.projectId = p.id
INNER JOIN Task AS t ON t.sprintId = s.id
INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
WHERE ( $1 BETWEEN ta.acceptedTime AND ta.finishTime)
ORDER BY "employeeId"
$BODY$ LANGUAGE SQL;

SELECT 
"employeeId",
"projectId",
"projectName",
"sprintId",
"sprintName",
"taskId",
"taskName"
FROM employeeActivityAtGivenTimeFunction('2017-01-01 12:0:0');


/* EMPLOYEE TASK DEVIATION */
 SELECT
 ta.employeeId AS "employeeId",
t.id AS "taskId",
 FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - ta.finishTime ) )  / 3600) AS "taskHoursDeviation"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = 1
ORDER BY ta.employeeId, t.id
/* employeeTaskDeviationFunction */
CREATE OR REPLACE FUNCTION employeeTaskDeviationFunction (projectId INT)
RETURNS TABLE ("employeeId" INT, "taskId" INT, "taskHoursDeviation" INT) AS 
$BODY$
 SELECT
 ta.employeeId AS "employeeId",
t.id AS "taskId",
 CAST( FLOOR(EXTRACT(EPOCH FROM (
  (t.startTime + INTERVAL '1h' * t.hoursEstimate) - ta.finishTime ) )  / 3600) AS INT) AS "taskHoursDeviation"
 FROM Task AS t
 INNER JOIN Sprint AS s ON t.sprintId = s.id
 INNER JOIN TaskAssignment AS ta ON ta.taskId = t.id
 WHERE s.projectId = $1
ORDER BY ta.employeeId, t.id
$BODY$ LANGUAGE SQL;

SELECT 
"employeeId",
"taskId",
"taskHoursDeviation"
FROM employeeTaskDeviationFunction(1);