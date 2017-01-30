<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<h1> Edit Employee Here</h1>
	<!-- 
	<p> id : ${employee.id } </p>
	<p> name : ${employee.name} </p>
	<p> email : ${employee.email } </p>
	<p> managerId : ${employee.managerId } </p>
	<p> button update </p>
	<p> button create </p>
	 -->
	 
	<form method="POST"
			action="${pageContext.request.contextPath}/admin/updateEmployee">
		Id  <input type="text" name="id" value="${employee.id }" /><br>
		Name <input type="text" name="name" value="${employee.name }" /><br>
		Email <input type="text" name="email" value="${employee.email }" /><br>
		ManagerId <input type="text" name="managerId" value="${employee.managerId }" /> <br>
		<input type="submit" value="Update" />
	</form>
	
</body>
</html>