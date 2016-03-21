<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="edu.asupoly.ser422.models.Author" %>
<p>
    <form action="./author" method="get">
    	ID: <input type="number" size="5" name="id"/><br/>
        Last name: <input type="text" size="13" name="lastname"/><br/>
        First name: <input type="text" size="12" name="firstname"/><br/>
        <input type='submit' name='action' value='Create' />
        <input type='submit' name='action' value='Update' />
        <input type='submit' name='action' value='Delete' />
    </form>
    <br/>
</p>

<H3>All Authors</H3>
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
<% } %>

