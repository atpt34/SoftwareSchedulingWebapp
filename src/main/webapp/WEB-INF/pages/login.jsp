<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>login</title>
</head>
<body>
	<jsp:include page="_header.jsp"/>
	<jsp:include page="_menu.jsp"/>
	
	<h1>Login</h1>
	<c:if test="${param.error == 'true' }">
		<div style="color:red;margin:10px 0px;">
			Login failed !!!<br/>
			Reason: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	
	<form action="${pageContext.request.contextPath }/security_check" method="POST">
		User: <input type="text" name="username" value=""/> <br>
		Password <input type="password" name="password" /> <br>
		<input type="submit" name="submit" value="Submit" />
	</form>

	<jsp:include page="_footer.jsp"/>
</body>
</html>