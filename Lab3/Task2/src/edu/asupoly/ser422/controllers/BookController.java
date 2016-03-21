package edu.asupoly.ser422.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.asupoly.ser422.Utility;
import edu.asupoly.ser422.models.Author;
import edu.asupoly.ser422.models.Book;
import edu.asupoly.ser422.services.AuthorDAO;
import edu.asupoly.ser422.services.BookDAO;
import edu.asupoly.ser422.services.factories.AuthorDaoFactory;
import edu.asupoly.ser422.services.factories.BookDaoFactory;

public class BookController extends HttpServlet{

	AuthorDAO authorDao;
	BookDAO bookDao;
	private Utility utility;
	private Map<String, String> pageViews = new HashMap<String, String>();
	private List<String> attributeNames = new ArrayList<String>();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		pageViews.put("create", "/book/create.jsp");
		pageViews.put("delete", "/book/delete.jsp");
		pageViews.put("confirmdelete", "/book/confirmdelete.jsp");
		pageViews.put("index", "/book/index.jsp");
		pageViews.put("update", "/book/update.jsp");
		pageViews.put("updateauthors", "/book/updateauthors.jsp");
		pageViews.put("error", "/error.jsp");
		
		utility = Utility.getInstance();
		attributeNames = Book.getAttributeNames();
		
