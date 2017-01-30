<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>


	<h2>list sprint's tasks</h2>
	
	<p> <a href="${pageContext.request.contextPath}/manager/createTask" >create new task </a> </p>
	<p> <a href="${pageContext.request.contextPath}/manager/listTaskDependencies" >list task dependencies </a> </p>

	<table border="1" >
		<tr>
			<th>id </th>
			<th>sprintId</th>
			<th>name</th>
			<th>startTime</th>
			<th>hoursEstimate</th>
			<th>edit</th>
			<th>delete</th>
			<th>view TaskAssignments</th>
		</tr>
		<c:forEach items="${listTasks}" var="task">
			<tr>
				<td>${task.id}</td>
				<td>${task.sprintId }</td>
				<td>${task.name}</td>
				<td>${task.startTime}</td>
				<td>${task.hoursEstimate}</td>
				<td> <a href="${pageContext.request.contextPath}/manager/editTask?taskId=${task.id}"> edit </a> </td>
				<td> <a href="${pageContext.request.contextPath}/manager/deleteTask?taskId=${task.id}"> delete </a> </td>
				<td> <a href="${pageContext.request.contextPath}/manager/listTaskAssignments?taskId=${task.id}">view </a> </td>
			</tr>
		</c:forEach>
	</table>
		
</body>
</html>