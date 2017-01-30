<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>creating employee ... </title>
</head>
<body>
	<h1> Create Employee Here</h1>

	 
	<form method="POST"
			action="${pageContext.request.contextPath}/admin/createEmployee">
		id <input type="text" name="id"  /> <br>
		name <input type="text" name="name" /> <br>
		email <input type="text" name="email"  /> <br>
		managerId <input type="text" name="managerId" /> <br>
		<input type="submit" value="Create" />
	</form>
	
</body>
</html>