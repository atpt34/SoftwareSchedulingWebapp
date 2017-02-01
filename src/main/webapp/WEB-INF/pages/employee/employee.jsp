<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="../_header.jsp"/>
	<jsp:include page="../_menu.jsp"/>
	
	<h1> employee dashboard</h1>
	<p> <a href="${pageContext.request.contextPath}/employee/listTaskAssignments" >list all task assignments</a> </p>
	<p> <a href="${pageContext.request.contextPath}/employee/createRequest" >add new request </a> </p>
	
	
	<jsp:include page="../_footer.jsp"/>
</body>
</html>