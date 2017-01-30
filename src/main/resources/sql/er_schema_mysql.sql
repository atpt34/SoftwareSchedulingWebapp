-- Create tables

create table if not exists Employee
(
id Integer not null,
name VARCHAR(36) not null,
email VARCHAR(36) not null,
managerId Integer
) ;

create table if not exists Project
(
id Integer not null,
name VARCHAR(36) not null,
company VARCHAR(36) not null,
customer VARCHAR(36) not null,
startDate datetime not null,
endDate datetime not null,
projectManagerId Integer not null
) ;

create table if not exists Sprint
(
id Integer not null,
projectId Integer not null,
name VARCHAR(36) not null,
dependOn Integer
) ;

create table if not exists Task
(
id Integer not null,
name VARCHAR(36) not null,
startTime datetime not null,
sprintId Integer not null,
estimate Integer not null
) ;

create table if not exists TaskDependency
(
id Integer not null,
taskId Integer not null,
dependOn Integer not null
) ;

create table if not exists TaskAssignment
(
id Integer not null,
taskId Integer not null,
employeeId Integer not null,
accepted bit not null,
done bit not null,
finishTime datetime not null
) ;

create table if not exists Account
(
name VARCHAR(36) not null,
password VARCHAR(36) not null,
active bit not null,
role VARCHAR(36) not null,
employeeId Integer not null
) ;

-- rename columns
alter table Task 
	change estimate hoursEstimate INT;

alter table Sprint 
	change dependOn dependsOn INT;
-- need some operations for TaskDependency table !
alter table TaskDependency 
	drop foreign key DependOn_FK;
	
alter table TaskDependency 
	change dependOn dependencyTaskId INT;

alter table TaskDependency
	add constraint DependencyTaskId_FK
	foreign key (dependencyTaskId)
	references Task(id)
	
-- Create relationships
-- primary keys
alter table Employee
	add constraint Employee_PK primary key (id);

alter table Project
	add constraint Project_PK primary key (id);

alter table Sprint
	add constraint Sprint_PK primary key (id);

alter table Task
	add constraint Task_PK primary key (id);

alter table TaskDependency
	add constraint TaskDependency_PK primary key (id);

alter table TaskAssignment
	add constraint TaskAssignment_PK primary key (id);

-- foreign keys

alter table Project
	add constraint ProjectManagerId_FK
	foreign key (projectManagerId)
	references Employee(id)
	
alter table Sprint
	add constraint ProjectId_FK
	foreign key (projectId)
	references Project(id)

alter table Task
	add constraint SprintId_FK
	foreign key (sprintId)
	references Sprint(id)
	
alter table TaskDependency
	add constraint TaskId_FK
	foreign key (taskId)
	references Task(id)
	
/*
alter table TaskDependency
	add constraint DependOn_FK
	foreign key (dependOn)
	references Task(id)
*/
	
alter table TaskAssignment
	add constraint TaskId2_FK
	foreign key (taskId)
	references Task(id)
	
alter table TaskAssignment
	add constraint EmployeeId_FK
	foreign key (employeeId)
	references Employee(id)
	
/* mysql joins:
  
SELECT * 
FROM `Sprint` AS `s1`
INNER JOIN `Sprint` AS  `s2` ON `s1`.`dependsOn` = `s2`.`id`

SELECT * 
FROM `TaskDependency` 
INNER JOIN `Task` AS  `t1` ON `t1`.`id` = `TaskDependency`.`taskId`
INNER JOIN `Task` AS  `t2` ON `t2`.`id` = `TaskDependency`.`dependencyTaskId`
INNER JOIN `TaskAssignment` AS `ta` ON `t2`.`id` = `ta`.`taskId`

 */

-- dump data
insert into Employee (id, name, email, managerId) 
values (1, 'employee1', 'employee1@gmail.com', null);

