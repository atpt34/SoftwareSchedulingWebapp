<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>

	<h1> Edit Task Dependency here </h1>
	
	<form method="POST"
			action="${pageContext.request.contextPath}/manager/updateTaskDependency">
		id <input type="text" name="id" value="${taskDependency.id }" /> <br>
		taskId <input type="text" name="taskId" value="${taskDependency.taskId }"/> <br>
		dependencyTaskId <input type="text" name="dependencyTaskId"  value="${taskDependency.dependencyTaskId }"/> <br>
		<input type="submit" value="Update" />
	</form>
	
</body>
</html>