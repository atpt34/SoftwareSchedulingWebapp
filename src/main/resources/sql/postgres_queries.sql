/*
 * date & time functions:
 * 
 * 

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

 * 
 * 
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