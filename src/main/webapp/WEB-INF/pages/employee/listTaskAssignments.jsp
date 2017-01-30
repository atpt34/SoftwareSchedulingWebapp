<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Employee task assignments</title>
</head>
<body>

	<jsp:include page="../_header.jsp" />
	<jsp:include page="../_menu.jsp" />

	<h1> list task assignments </h1>
	<!-- no such option for employee  
	<h3> <a href="${pageContext.request.contextPath}/emp/createProject" >add new project </a> </h3> 
	 -->
	<table border="1" >
		<tr>

			<th>id </th>
			<th>taskId</th>
			<th>employeeId</th>
			<th>acceptedTime</th>	
			<th>finishTime</th>
						
			<th>edit</th>
			<!-- 
			<th>delete</th>
 			-->
		</tr>

		<c:forEach items="${taskAssigns}" var="taskAssign">
			<tr>
				<td>${taskAssign.id}</td>

				<td>${taskAssign.taskId}</td>
				<td>${taskAssign.employeeId}</td>
				<td>${taskAssign.acceptedTime}</td>
				<td>${taskAssign.finishTime}</td>
							
				
				<td> <a href="${pageContext.request.contextPath}/employee/editTaskAssignment?taskAssignmentId=${taskAssign.id}"> edit </a> </td>
				<!-- no such option for employee
				<td> <a href="${pageContext.request.contextPath}/admin/deleteProject?projectId=${proj.id}"> delete </a> </td>
				 -->
			</tr>
		</c:forEach>
	</table>
	
	<jsp:include page="../_footer.jsp" />
	
</body>
</html>