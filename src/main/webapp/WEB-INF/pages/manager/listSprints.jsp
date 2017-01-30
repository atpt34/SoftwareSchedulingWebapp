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
	<h2>list project's sprints</h2>
	
	<p> <a href="${pageContext.request.contextPath}/manager/createSprint" >create new sprint</a> </p>
	<p> <a href="${pageContext.request.contextPath}/manager/listRequests" >list employee requests </a> </p>
	<p> <a href="${pageContext.request.contextPath}/manager/projectService" > project Service </a> </p>
	
	<table border="1" >
		<tr>

			<th>id </th>
			<th>name</th>
			<th>projectId</th>
			<th>dependsOn</th>
			<th>edit</th>
			<th>delete</th>
			<th>view Tasks</th>

		</tr>

		<c:forEach items="${listSprints}" var="sprint">
			<tr>
				<td>${sprint.id}</td>

				<td>${sprint.name}</td>



				<td>${sprint.projectId}</td>

				<td>${sprint.dependsOn}</td>
				
				<td> <a href="${pageContext.request.contextPath}/manager/editSprint?sprintId=${sprint.id}"> edit </a> </td>
				<td> <a href="${pageContext.request.contextPath}/manager/deleteSprint?sprintId=${sprint.id}"> delete</a> </td>
				<td> <a href="${pageContext.request.contextPath}/manager/listTasks?sprintId=${sprint.id}">view </a> </td>
				
			</tr>
		</c:forEach>
	</table>
	
</body>
</html>