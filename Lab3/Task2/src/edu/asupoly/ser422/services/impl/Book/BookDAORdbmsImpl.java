package edu.asupoly.ser422.services.impl.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.asupoly.ser422.Utility;
import edu.asupoly.ser422.models.Author;
import edu.asupoly.ser422.models.Book;
import edu.asupoly.ser422.services.BookDAO;
import edu.asupoly.ser422.services.impl.Author.AuthorDAORdbmsImpl;

//create table Books(isbn varchar(40), publisher varchar(40), title varchar(40), "YEAR" int, author_ids varchar(40), PRIMARY KEY (isbn));

public class BookDAORdbmsImpl implements BookDAO{

	private static int __id = 1;
	private static String __jdbcUrl;
	private static String __jdbcUser;
	private static String __jdbcPasswd;
	private static String __jdbcDriver;
	
	// load initial settings from rdbm.properties
	static{
		try {
			Properties dbProperties = new Properties();
			dbProperties.load(AuthorDAORdbmsImpl.class.getClassLoader().getResourceAsStream("rdbm.properties"));
			__jdbcUrl    = dbProperties.getProperty("jdbcUrl");
			__jdbcUser   = dbProperties.getProperty("jdbcUser");
			__jdbcPasswd = dbProperties.getProperty("jdbcPasswd");
			__jdbcDriver = dbProperties.getProperty("jdbcDriver");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public Book findByPrimaryKey(String bookIsbn) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Book rval = null;
		
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select isbn, publisher, title, \"YEAR\", author_ids from Books where isbn='" + bookIsbn + "'");
			rs.next();
			rval = new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
		} catch (Exception se) {
			se.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (stmt != null) { 
						stmt.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}
			}
		}
		return rval;
	}

	@Override
	public List<Book> getAllBooks() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Book> rval = new ArrayList<Book>();
		
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return new ArrayList<Book>();
			}
			
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select isbn, publisher, title, \"YEAR\", author_ids from Books");
			while (rs.next()) {
				rval.add(new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			return new ArrayList<Book>();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (stmt != null) { 
						stmt.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}
			}
		}
		return rval;
	}

	@Override
	public boolean deleteBook(Book book) {
		Boolean success = false;
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return false;
			}
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM Books WHERE isbn='" + book.getIsbn() + "'");
			success = true;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			try {
				if (stmt != null) { 
					stmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		}
		return success;
	}

	@Override
	public Book createBook(Book book) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			
			String authorIds = Utility.getInstance().join(",", book.getAuthorIds());
			String values = "'"+ book.getIsbn() + "', '" + book.getPublisher() + "', '" + book.getTitle() + "', " + book.getYear() + ", '" +authorIds + "'";
			String query = "INSERT INTO Books (isbn, publisher, title, \"YEAR\", author_ids) VALUES (" + values + ")";

			stmt.execute(query);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null) { 
					stmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		}
		
		return book;
	}

	@Override
	public Book updateBook(Book book) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();

			String authorIds = Utility.getInstance().join(",", book.getAuthorIds());
			String query = "UPDATE Books SET publisher='" + book.getPublisher() + "', title='"+book.getTitle() + "', \"YEAR\"="+book.getYear() + ", author_ids='"+authorIds + "' where isbn='"+book.getIsbn() + "'";

			stmt.executeUpdate(query);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null) { 
					stmt.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		}
		return book;
	}

	@Override
	public List<Book> findBooksByTitle(String title) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Book> rval = new ArrayList<Book>();;
		
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return new ArrayList<Book>();
			}
			
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select isbn, publisher, title, \"YEAR\", author_ids from Books where title LIKE '%" + title + "%'");
			
			while (rs.next()) {
				rval.add(new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)));
			}
		} catch (Exception se) {
			se.printStackTrace();
			return new ArrayList<Book>();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (stmt != null) { 
						stmt.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				} finally {
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}
			}
		}
		return rval;
	}

}
