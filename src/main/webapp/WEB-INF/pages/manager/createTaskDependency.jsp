<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>

	<h2> create task dependency</h2>
	
	<form method="POST"
			action="${pageContext.request.contextPath}/manager/createTaskDependency">
		id <input type="text" name="id"  /> <br>
		taskId <input type="text" name="taskId" /> <br>
		dependencyTaskId <input type="text" name="dependencyTaskId"  /> <br>
		<input type="submit" value="Create" />
	</form>
</body>
</html>