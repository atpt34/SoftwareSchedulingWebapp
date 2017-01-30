<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Create Project</title>
</head>
<body>

	<h1> Create project </h1>
	<form method="POST"
			action="${pageContext.request.contextPath}/admin/createProject">
		Id  <input type="text" name="id"  /><br>
		Name <input type="text" name="name"  /><br>
		Company <input type="text" name="company"  /><br>
		Customer <input type="text" name="customer"  /><br>
		Start Date <input type="text" name="startDate"  /><br>
		End Date <input type="text" name="endDate" /><br>
		ProjectManagerId <input type="text" name="projectManagerId"  /> <br>
		<input type="submit" value="Create" />
	</form>
</body>
</html>