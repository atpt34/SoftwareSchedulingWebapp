<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Update employee task assignment</title>
</head>
<body>
	<h1> Edit task assignment here</h1>
	
	<form method="POST" 
			action="${pageContext.request.contextPath}/employee/updateTaskAssignment">
		id  <input type="text" name="id" value="${taskAssign.id }" /><br>
		taskId <input type="text" name="taskId" value="${taskAssign.taskId }" /><br>
		employeeId <input type="text" name="employeeId" value="${taskAssign.employeeId }" /><br>
		acceptedTime <input type="text"  name="acceptedTime"  value="${taskAssign.acceptedTime }" /><br>
		finishTime <input type="text" name="finishTime" value="${taskAssign.finishTime }" /><br>
		<input type="submit" value="Update" />
	</form>

</body>
</html>