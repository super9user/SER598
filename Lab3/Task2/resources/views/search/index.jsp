<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="edu.asupoly.ser422.models.Author" %>
<%@ page import="edu.asupoly.ser422.models.Book" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Booktown</title>
	</head>
	
	<body>
		<H3>Search</H3>
		<p>
		    <form action="./search" method="get">
		    	Author ID: <input type="number" size="10" name="id"/>
		        <input type='submit' name='action' value='Search' />
		    </form>
		</p>
		<p>
		    <form action="./search" method="get">
		    	Book ISBN: <input type="text" size="12" name="isbn"/>
		        <input type='submit' name='action' value='Search' />
		    </form>
		</p>
		<p>
		    <form action="./search" method="get">
		    	Book Title: <input type="text" size="12" name="title"/>
		        <input type='submit' name='action' value='Search' />
		    </form>
		</p>
		
		<% String type = (String) request.getAttribute("type"); %>
		<% String status = (String) request.getAttribute("status"); %>
		<% if(status.equalsIgnoreCase("error")) { %>
			<h3><%= request.getAttribute("message") %></h3>
		<% } else if(type!=null) { %>
			<H4>Results:</H4>
			<% if(type.equalsIgnoreCase("book")) { %>
				<% Map<Book, String> map = (Map<Book, String>) request.getAttribute("results"); %>
				<% if (map == null || map.size() == 0) { %>
					No books found!!!<br/>
				<% } else { %>
					<table>
					<tr>
						<th> ISBN </td>
						<th> Title </td>
						<th> Publisher </td>
						<th> Year </td>
						<th> Authors </td>
					</tr>
					<% 
					Book nextBook = null;
					String authorName = null;
					for(Entry<Book, String> entry: map.entrySet()){
						nextBook = entry.getKey();
						authorName = entry.getValue();
					%>
					<tr>
						<td> <%= nextBook.getIsbn() %> </td>
						<td> <%= nextBook.getTitle() %> </td>
						<td> <%= nextBook.getPublisher() %> </td>
						<td> <%= nextBook.getYear() %> </td>
						<td> <%= authorName %> </td>
					</tr>
					<% } %>
					</table>
					<br/>
				<% } %>
			<% } else { %>
			
				<% List<Author> authors = (List<Author>) request.getAttribute("results"); %>
				<% if (authors == null || authors.size() == 0) { %>
					Author not found!!!<br/>
				<% } else { %>
					<table>
					<tr>
						<th> First Name </td>
						<th> Last Name </td>
					</tr>
					<% 
					Author nextAuthor = null;
					Iterator<Author> authorIter = authors.iterator();
					while (authorIter.hasNext()) {
						nextAuthor = (Author)authorIter.next();
					%>
					<tr>
						<td> <%= nextAuthor.getFirstName() %> </td>
						<td> <%= nextAuthor.getLastName() %> </td>
					</tr>
					<% } %>
					</table>
					<br/>
				<% } %>
			
			<% } %>
		<% } %>
		
		
	</body>
</html>