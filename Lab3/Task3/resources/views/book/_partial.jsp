<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="edu.asupoly.ser422.models.Book" %>
<p>
    <form action="book" method="get">
    	ISBN: <input type="text" size="10" name="isbn"/><br/>
        Title: <input type="text" size="13" name="title"/><br/>
        Publisher: <input type="text" size="12" name="publisher"/><br/>
        Year: <input type="number" size="4" name="year"/><br/>
        <input type='submit' name='action' value='Create' />
        <input type='submit' name='action' value='Update' />
        <input type='submit' name='action' value='Delete' />
    </form>
    <br/>
</p>

<H3>All Books</H3>
<% List<Book> booksList = (List<Book>) request.getAttribute("booksList"); %>
<% if (booksList == null || booksList.size() == 0) { %>
	No books found!!!<br/>
<% } else { %>
	<table>
	<tr>
		<th> ISBN </td>
		<th> Title </td>
		<th> Publisher </td>
		<th> Year </td>
	</tr>
	<% 
	Book nextBook = null;
	Iterator<Book> authorIter = booksList.iterator();
	while (authorIter.hasNext()) {
		nextBook = (Book)authorIter.next();
	%>
	<tr>
		<td> <%= nextBook.getIsbn() %> </td>
		<td> <%= nextBook.getTitle() %> </td>
		<td> <%= nextBook.getPublisher() %> </td>
		<td> <%= nextBook.getYear() %> </td>
	</tr>
	<% } %>
	</table>
	<br/>
<% } %>

