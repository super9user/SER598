README
SER422 Spring 2016 Lab3 => Activity2
Tanmay Patil

1) SubActivity1:- DAO Interfaces
a) BookDAO:
findByPrimaryKey(String bookIsbn), getAllBooks(), deleteBook(Book book), createBook(Book book), updateBook(Book book), findBooksByTitle(String title)
 
b) AuthorDAO:
findByPrimaryKey(int authorId), getAllAuthors(), deleteAuthor(Author author), createAuthor(Author author), updateAuthor(Author author), getFullName(int authorId)

2) SubActivity2:- ValueObjects
Author & Book ValueObjects can be found inside ‘Model’ folder.

3) SubActivity 3:- Implemented successfully. 

4) SubActivity 4:- RDBMS Implementations can be found inside ‘services/impl’ folder.

5) SubActivity 5:- Can be tested on URL “/search”

6) SubActivity 6:- Can be tested on URL “/author”

7) SubActivity 7:- Can be tested on URL “/book”

8) Controllers List:-
AuthorController
BookController
SearchController
HomeController

9) Models List:-
Book
Author

10) Views List:-
author/*.jsp
book/*.jsp
home/*.jsp
search/*.jsp


-- ----------------------------------------------
-- DDL Statements for tables
-- ----------------------------------------------

CREATE TABLE "APP"."BOOKS" ("ISBN" VARCHAR(40) NOT NULL, "PUBLISHER" VARCHAR(40), "TITLE" VARCHAR(40), "YEAR" INTEGER, "AUTHOR_IDS" VARCHAR(40));

CREATE TABLE "APP"."AUTHORS" ("ID" INTEGER NOT NULL, "FIRST_NAME" VARCHAR(40), "LAST_NAME" VARCHAR(40), "BOOK_IDS" VARCHAR(40));

-- ----------------------------------------------
-- DDL Statements for keys
-- ----------------------------------------------

-- PRIMARY/UNIQUE
ALTER TABLE "APP"."AUTHORS" ADD CONSTRAINT "SQL160229135035720" PRIMARY KEY ("ID");

ALTER TABLE "APP"."BOOKS" ADD CONSTRAINT "SQL160229135017970" PRIMARY KEY ("ISBN");



