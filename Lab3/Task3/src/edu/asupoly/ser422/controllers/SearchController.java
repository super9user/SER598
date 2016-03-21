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

import edu.asupoly.ser422.models.Author;
import edu.asupoly.ser422.models.Book;
import edu.asupoly.ser422.services.AuthorDAO;
import edu.asupoly.ser422.services.BookDAO;
import edu.asupoly.ser422.services.factories.AuthorDaoFactory;
import edu.asupoly.ser422.services.factories.BookDaoFactory;

public class SearchController extends HttpServlet {
	
	private Map<String, String> pageViews = new HashMap<String, String>();
	AuthorDAO authorDao;
	BookDAO bookDao;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		pageViews.put("index", "/search/index.jsp");
		pageViews.put("/author", "/search/author.jsp");
		pageViews.put("/book", "/search/book.jsp");
		pageViews.put("error", "/error.jsp");
		
		try{
			authorDao = AuthorDaoFactory.getInstance();
			bookDao = BookDaoFactory.getInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String action = "";
		
		if(req.getParameter("id")!=null){
			action = "id";
		}else if(req.getParameter("isbn")!=null){
			action = "isbn";
		}else if(req.getParameter("title")!=null){
			action = "title";
		}else{
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
		
		switch (action) {
		
			case "id":
				action="index";
				List<Author> results = new ArrayList<Author>();
				String id = req.getParameter("id");
				if(id!=null & id.length()>0){
					Author author = authorDao.findByPrimaryKey(Integer.parseInt(id));
					if(author!=null){
						results.add(author);
						status = "OK";
					}else{
						status = "ERROR";
						message = "ERROR: Author not found!";
					}
					req.setAttribute("type", "author");
					req.setAttribute("results", results);
				}else{
					status = "ERROR";
					message = "ERROR: ID cannot be null";
				}
				
				break;
			
			case "isbn":
				action="index";
				Map<Book, String> map = new HashMap<Book, String>();
				String isbn = req.getParameter("isbn");
				if(isbn!=null && isbn.length()>0){
					Book book = bookDao.findByPrimaryKey(isbn);
					if(book!=null){
						String authorNames = "";
						for (String aId : book.getAuthorIds()) {
							authorNames += authorDao.getFullName(Integer.parseInt(aId)) + ", ";
						}
						map.put(book, authorNames);
						status = "OK";
					}else{
						status = "ERROR";
						message = "Book not found!";
					}
					req.setAttribute("type", "book");
					req.setAttribute("results", map);
				}else{
					status = "ERROR";
					message = "ERROR: ISBN cannot be null";
				}
				
				break;
				
			case "title":
				action="index";
				String title = req.getParameter("title");
				List<Book> books = bookDao.findBooksByTitle(title);
				Map<Book, String> map1 = new HashMap<Book, String>();
				for (Book b : books) {
					String authorNames = "";
					for (String aId : b.getAuthorIds()) {
						authorNames += authorDao.getFullName(Integer.parseInt(aId)) + ", ";
					}
					map1.put(b, authorNames);
				}
				req.setAttribute("type", "book");
				req.setAttribute("results", map1);
				break;
				
			case "index":
				List<Book> allBooks = bookDao.getAllBooks();
				Map<Book, String> map2 = new HashMap<Book, String>();
				for (Book b : allBooks) {
					String authorNames = "";
					for (String aId : b.getAuthorIds()) {
						authorNames += authorDao.getFullName(Integer.parseInt(aId)) + ", ";
					}
					map2.put(b, authorNames);
				}
				req.setAttribute("type", "book");
				req.setAttribute("results", map2);
				break;
				
			default:
				action = "error";
				break;
				
		}
		
		req.setAttribute("status", status);
		req.setAttribute("message", message);

		// Forward request to JSP
		RequestDispatcher reqDispatcher = req.getRequestDispatcher("/views" + pageViews.get(action));
		reqDispatcher.forward(req, res);
	}

}
