package edu.asupoly.ser422.services;

import java.util.List;
import edu.asupoly.ser422.models.Author;
import edu.asupoly.ser422.models.Book;

public interface BookDAO {
	
	public Book findByPrimaryKey(String bookIsbn);
	public List<Book> getAllBooks();
    public boolean deleteBook(Book book);
    public Book createBook(Book book);
    public Book updateBook(Book book);
    public List<Book> findBooksByTitle(String title);

}
