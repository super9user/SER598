<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="edu.asupoly.ser422.models.Author" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Booktown - Author</title>
</head>
<body>
<H3>Delete Book: </H3>
<% String status = (String) request.getAttribute("status"); %>
<% List<Author> authors = (List<Author>) request.getAttribute("authors"); %>

<% if (status.equalsIgnoreCase("ok")) { %>
	<H5>Select Which Authors You Want to Delete ALong with this Book:- </H4>
	<p>
	    <form action="./book" method="get">
	    	<input type="hidden" name="isbn" value="<%= request.getAttribute("isbn") %>"/>
	    	<table>
	    	<tr>
	    		<th>First Name</th>
	    		<th>Last Name</th>
	    		<th>Delete this Author?</th>
	    	</tr>
	    	<% for(Author author: authors) { %>
	    		<tr>
	    			<td> <%= author.getFirstName() %> </td>
	    			<td> <%= author.getLastName() %> </td>
	    			<td> <input type="checkbox" name="author_ids" value="<%= author.getId() %>" /> </td>
	    		</tr>
	    	<% } %>
	    	</table>
	        <input type='submit' name='action' value='ConfirmDelete' />
	    </form>
	    <br/>
	</p>
<% } else { %>
	<%= (String)request.getAttribute("message") %>
	<%@include file="./_partial.jsp" %>
<% } %>

<br/>
<br/>
</body>
</html>