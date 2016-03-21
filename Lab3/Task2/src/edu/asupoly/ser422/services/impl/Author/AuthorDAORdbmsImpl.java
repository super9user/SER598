package edu.asupoly.ser422.services.impl.Author;

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
import edu.asupoly.ser422.services.AuthorDAO;

public class AuthorDAORdbmsImpl implements AuthorDAO {
	
	//create table Authors(id int, first_name varchar(40), last_name varchar(40), book_ids varchar(40), PRIMARY KEY (id));
	
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
	public Author findByPrimaryKey(int authorId) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Author rval = null;
		
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select id, last_name, first_name, book_ids from Authors where id=" + authorId);
			rs.next();
			rval = new Author(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
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
	public List<Author> getAllAuthors() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Author> rval = new ArrayList<Author>();
		
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return new ArrayList<Author>();
			}
			
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select id, last_name, first_name, book_ids from Authors");
			while (rs.next()) {
				rval.add(new Author(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			return new ArrayList<Author>();
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
	public boolean deleteAuthor(Author author) {
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
			stmt.executeUpdate("DELETE FROM Authors WHERE id=" + author.getId());
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
	public Author createAuthor(Author author) {
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
			
			String bookIds = Utility.getInstance().join(",", author.getBookIds());
			String values = __id++ + ", '" + author.getLastName() + "', '" + author.getFirstName() + "', '" +bookIds + "'";
			String query = "INSERT INTO Authors (id, last_name, first_name, book_ids) VALUES (" + values + ")";
			
			int id = stmt.executeUpdate(query);
			author.setId(id);
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
		
		return author;
	}

	@Override
	public Author updateAuthor(Author author) {
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
			String booksIds = Utility.getInstance().join(",", author.getBookIds());
			String query = "UPDATE Authors SET last_name='" + author.getLastName() + "', first_name='" + author.getFirstName() + "', book_ids='"+booksIds+"' where id="+author.getId();

			int id = stmt.executeUpdate(query);
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
		
		return author;
	}

	@Override
	public String getFullName(int authorId) {
		String name = "";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			try {
				Class.forName(__jdbcDriver);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			stmt = conn.createStatement();
			
			String query = "Select first_name, last_name from Authors where id="+authorId;

			rs = stmt.executeQuery(query);
			rs.next();
			
			name = rs.getString(1) + " " + rs.getString(2);
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
		return name;
	}
}
