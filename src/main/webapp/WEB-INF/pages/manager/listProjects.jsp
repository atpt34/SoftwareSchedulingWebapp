<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>All Manager's Projects </title>
</head>
<body>
	<h1> List Manager's Projects</h1>
	
	<table border="1" >
		<tr>

			<th>id </th>
			<th>name</th>
			<th>company</th>
			<th>customer</th>
			<th>projectManagerId</th>
			<th>startDate</th>
			<th>endDate</th>
			<th>edit</th>
			<th>view Sprints</th>

		</tr>

		<c:forEach items="${listProjects}" var="proj">
			<tr>
				<td>${proj.id}</td>

				<td>${proj.name}</td>

				<td>${proj.company}</td>
				<td>${proj.customer}</td>
				<td>${proj.projectManagerId}</td>
				<td>${proj.startDate}</td>
				<td>${proj.endDate}</td>
				
				<td> <a href="${pageContext.request.contextPath}/manager/editProject?projectId=${proj.id}"> edit </a> </td>
				<td> <a href="${pageContext.request.contextPath}/manager/listSprints?projectId=${proj.id}">view </a> </td>
				
			</tr>
		</c:forEach>
	</table>
</body>
</html>