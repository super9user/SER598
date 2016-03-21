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

public class AuthorController extends HttpServlet {
	
	AuthorDAO authorDao;
	BookDAO bookDao;
	private Utility utility;
	private Map<String, String> pageViews = new HashMap<String, String>();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		pageViews.put("create", "/author/create.jsp");
		pageViews.put("delete", "/author/delete.jsp");
		pageViews.put("confirmdelete", "/author/confirmdelete.jsp");
		pageViews.put("index", "/author/index.jsp");
		pageViews.put("update", "/author/update.jsp");
		pageViews.put("error", "/error.jsp");
		
		utility = Utility.getInstance();
		
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
	
	private void doAction(String action, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		String status = "", message = "";
		action = action.toLowerCase();
		Author author;
		
		switch (action) {
		
			case "confirmdelete":
				if(req.getParameter("id") != null && !req.getParameter("id").equals("")){
					int authorId = Integer.parseInt(req.getParameter("id"));
					author = authorDao.findByPrimaryKey(authorId);
					if(author!=null){
						List<String> authorBookIds = author.getBookIds();
						if(authorDao.deleteAuthor(author)){
							
							for (String id : authorBookIds) {
								// from all books that author has written, remove reference of authors
								Book book = bookDao.findByPrimaryKey(id);
								if(book!=null){
									book.removeAuthorReference(authorId);
									bookDao.updateBook(book);
								}
							}
							
							int counter = 0;
							if(req.getParameter("book_ids") != null && !req.getParameter("book_ids").equals("")){
								List<String> bookIds = utility.split(",", req.getParameterValues("book_ids"));
								
								// delete all books that need to be deleted
								for (String id : bookIds) {
									Book book = bookDao.findByPrimaryKey(id);
									List<String> bookAuthorIds = book.getAuthorIds();
									if(book!=null && (bookDao.deleteBook(book))){
										counter++;
										// from all authors associated with that book. remove reference of book
										for (String aId : bookAuthorIds) {
											Author a = authorDao.findByPrimaryKey(Integer.parseInt(aId));
											if(a!=null){
												a.removeBookReference(id);
												authorDao.updateAuthor(a);
											}
										}
									}
								}
							}
							status = "OK";
							message = "Author with ID: " + authorId + " has been deleted. <br />";
							message += "Total Books deleted: " + counter;
						}else{
							status = "ERROR";
							message = "Could not delete author with id " + authorId;
						}
					}else{
						status = "ERROR";
						message = "Author with ID " + authorId + " not found.";
					}
				}else{
					status = "ERROR";
					message = "Author ID cannot be null";
				}
				break;
		
			case "delete":
				if(req.getParameter("id") != null && !req.getParameter("id").equals("")){
					int authorId = Integer.parseInt(req.getParameter("id"));
					author = authorDao.findByPrimaryKey(authorId);
					if(author!=null){
						status = "OK";
						req.setAttribute("author_id", authorId);
						List<Book> books = new ArrayList<Book>();
						for (String bookId: author.getBookIds()) {
							Book book = bookDao.findByPrimaryKey(bookId);
							if(book!=null){
								books.add(book);
							}
						}
						req.setAttribute("books", books);
					}else{
						status = "ERROR";
						message = "Author with ID " + authorId + " not found.";
					}
				}else{
					status = "ERROR";
					message = "Author ID cannot be null";
				}
				break;
			
			case "create":
				List<Author> authorsList = authorDao.getAllAuthors();
				author = new Author();
				String lname = req.getParameter("lastname");
				String fname = req.getParameter("firstname");
				if (lname != null && fname != null && lname.length() > 0 && fname.length() > 0) {
					author.setFirstName(fname);
					author.setLastName(lname);
					if(isAuthorNew(author, authorsList)){
						author = authorDao.createAuthor(author);
						if (author != null) {
							status = "OK";
							message = "Created new author";
						} else {
							status = "ERROR";
							message = "ERROR: Unable to create an author!";
						}
					}else{
						status = "ERROR";
						message = "ERROR: Author already exists!";
					}
				} else {
					status = "ERROR";
					message = "Cannot create an author with an empty first or last name";
				}
				break;
				
			case "index":
				// no need to do anything here
				break;
				
			case "update":
				if(req.getParameter("id") != null && !req.getParameter("id").equals("")){
					int authorId = Integer.parseInt(req.getParameter("id"));
					author = authorDao.findByPrimaryKey(authorId);
					if(author!=null){
						String lName = req.getParameter("lastname");
						String fName = req.getParameter("firstname");
						if (lName != null && fName != null && lName.length() > 0 && fName.length() > 0) {
							author.setFirstName(fName);
							author.setLastName(lName);
							author = authorDao.updateAuthor(author);
							if(author!=null){
								status = "OK";
								message = "Author updated!";
							}else{
								status = "ERROR";
								message = "ERROR: Unable to update author!";
							}
						} else {
							status = "ERROR";
							message = "First Name & Last Name cannot be null";
						}
					}else{
						status = "ERROR";
						message = "Author with ID " + authorId + " not found.";
					}
				}else{
					status = "ERROR";
					message = "Author ID cannot be null";
				}
				break;
				
			default:
				action = "error";
				break;
				
		}
		
		List<Author> authorsList = authorDao.getAllAuthors();
		req.setAttribute("authorsList", authorsList);
		req.setAttribute("status", status);
		req.setAttribute("message", message);

		// Forward request to JSP
		RequestDispatcher reqDispatcher = req.getRequestDispatcher("/views" + pageViews.get(action));
		reqDispatcher.forward(req, res);
	}
	
	private Boolean isAuthorNew(Author author, List<Author> list){
		Boolean flag = true;
		String fName = author.getFirstName();
		String lName = author.getLastName();
		for (Author a : list) {
			if(a.getFirstName().equalsIgnoreCase(fName) && a.getLastName().equalsIgnoreCase(lName)){
				flag = false;
				break;
			}
		}
		return flag;
	}

}
