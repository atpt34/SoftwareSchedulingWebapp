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



	<h2>list task's task assignments </h2>
	
	<p><a href="${pageContext.request.contextPath}/manager/createTaskAssignment"> create task assignment </a></p>
	
	<table border="1" >
		<tr>

			<th>id </th>
			<th>taskId</th>
			<th>employeeId</th>
			<th>acceptedTime</th>
			<th>finishTime</th>
			<th>edit</th>
			<th>delete</th>


		</tr>

		<c:forEach items="${listTaskAssignments}" var="taskAssign">
			<tr>
				<td>${taskAssign.id}</td>

				<td>${taskAssign.taskId }</td>
				<td>${taskAssign.employeeId}</td>



				<td>${taskAssign.acceptedTime}</td>

				<td>${taskAssign.finishTime}</td>
				
				<td> <a href="${pageContext.request.contextPath}/manager/editTaskAssignment?taskAssignmentId=${taskAssign.id}"> edit </a> </td>
				<td> <a href="${pageContext.request.contextPath}/manager/deleteTaskAssignment?taskAssignmentId=${taskAssign.id}"> delete </a> </td>

				
			</tr>
		</c:forEach>
	</table>
</body>
</html>