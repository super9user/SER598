package edu.asupoly.ser422.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.asupoly.ser422.models.Author;
import edu.asupoly.ser422.services.BooktownService;
import edu.asupoly.ser422.services.BooktownServiceFactory;

public class AuthorController extends HttpServlet {
	
	BooktownService bookstore;
	private Map<String, String> pageViews = new HashMap<String, String>();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		pageViews.put("create", "/author/create.jsp");
		pageViews.put("delete", "/author/delete.jsp");
		pageViews.put("index", "/author/index.jsp");
		pageViews.put("error", "/error.jsp");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		bookstore = null;
		try{
			bookstore = BooktownServiceFactory.getInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
		int authorId = -1;
		String status = "", message = "";
		action = action.toLowerCase();
		
		switch (action) {
		
			case "delete":
				authorId = Integer.parseInt(req.getParameter("authorid"));
				if(bookstore.deleteAuthor(authorId)){
					status = "OK";
					message = "Author deleted: " + authorId;
				}else{
					status = "ERROR";
					message = "Could not delete author with id " + authorId;
				}
				break;
			
			case "create":
				String lname = req.getParameter("lastname");
				String fname = req.getParameter("firstname");
				if (lname != null && fname != null && lname.length() > 0 && fname.length() > 0) {
					authorId = bookstore.createAuthor(lname, fname);
					if (authorId > 0) {
						status = "OK";
						message = "Created new author";
					} else {
						status = "ERROR";
						message = "ERROR: Unable to create an author!";
					}
				} else {
					status = "ERROR";
					message = "Cannot create an author with an empty first or last name";
				}
				break;
				
			case "index":
				// no need to do anything here
				break;
				
			default:
				action = "error";
				break;
				
		}
		
		// No matter what the command was, we display the list of authors and a create form
		// Call a service to get the list of authors
		List<Author> authorsList = bookstore.getAuthors();
		
		req.setAttribute("authorsList", authorsList);
		req.setAttribute("status", status);
		req.setAttribute("message", message);

		// Forward request to JSP
		RequestDispatcher reqDispatcher = req.getRequestDispatcher("/views" + pageViews.get(action));
		reqDispatcher.forward(req, res);
	}

}
