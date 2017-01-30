<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>List All Employees</title>
</head>
<body>

	<jsp:include page="../_header.jsp" />
	<jsp:include page="../_menu.jsp" />

	<fmt:setLocale value="en_US" scope="session"/>
	<h1> List Employees Here </h1>
	<p> <a href="${pageContext.request.contextPath}/admin/createEmployee" >add new employee </a> </p>
	
	<table border="1" >
		<tr>

			<th>id </th>
			<th>name</th>
			<th>email</th>
			<th>managerId</th>
			<th> </th>
			<th> </th>

		</tr>

		<c:forEach items="${listEmployees}" var="emp">
			<tr>
				<td>${emp.id}</td>

				<td>${emp.name}</td>

				<td>${emp.email}</td>
				<td style="color:red;">${emp.managerId} </td>
				<td> <a href="${pageContext.request.contextPath}/admin/editEmployee?employeeId=${emp.id}"> edit </a> </td>
				<td> <a href="${pageContext.request.contextPath}/admin/deleteEmployee?employeeId=${emp.id}"> delete </a> </td>

			</tr>
		</c:forEach>
	</table>
	

	<jsp:include page="../_footer.jsp" />
</body>
</html>