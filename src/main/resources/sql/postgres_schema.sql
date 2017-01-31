create table Employee (
id serial primary key,
name VARCHAR(36) not null,
email VARCHAR(36) not null,
managerId int
) ;

create table Project (
id serial primary key,
name VARCHAR(36) not null,
company VARCHAR(36) not null,
customer VARCHAR(36) not null,
startDate timestamp not null,
endDate timestamp not null,
projectManagerId int references Employee(id) not null
) ;

create table Sprint (
id serial primary key,
name varchar(36) not null,
dependsOn int,
projectId int references Project(id) not null
);

create table Task (
id serial primary key,
name varchar(36) not null,
sprintId int references Sprint(id) not null,
hoursEstimate int not null,
startTime timestamp not null
);

create table TaskDependency(
id serial primary key,
taskId int references Task(id) not null,
dependencyTaskId int references Task(id) not null
);

create table EmployeeRequest (
id serial primary key,
taskId int references Task(id) not null,
employeeId int references Employee(id) not null,
hours int not null
);

create table TaskAssignment (
id serial primary key,
taskId int references Task(id) not null,
employeeId int references Employee(id) not null,
acceptedTime timestamp,
finishTime timestamp
);

/* notice:
 * automatically added sequences
 * and foreign key constraints
 * 
 * */

/* some data: */
insert into Employee (name, email, managerId) 
values ( 'employee1', 'employee1@gmail.com', null);
insert into Employee ( name, email, managerId) 
values ( 'employee2', 'employee2@gmail.com', 1),
 ( 'employee3', 'employee3@gmail.com', 1),
 ( 'employee4', 'employee4@gmail.com', 1),
 ( 'employee5', 'employee5@gmail.com', 1),
 ( 'employee6', 'employee6@gmail.com', 1),
 ( 'employee7', 'employee7@gmail.com', 1),
 ( 'employee8', 'employee8@gmail.com', 1),
 ( 'employee9', 'employee9@gmail.com', 1),
 ( 'employee10', 'employee10@gmail.com', 1);