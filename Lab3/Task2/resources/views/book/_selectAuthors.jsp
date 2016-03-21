<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="edu.asupoly.ser422.models.Author" %>

<H4>Add Authors to this book</H4>
<% List<Author> allAuthors = (List<Author>) request.getAttribute("allAuthors"); %>
<% List<String> bookAuthorIds = (List<String>) request.getAttribute("bookAuthorIds"); %>
	<p>
	    <form action="./book" method="get">
	    	<input type="hidden" name="isbn" value="<%= request.getAttribute("isbn") %>"/>
	    	<table>
	    	<tr>
	    		<th>First Name</th>
	    		<th>Last Name</th>
	    		<th>Author of this book?</th>
	    	</tr>
	    	<% for(Author author: allAuthors) { %>
	    		<tr>
	    			<td> <%= author.getFirstName() %> </td>
	    			<td> <%= author.getLastName() %> </td>
	    			<td> <input type="checkbox" name="author_ids" value="<%= author.getId() %>" <%= bookAuthorIds.contains(author.getId()+"") ? "checked" : "" %>/> </td>
	    		</tr>
	    	<% } %>
	    	</table>
	        <input type='submit' name='action' value='UpdateAuthors' />
	    </form>
	    <br/>
	</p>
	
	<br/>

