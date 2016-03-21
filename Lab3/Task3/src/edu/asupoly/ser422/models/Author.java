package edu.asupoly.ser422.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import edu.asupoly.ser422.Helpers.Utility;

public class Author {
	private int id;
	private String lastName;
	private String firstName;
	private List<String> bookIds = new ArrayList<String>();
	
	public Author(){}
	
	public Author(int id, String lastName, String firstName, String bookIds) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.bookIds = Utility.getInstance().split(",", bookIds);
	}
	
	public static List<String> getAttributeNames(){
		List<String> list = new ArrayList<String>();
		for (Field f : Author.class.getDeclaredFields()) {
			list.add(f.getName());
		}
		return list;
	}
	
	public void removeBookReference(String bookId){
		this.bookIds = Utility.getInstance().removeFromList(this.bookIds, bookId);
	}
	
	public void addBookReference(String bookId){
		if(!this.bookIds.contains(bookId)){
			this.bookIds.add(bookId);
		}
	}
	
	public List<String> getBookIds() {
		return bookIds;
	}

	public void setBookIds(List<String> bookIds) {
		this.bookIds = bookIds;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
}
