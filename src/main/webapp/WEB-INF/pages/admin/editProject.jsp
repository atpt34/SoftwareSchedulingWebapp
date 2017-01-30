<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Project edit</title>
</head>
<body>
	<h1> Edit Project </h1>
	
	<form method="POST"
			action="${pageContext.request.contextPath}/admin/updateProject">
		Id  <input type="text" name="id" value="${project.id }" /><br>
		Name <input type="text" name="name" value="${project.name }" /><br>
		Company <input type="text" name="company" value="${project.company }" /><br>
		Customer <input type="text" name="customer" value="${project.customer }" /><br>
		Start Date <input type="text" name="startDate" value="${project.startDate }" /><br>
		End Date <input type="text" name="endDate" value="${project.endDate }" /><br>
		ProjectManagerId <input type="text" name="projectManagerId" value="${project.projectManagerId }" /> <br>
		<input type="submit" value="Update" />
	</form>
	
</body>
</html>