insert into Employee (id, name, email, managerId) 
values (2, 'employee2', 'employee2@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (3, 'employee3', 'employee3@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (4, 'employee4', 'employee4@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (5, 'employee5', 'employee5@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (6, 'employee6', 'employee6@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (7, 'employee7', 'employee7@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (8, 'employee8', 'employee8@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (9, 'employee9', 'employee9@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (10, 'employee10', 'employee10@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (11, 'employee11', 'employee11@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (12, 'employee12', 'employee12@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (13, 'employee13', 'employee13@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (14, 'employee14', 'employee14@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (15, 'employee15', 'employee15@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (16, 'employee16', 'employee16@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (17, 'employee17', 'employee17@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (18, 'employee18', 'employee18@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (19, 'employee19', 'employee19@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (20, 'employee20', 'employee20@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (21, 'employee21', 'employee21@gmail.com', 1);
insert into Employee (id, name, email, managerId) 
values (24, 'employee24', 'employee24@gmail.com', 1),
(25, 'emp25', 'emp25@gmail.com', 1),
(26, 'emp26', 'emp26@gmail.com', 1),
(27, 'emp27', 'emp27@gyahoo.com', 1),
(28, 'emp28', 'emp28@mail.com', 1);
insert into Employee (id, name, email, managerId) 
values (200, 'emp200', 'man2@gmail.com', null);
insert into Employee (id, name, email, managerId) 
values (300, 'emp300', 'man3@gmail.com', null);
insert into Employee (id, name, email, managerId) 
values (400, 'emp400', 'man4@gmail.com', null);

update `Employee` set managerId = Null   WHERE id = 1

insert into Project (id, name, company, customer, startDate, endDate, projectManagerId) 
values (1, 'project1', 'company1', 'customer1', '2017-01-01 12-00-00', '2018-01-01 13-00-00', 1);
insert into Project (id, name, company, customer, startDate, endDate, projectManagerId) 
values (2, 'project2', 'company2', 'customer2', '2017-02-02 12-00-00', '2018-02-02 14-00-00', 200);
insert into Project (id, name, company, customer, startDate, endDate, projectManagerId) 
values (3, 'project3', 'company3', 'customer3', '2017-03-03 12-00-00', '2018-03-03 15-00-00', 300);
insert into Project (id, name, company, customer, startDate, endDate, projectManagerId) 
values (4, 'project4', 'company4', 'customer4', '2017-04-04 12-00-00', '2018-04-04 16-00-00', 400);

insert into Sprint (id, projectId, name, dependOn)
values (1, 1, 'sprint1', null);
insert into Sprint (id, projectId, name, dependOn)
values (2, 1, 'sprint2', 1);
insert into Sprint (id, projectId, name, dependOn)
values (3, 1, 'sprint3', 2);
insert into Sprint (id, projectId, name, dependOn)
values (4, 1, 'sprint4', 3);
insert into Sprint (id, projectId, name, dependOn)
values (5, 1, 'sprint5', 4);

insert into Task (id, name, startTime, sprintId, estimate)
values (1, 'task1', '2017-01-01 12-00-00', 1, 48),
 (2, 'task2', '2017-01-03 12-00-00', 1, 24),
 (3, 'task3', '2017-01-04 12-00-00', 1, 72),
 (4, 'task4', '2017-01-08 12-00-00', 1, 24),
 (5, 'task5', '2017-01-10 12-00-00', 2, 24),
 (6, 'task6', '2017-01-11 12-00-00', 2, 24),
 (7, 'task7', '2017-01-12 12-00-00', 2, 48),
 (8, 'task8', '2017-01-13 12-00-00', 2, 24),
 (9, 'task9', '2017-01-15 12-00-00', 3, 48),
 (10, 'task10', '2017-01-17 12-00-00', 3, 24),
 (11, 'task11', '2017-01-18 12-00-00', 4, 72),
 (12, 'task12', '2017-01-21 12-00-00', 4, 24),
 (13, 'task13', '2017-01-22 12-00-00', 4, 24),
 (14, 'task14', '2017-01-23 12-00-00', 4, 24),
 (15, 'task15', '2017-01-24 12-00-00', 4, 24),
 (16, 'task16', '2017-01-25 12-00-00', 4, 24),
 (17, 'task17', '2017-01-26 12-00-00', 5, 24),
 (18, 'task18', '2017-01-27 12-00-00', 5, 24),
 (19, 'task19', '2017-01-28 12-00-00', 5, 24);
 
 insert into TaskDependency(id, taskId, dependOn)
 values (1, 2, 1),
 (2, 4, 1),
 (3, 4, 3),
 (4, 8, 7),
 (5, 7, 5),
 (6, 15, 11),
 (7, 15, 12),
 (8, 19, 17);
 
 insert into TaskAssignment (id, taskId, employeeId, accepted, done, finishTime)
 values (1, 1, 2, 0, 0, '2017-01-01 12-00-00'),
 (2, 2, 3, 0, 0 , '2017-01-03 12-00-00'),
 (3, 3, 4, 0, 0, '2017-01-04 12-00-00'),
 (4, 4, 5, 0, 0, '2017-01-08 12-00-00'),
 (5, 5, 6, 0, 0, '2017-01-10 12-00-00'),
 (6, 6, 7, 0, 0, '2017-01-11 12-00-00'),
 (7, 7, 8, 0, 0, '2017-01-12 12-00-00'),
 (8, 8, 9, 0, 0, '2017-01-13 12-00-00'),
 (9, 9, 10, 0, 0, '2017-01-15 12-00-00'),
 (10, 10, 11, 0, 0, '2017-01-17 12-00-00'),
 (11, 11, 12, 0, 0, '2017-01-18 12-00-00'),
 (12, 12, 13, 0, 0, '2017-01-21 12-00-00'),
 (13, 13, 14, 0, 0, '2017-01-22 12-00-00'),
 (14, 14, 15, 0, 0, '2017-01-23 12-00-00'),
 (15, 15, 16, 0, 0, '2017-01-24 12-00-00'),
 (16, 16, 17, 0, 0, '2017-01-25 12-00-00'),
 (17, 17, 18, 0, 0, '2017-01-26 12-00-00'),
 (18, 18, 19, 0, 0, '2017-01-27 12-00-00'),
 (19, 19, 20, 0, 0, '2017-01-28 12-00-00');