		try{
			authorDao = AuthorDaoFactory.getInstance();
			bookDao = BookDaoFactory.getInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// Get the user "command"
		String action = req.getParameter("action");
		if(action==null){
			action = "index";
		}
		
		// Perform Action
		doAction(action, req, res);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void doAction(String action, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		String status = "", message = "";
		action = action.toLowerCase();
		Book book;
		Map<String, String> params;
		String isbn;
		
		switch (action) {
		
			case "updateauthors":
				isbn = req.getParameter("isbn");
				book = bookDao.findByPrimaryKey(isbn);
				// remove book reference from authors which have been deSelected.
				if(book!=null){
					List<String> newAuthorIds = utility.split(",", req.getParameterValues("author_ids"));
					List<String> deSelectedAuthorId = utility.difference(book.getAuthorIds(), newAuthorIds);
					book.setAuthorIds(newAuthorIds);
					book = bookDao.updateBook(book);
					if(book!=null){
						newAuthorIds = book.getAuthorIds();
						for (String aId : newAuthorIds) {
							Author author = authorDao.findByPrimaryKey(Integer.parseInt(aId));
							if(author!=null){
								author.addBookReference(isbn);
								authorDao.updateAuthor(author);
							}
						}
						
						for (String aId : deSelectedAuthorId) {
							Author author = authorDao.findByPrimaryKey(Integer.parseInt(aId));
							if(author!=null){
								author.removeBookReference(isbn);
								authorDao.updateAuthor(author);
							}
						}
						
						status = "OK";
						message = "Authors successfully added to book.";
					}else{
						status = "ERROR";
						message = "ERROR: Unable to add authors to book !";
					}
				}else{
					status = "ERROR";
					message = "ERROR: Book with ISBN " + isbn + " not found.";
				}
				break;
				
			case "index":
				// no need to do anything here
				break;
				
			case "create":
				List<Book> booksList = bookDao.getAllBooks();
				
				params = getParams(req);
				if (isValidArguments(params)) {
					book = new Book(params.get("isbn"), params.get("publisher"), params.get("title"), Integer.parseInt(params.get("year")), null);
					if(isBookNew(book, booksList)){
						book = bookDao.createBook(book);
						if (book != null) {
							req.setAttribute("isbn", book.getIsbn());
							req.setAttribute("allAuthors", authorDao.getAllAuthors());
							req.setAttribute("bookAuthorIds", book.getAuthorIds());
							status = "OK";
							message = "Created new book";
						} else {
							status = "ERROR";
							message = "ERROR: Unable to create book !";
						}
					}else{
						status = "ERROR";
						message = "ERROR: Book already exists!";
					}
				} else {
					status = "ERROR";
					message = "ERROR: Please enter all parameters.";
				}
				break;
				
			case "update":
				params = getParams(req);
				if(isValidArguments(params)){
					isbn = params.get("isbn");
					book = bookDao.findByPrimaryKey(isbn);
					if(book!=null){
						status = "OK";
						book.setPublisher(params.get("publisher"));
						book.setTitle(params.get("title"));
						book.setYear(Integer.parseInt(params.get("year")));
						bookDao.updateBook(book);
						
						req.setAttribute("isbn", isbn);
						req.setAttribute("allAuthors", authorDao.getAllAuthors());
						req.setAttribute("bookAuthorIds", book.getAuthorIds());
						
					}else{
						status = "ERROR";
						message = "Book with ISBN " + isbn + " not found.";
					}
				}else{
					status = "ERROR";
					message = "ERROR: Please enter all parameters.";
				}
				break;
				
			case "delete":
				if(req.getParameter("isbn") != null && !req.getParameter("isbn").equals("")){
					isbn = req.getParameter("isbn");
					book = bookDao.findByPrimaryKey(isbn);
					if(book!=null){
						status = "OK";
						req.setAttribute("book_isbn", isbn);
						List<Author> authors = new ArrayList<Author>();
						for (String authorId: book.getAuthorIds()) {
							Author author = authorDao.findByPrimaryKey(Integer.parseInt(authorId));
							if(author!=null){
								authors.add(author);
							}
						}
						req.setAttribute("authors", authors);
						req.setAttribute("isbn", isbn);
					}else{
						status = "ERROR";
						message = "Book with ISBN " + isbn + " not found.";
					}
				}else{
					status = "ERROR";
					message = "Book ISBN cannot be null";
				}
				break;
				
			case "confirmdelete":
				if(req.getParameter("isbn") != null && !req.getParameter("isbn").equals("")){
					isbn = req.getParameter("isbn");
					book = bookDao.findByPrimaryKey(isbn);
					if(book!=null){
						List<String> bookAuthorIds = book.getAuthorIds();
						if(bookDao.deleteBook(book)){
							
							for (String aId : bookAuthorIds) {
								Author author = authorDao.findByPrimaryKey(Integer.parseInt(aId));
								if(author!=null){
									author.removeBookReference(isbn);
									authorDao.updateAuthor(author);
								}
							}
							
							int counter = 0;
							if(req.getParameter("author_ids") != null && !req.getParameter("author_ids").equals("")){
								List<String> authorIds = utility.split(",", req.getParameterValues("author_ids"));
								
								// delete all authors that need to be deleted
								for (String aId : authorIds) {
									Author author = authorDao.findByPrimaryKey(Integer.parseInt(aId));
									List<String> authorBookIds = author.getBookIds();
									if(author!=null && authorDao.deleteAuthor(author)){
										counter++;
										// from all books associated with that author. remove reference of author
										for (String bId : authorBookIds) {
											Book b = bookDao.findByPrimaryKey(bId);
											if(b!=null){
												b.removeAuthorReference(Integer.parseInt(aId));
												bookDao.updateBook(b);
											}
										}
									}
								}
							}
							status = "OK";
							message = "Book with ISBN: " + isbn + " has been deleted. <br />";
							message += "Total Authors deleted: " + counter;
						}else{
							status = "ERROR";
							message = "Could not delete book with ISBN " + isbn;
						}
					}else{
						status = "ERROR";
						message = "Book with ISBN " + isbn + " not found.";
					}
				}else{
					status = "ERROR";
					message = "Book ISBN cannot be null";
				}
				break;
	
			default:
				action="error";
				break;
		}
		
		List<Book> booksList = bookDao.getAllBooks();
		req.setAttribute("booksList", booksList);
		req.setAttribute("status", status);
		req.setAttribute("message", message);

		// Forward request to JSP
		RequestDispatcher reqDispatcher = req.getRequestDispatcher("/views" + pageViews.get(action));
		reqDispatcher.forward(req, res);
	}
	
	private Map<String, String> getParams(HttpServletRequest req){
		Map<String, String> map = new HashMap<String, String>();
		for (String attr : attributeNames) {
			Object val = req.getParameter(attr);
			if(val!=null){
				map.put(attr, val.toString());
			}
		}
		return map;
	}
	
	private Boolean isValidArguments(Map<String, String> params){
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if(entry.getValue()==null || entry.getValue().length()==0){
				return false;
			}
		}
		return true;
	}
	
	private Boolean isBookNew(Book book, List<Book> list){
		Boolean flag = true;
		String isbn = book.getIsbn();
		String title = book.getTitle();
		String publisher = book.getPublisher();
		for (Book b : list) {
			if(b.getIsbn().equalsIgnoreCase(isbn) || (b.getTitle().equalsIgnoreCase(title) && b.getPublisher().equalsIgnoreCase(publisher)) ){
				flag = false;
				break;
			}
		}
		return flag;
	}
	
}
