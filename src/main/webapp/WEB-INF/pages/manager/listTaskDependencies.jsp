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


	<h2>list task's task dependencies</h2>
	<p><a href="${pageContext.request.contextPath}/manager/createTaskDependency"> create task dependency </a> </p>

	<table border="1" >
		<tr>
			<th>id </th>
			<th>taskId</th>
			<th>dependencyTaskId</th>
			<th>edit</th>
			<th>delete</th>
		</tr>
		<c:forEach items="${listTaskDependencies}" var="taskDep">
			<tr>
				<td>${taskDep.id}</td>
				<td>${taskDep.taskId}</td>
				<td>${taskDep.dependencyTaskId}</td>				
				<td> <a href="${pageContext.request.contextPath}/manager/editTaskDependency?taskDependencyId=${taskDep.id}"> edit </a> </td>
				<td> <a href="${pageContext.request.contextPath}/manager/deleteTaskDependency?taskDependencyId=${taskDep.id}"> delete </a> </td>
			</tr>
		</c:forEach>
	</table>
	
</body>
</html>