<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>create employee request</title>
</head>
<body>

	<h1> Create Request Here</h1>

	 
	<form method="POST"
			action="${pageContext.request.contextPath}/employee/createRequest">
		id <input type="text" name="id"  /> <br>
		taskId <input type="text" name="taskId" /> <br>
		employeeId <input type="text" name="employeeId"  /> <br>
		hours <input type="text" name="hours" /> <br>
		<input type="submit" value="Create Request" />
	</form>

</body>
</html>