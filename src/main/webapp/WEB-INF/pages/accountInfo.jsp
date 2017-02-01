<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>accountInfo</title>
</head>
<body>

	<jsp:include page="_header.jsp"/>
	<jsp:include page="_menu.jsp"/>

	<h2>Welcome ${pageContext.request.userPrincipal.name }</h2>
	
	
	<jsp:include page="_footer.jsp"/>
</body>
</html>