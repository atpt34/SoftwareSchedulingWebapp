<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>

	<h2>create task form</h2>
	
	<form method="POST"
			action="${pageContext.request.contextPath}/manager/createTask">
		id <input type="text" name="id"  /> <br>
		sprintId <input type="text" name="sprintId" /> <br>
		name <input type="text" name="name" /><br>
		hoursEstimate <input type="text" name="hoursEstimate"  /> <br>
		startTime <input type="text" name="startTime" /> <br>
		<input type="submit" value="Create" />
	</form>
</body>
</html>