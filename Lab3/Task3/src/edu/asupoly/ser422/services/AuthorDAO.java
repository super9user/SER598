package edu.asupoly.ser422.services;

import java.util.List;
import edu.asupoly.ser422.models.Author;

public interface AuthorDAO {
	
	public Author findByPrimaryKey(int authorId);
	public List<Author> getAllAuthors();
    public boolean deleteAuthor(Author author);
    public Author createAuthor(Author author);
    public Author updateAuthor(Author author);
    public String getFullName(int authorId);

}
