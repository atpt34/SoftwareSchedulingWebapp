<%@page session="false" %>
<html>
<body>

	<jsp:include page="_header.jsp" />
	<jsp:include page="_menu.jsp" />

	<h2>This is the home page of my website</h2>
	
	

	
	<p> <a href="${pageContext.request.contextPath}/admin" >goto admin home page </a> </p>
	<p> <a href="${pageContext.request.contextPath}/manager" >goto manager home page </a> </p>
	<p> <a href="${pageContext.request.contextPath}/employee" >goto employee home page </a> </p>
	
	<jsp:include page="_footer.jsp" />
	
</body>
</html>
