<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>

	<h2>project's services: </h2>
	
	
	<p><a href="${pageContext.request.contextPath}/manager/listEmployeeEstimatedOvertimes?projectId=${projectId}">  list employee's estimated overtimes</a></p>
	<p><a href="${pageContext.request.contextPath}/manager/listHumanHoursBySprint?projectId=${projectId}">  list human-hours by sprint </a></p>
	<p><a href="${pageContext.request.contextPath}/manager/listEmployeeActualOvertimes?projectId=${projectId}">  list actual employee's overtimes</a> </p>
	

</body>
</html>