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

	<h2> list employees requests by project</h2>
	
	<table border="1" >
		<tr>

			<th>id </th>
			<th>taskId</th>
			<th>employeeId</th>
			<th>hours</th>
			<th>delete</th>


		</tr>
		<c:forEach items="${listEmployeeRequests}" var="empReq">
			<tr>
				<td>${empReq.id}</td>
				<td>${empReq.taskId}</td>
				<td>${empReq.employeeId}</td>	
				<td>${empReq.hours }			
				<td> <a href="${pageContext.request.contextPath}/manager/deleteEmployeeRequest?requestId=${empReq.id}"> delete </a> </td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>