<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="edu.asupoly.ser422.models.Book" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Booktown - Author</title>
</head>
<body>

<% String status = (String) request.getAttribute("status"); %>
<% List<Book> books = (List<Book>) request.getAttribute("books"); %>

<% if (status.equalsIgnoreCase("ok")) { %>
<H3>Delete Books Written by this Author: </H3>
Total books: <%= books.size() %>
	<p>
	    <form action="./author" method="get">
	    	<input type="hidden" name="id" value="<%= request.getAttribute("author_id") %>"/>
	    	<table>
	    	<tr>
	    		<th>Title</th>
	    		<th>Delete this book?</th>
	    	</tr>
	    	<% for(Book book: books) { %>
	    		<tr>
	    			<td> <%= book.getTitle() %> </td>
	    			<td> <input type="checkbox" name="book_ids" value="<%= book.getIsbn() %>" /> </td>
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

<%-- <H3>All Authors</H3>
<% List<Author> authorsList = (List<Author>) request.getAttribute("authorsList"); %>
<% if (authorsList == null || authorsList.size() == 0) { %>
	No authors found!!!<br/>
<% } else { %>
	<table>
	<tr>
		<th> ID </td>
		<th> LastName </td>
		<th> FirstName </td>
	</tr>
	<% 
	Author nextAuthor = null;
	Iterator<Author> authorIter = authorsList.iterator();
	while (authorIter.hasNext()) {
		nextAuthor = (Author)authorIter.next();
	%>
	<tr>
		<td> <%= nextAuthor.getId() %> </td>
		<td> <%= nextAuthor.getLastName() %> </td>
		<td> <%= nextAuthor.getFirstName() %> </td>
	</tr>
	<% } %>
	</table>
	<br/>
<% } %> --%>

<br/>
<br/>
</body>
</html>