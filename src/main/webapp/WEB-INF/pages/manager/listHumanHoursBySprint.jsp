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

	<h2>list human-hours by sprint</h2>
	
	<table border="1" >
		<tr>
			<th>sprintId</th>
			<th>totalHumanHours</th>
		</tr>
		<c:forEach items="${listHumanHoursBySprint}" var="humHourSprint">
			<tr>
				<td>${humHourSprint.sprintId }</td>
				<td>${humHourSprint.totalHumanHours}</td>				
			</tr>
		</c:forEach>
	</table>

</body>
</html>