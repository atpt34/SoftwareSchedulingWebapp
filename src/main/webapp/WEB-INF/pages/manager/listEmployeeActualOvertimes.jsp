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

	<h2> list employee's actual overtimes: </h2>
	
	<table border="1" >
		<tr>
			<th>employeeId</th>
			<th>weekOfYear</th>
			<th>overtimeHours</th>
		</tr>
		<c:forEach items="${listEmployeeActualOvertimes}" var="empActOvertimes">
			<tr>
				<td>${empActOvertimes.employeeId }</td>
				<td>${empActOvertimes.weekOfYear}</td>
				<td>${empActOvertimes.overtimeHours }</td>				
			</tr>
		</c:forEach>
	</table>
	
</body>
</html>