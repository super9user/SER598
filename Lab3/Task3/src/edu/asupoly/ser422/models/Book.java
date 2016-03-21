package edu.asupoly.ser422.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import edu.asupoly.ser422.Helpers.Utility;

public class Book {
	
	private String isbn;
	private String publisher;
	private String title;
	private int year;
	private List<String> authorIds = new ArrayList<String>();

	public Book(String isbn, String publisher, String title, int year, String authorIds) {
		this.isbn = isbn;
		this.publisher = publisher;
		this.title = title;
		this.year = year;
		this.authorIds = Utility.getInstance().split(",", authorIds);
	}
	
	public Book(){}
	
	public static List<String> getAttributeNames(){
		List<String> list = new ArrayList<String>();
		for (Field f : Book.class.getDeclaredFields()) {
			list.add(f.getName());
		}
		return list;
	}
	
	public void removeAuthorReference(int authorId){
		this.authorIds = Utility.getInstance().removeFromList(this.authorIds, authorId + "");
	}
	
	public void addAuthorReference(int authorId){
		String id = authorId + "";
		if(!this.authorIds.contains(id)){
			this.authorIds.add(id);
		}
	}
	
	public List<String> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<String> authorIds) {
		this.authorIds = authorIds;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
}
