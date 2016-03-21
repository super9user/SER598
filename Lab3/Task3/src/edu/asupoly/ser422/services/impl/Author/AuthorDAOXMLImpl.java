package edu.asupoly.ser422.services.impl.Author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.asupoly.ser422.Helpers.Utility;
import edu.asupoly.ser422.Helpers.XmlWrapper;
import edu.asupoly.ser422.models.Author;
import edu.asupoly.ser422.services.AuthorDAO;
import org.w3c.dom.Document;

public class AuthorDAOXMLImpl implements AuthorDAO{

	private static int __id = 0;
	private static String __fileName;
	private static XmlWrapper __wrapper;
	
	// load initial settings
	static{
		try {
			Properties dbProperties = new Properties();
			dbProperties.load(AuthorDAORdbmsImpl.class.getClassLoader().getResourceAsStream("xml.properties"));
			
			String contextPath = System.getProperty("catalina.home");
			String relativePath = dbProperties.getProperty("relativePath");
			__fileName = contextPath + "/" + relativePath + "_author.xml";
			__wrapper = new XmlWrapper(__fileName, "author", Author.getAttributeNames());
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public Author findByPrimaryKey(int authorId) {
		Document doc = __wrapper.getDocument();
		try{
			Map<String, String> map = __wrapper.findEntry(doc, "id", authorId+"");
			if(map==null){
				return null;
			}else{
				Author author = new Author(Integer.parseInt(map.get("id")), map.get("lastName"), map.get("firstName"), map.get("bookIds"));
				return author;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Author> getAllAuthors() {
		List<Author> rval = new ArrayList<Author>();
		try{
			Document doc = __wrapper.getDocument();
			List<Map<String, String>> allAuthors = __wrapper.getAllEntries(doc);
			for (Map<String, String> map : allAuthors) {
				Author author = new Author(Integer.parseInt(map.get("id")), map.get("lastName"), map.get("firstName"), map.get("bookIds"));
				rval.add(author);
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<Author>();
		}
		return rval;
	}

	@Override
	public boolean deleteAuthor(Author author) {
		Document original = __wrapper.getDocument();
		Document doc = __wrapper.getDocument();
		try{
			doc = __wrapper.deleteEntry(doc, "id", author.getId()+"");
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
	public Author createAuthor(Author author) {
		Document original = __wrapper.getDocument();
		Document doc = __wrapper.getDocument();
		
		try{
			Map<String, String> values = new HashMap<String, String>();
			__id += 1;
			values.put("id", __id+"");
			values.put("lastName", author.getLastName());
			values.put("firstName", author.getFirstName());
			values.put("bookIds", Utility.getInstance().join(",", author.getBookIds()));
			author.setId(__id);
			doc = __wrapper.createEntry(doc, values);
			__wrapper.commit(doc);
		}catch(Exception e){
			e.printStackTrace();
			__wrapper.rollback(original);
			return null;
		}
		
		return author;
	}

	@Override
	public Author updateAuthor(Author author) {
		Document original = __wrapper.getDocument();
		Document doc = __wrapper.getDocument();
		
		try{
			Map<String, String> values = new HashMap<String, String>();
			values.put("id", author.getId()+"");
			values.put("lastName", author.getLastName());
			values.put("firstName", author.getFirstName());
			values.put("bookIds", Utility.getInstance().join(",", author.getBookIds()));
			doc = __wrapper.updateEntry(doc, values, "id", author.getId()+"");
			__wrapper.commit(doc);
		}catch(Exception e){
			e.printStackTrace();
			 __wrapper.rollback(original);
			return null;
		}
		return author;
	}

	@Override
	public String getFullName(int authorId) {
		Document doc = __wrapper.getDocument();
		try{
			Map<String, String> map = __wrapper.findEntry(doc, "id", authorId+"");
			if(map==null){
				return "";
			}else{
				return (map.get("firstName") + " " + map.get("lastName"));
			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

}
