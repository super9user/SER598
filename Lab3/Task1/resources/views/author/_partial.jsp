<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="edu.asupoly.ser422.models.Author" %>

<% List<Author> authorsList = (List<Author>) request.getAttribute("authorsList"); %>
<% if (authorsList == null || authorsList.size() == 0) { %>
	No authors found!!!<br/>
<% } else { %>
	<table>
	<% 
	Author nextAuthor = null;
	Iterator<Author> authorIter = authorsList.iterator();
	while (authorIter.hasNext()) {
		nextAuthor = (Author)authorIter.next();
	%>
	<tr>
		<td> <%= nextAuthor.getLastName() %> </td>
		<td> <%= nextAuthor.getFirstName() %> </td>
		<td> 
			<a href="?action=delete&authorid=<%= nextAuthor.getAuthorID() %>">delete</a>
		</td>
	</tr>
	<% } %>
	</table>
	<br/>
<% } %>

<p>
    <form action="./booktown" method="get">
        <input type="hidden" name="action" value="create"/>
        Last name: <input type="text" size="13" name="lastname"/><br/>
        First name: <input type="text" size="12" name="firstname"/><br/>
        <button type="submit">Create author</button><br/>
    </form>
    <br/>
</p>

