package edu.asupoly.ser422.services.factories;

import java.util.Properties;
import edu.asupoly.ser422.services.BookDAO;
import edu.asupoly.ser422.services.impl.Book.BookDAORdbmsImpl;

public class BookDaoFactory {
	private static BookDAO __bookDAO;
	
	private BookDaoFactory() {}
	
	public static BookDAO getInstance() {
		if (__bookDAO == null) {
			__bookDAO = new BookDAORdbmsImpl();
		}
		return __bookDAO;
	}
	    
	// load initial settings from book.properties
	static {
		try {
			Properties dbProperties = new Properties();
			Class<?> initClass = null;
			dbProperties.load(AuthorDaoFactory.class.getClassLoader().getResourceAsStream("lab3.properties"));
			String serviceImpl = dbProperties.getProperty("bookServiceImpl");
			if (serviceImpl != null) {
				initClass = Class.forName(serviceImpl);
			} else {
				initClass = Class.forName("edu.asupoly.ser422.services.impl.Book.BookDAORdbmsImpl");
			}
			__bookDAO = (BookDAO) initClass.newInstance();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}
	}
	
}
