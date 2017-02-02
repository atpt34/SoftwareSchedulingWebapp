<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- <div>
	 hello someone / logout 
	| login 
</div> -->

<div style="border: 1px solid #ccc;padding:5px;margin-bottom:20px;">
	<a href="${pageContext.request.contextPath}/index">Home</a>

	<c:if test="${pageContext.request.userPrincipal.name == null }">
		| &nbsp;
		<a href="${pageContext.request.contextPath}/login" >Please, login </a>
	</c:if>


	<c:if test="${pageContext.request.userPrincipal.name != null}">
		| &nbsp;
		<a href="${pageContext.request.contextPath}/accountInfo">Account</a>
		| &nbsp;
		<a href="${pageContext.request.contextPath}/logout">Logout</a>
	</c:if>
</div>