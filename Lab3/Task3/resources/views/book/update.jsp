<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="edu.asupoly.ser422.models.Author" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Booktown</title>
		</head>
	<body>
		<H3>Update Book: </H3>
		<% String status = (String) request.getAttribute("status"); %>
		
		<% if (status.equalsIgnoreCase("ok")) { %>
			<%@include file="./_selectAuthors.jsp" %>
		<% } else { %>
			<%= (String)request.getAttribute("message") %>
			<%@include file="./_partial.jsp" %>
		<% } %>
		<br/><br/>
	</body>
</html>