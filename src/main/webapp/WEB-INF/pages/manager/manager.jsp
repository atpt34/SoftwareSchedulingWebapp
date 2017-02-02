<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>

	<jsp:include page="../_header.jsp" />
	<jsp:include page="../_menu.jsp" />

	<h1> Manager dashboard</h1>
	<p> <a href="${pageContext.request.contextPath}/manager/listProjects" >list manager's projects </a> </p>
	
	<jsp:include page="../_footer.jsp" />
	
</body>
</html>