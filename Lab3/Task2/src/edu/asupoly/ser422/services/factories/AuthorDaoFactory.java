package edu.asupoly.ser422.services.factories;

import java.util.Properties;
import edu.asupoly.ser422.services.AuthorDAO;
import edu.asupoly.ser422.services.impl.Author.AuthorDAORdbmsImpl;

public class AuthorDaoFactory {
	private static AuthorDAO __authorDAO;
	
	private AuthorDaoFactory() {}
	
	public static AuthorDAO getInstance() {
		if (__authorDAO == null) {
			__authorDAO = new AuthorDAORdbmsImpl();
		}
		return __authorDAO;
	}
	    
	// load initial settings from author.properties
	static {
		try {
			Properties dbProperties = new Properties();
			Class<?> initClass = null;
			dbProperties.load(AuthorDaoFactory.class.getClassLoader().getResourceAsStream("lab3.properties"));
			String serviceImpl = dbProperties.getProperty("authorServiceImpl");
			if (serviceImpl != null) {
				initClass = Class.forName(serviceImpl);
			} else {
				initClass = Class.forName("edu.asupoly.ser422.services.impl.Author.AuthorDAORdbmsImpl");
			}
			__authorDAO = (AuthorDAO) initClass.newInstance();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}
	}
	
}
