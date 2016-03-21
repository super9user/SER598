package edu.asupoly.ser422.services.impl.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.w3c.dom.Document;
import edu.asupoly.ser422.Helpers.Utility;
import edu.asupoly.ser422.Helpers.XmlWrapper;
import edu.asupoly.ser422.models.Book;
import edu.asupoly.ser422.services.BookDAO;
import edu.asupoly.ser422.services.impl.Author.AuthorDAORdbmsImpl;

public class BookDAOXMLImpl implements BookDAO{

	
	private static String __fileName;
	private static XmlWrapper __wrapper;
	
	// load initial settings
	static{
		try {
			Properties dbProperties = new Properties();
			dbProperties.load(AuthorDAORdbmsImpl.class.getClassLoader().getResourceAsStream("xml.properties"));
			
			String contextPath = System.getProperty("catalina.home");
			String relativePath = dbProperties.getProperty("relativePath");
			__fileName = contextPath + "/" + relativePath + "_book.xml";
			__wrapper = new XmlWrapper(__fileName, "book", Book.getAttributeNames());
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public Book findByPrimaryKey(String bookIsbn) {
		Document doc = __wrapper.getDocument();
		try{
			Map<String, String> map = __wrapper.findEntry(doc, "isbn", bookIsbn+"");
			if(map==null){
				return null;
			}else{
				Book book = new Book(map.get("isbn"), map.get("publisher"), map.get("title"), Integer.parseInt(map.get("year")), map.get("authorIds"));
				return book;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Book> getAllBooks() {
		List<Book> rval = new ArrayList<Book>();
		try{
			Document doc = __wrapper.getDocument();
			List<Map<String, String>> allBooks = __wrapper.getAllEntries(doc);
			for (Map<String, String> map : allBooks) {
				Book book = new Book(map.get("isbn"), map.get("publisher"), map.get("title"), Integer.parseInt(map.get("year")), map.get("authorIds"));
				rval.add(book);
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<Book>();
		}
		return rval;
	}

	@Override
	public boolean deleteBook(Book book) {
		Document original = __wrapper.getDocument();
		Document doc = __wrapper.getDocument();
		try{
			doc = __wrapper.deleteEntry(doc, "isbn", book.getIsbn());
			if(doc==null){
				return false;
			}else{
				__wrapper.commit(doc);
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			__wrapper.rollback(original);
			return false;
		}
	}

	@Override
	public Book createBook(Book book) {
		Document original = __wrapper.getDocument();
		Document doc = __wrapper.getDocument();
		
		try{
			Map<String, String> values = new HashMap<String, String>();
			values.put("isbn", book.getIsbn());
			values.put("publisher", book.getPublisher());
			values.put("title", book.getTitle());
			values.put("year", book.getYear()+"");
			values.put("authorIds", Utility.getInstance().join(",", book.getAuthorIds()));
			
			doc = __wrapper.createEntry(doc, values);
			__wrapper.commit(doc);
		}catch(Exception e){
			e.printStackTrace();
			__wrapper.rollback(original);
			return null;
		}
		
		return book;
	}

	@Override
	public Book updateBook(Book book) {
		Document original = __wrapper.getDocument();
		Document doc = __wrapper.getDocument();
		
		try{
			Map<String, String> values = new HashMap<String, String>();
			values.put("isbn", book.getIsbn());
			values.put("publisher", book.getPublisher());
			values.put("title", book.getTitle());
			values.put("year", book.getYear()+"");
			values.put("authorIds", Utility.getInstance().join(",", book.getAuthorIds()));
			
			doc = __wrapper.updateEntry(doc, values, "isbn", book.getIsbn());
			__wrapper.commit(doc);
		}catch(Exception e){
			e.printStackTrace();
			 __wrapper.rollback(original);
			return null;
		}
		return book;
	}

	@Override
	public List<Book> findBooksByTitle(String title) {
		Document doc = __wrapper.getDocument();
		List<Book> books = new ArrayList<Book>();
		try{
			List<Map<String, String>> list = __wrapper.filterEntries(doc, "title", title);
			for (Map<String, String> map : list) {
				Book book = new Book(map.get("isbn"), map.get("publisher"), map.get("title"), Integer.parseInt(map.get("year")), map.get("authorIds"));
				books.add(book);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return books;
	}
	
}
