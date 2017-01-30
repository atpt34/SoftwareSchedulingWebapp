<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<h1> Edit task</h1>
	
	<form method="POST"
			action="${pageContext.request.contextPath}/manager/updateTask">
		id <input type="text" name="id"  value="${task.id }"/> <br>
		sprintId <input type="text" name="sprintId" value="${task.sprintId }"/> <br>
		name <input type="text" name="name" value="${task.name }" /><br>
		hoursEstimate <input type="text" name="hoursEstimate"  value="${task.hoursEstimate }"/> <br>
		startTime <input type="text" name="startTime" value="${task.startTime }"/> <br>
		<input type="submit" value="Update" />
	</form>
	
</body>
</html>