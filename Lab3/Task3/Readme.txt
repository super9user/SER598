README
SER422 Spring 2016 Lab3 => Activity3
Tanmay Patil

1) XML based DAO implementations have been successfully provided for AuthorDAO and BookDAO.
a) AuthorDAOXMLImpl
b) BookDAOXMLImpl

2) You can mix-and-match backing stores for each DAO using the “lab3.properties” file.

3) Transactional safety:-
a) RDBMS implementations:
- Auto Commit has been set to FALSE.
- connection.commit() and connection.rollback() used to manage transaction safety.

b) XML implementations:
- Original Copy of ‘Document’ object has been maintained using cloning techniques.
- If transaction succeeds, the XML file is written with the changed ‘Document’ object.
Hence, replicating the commit() behavior.
- If transaction fails, the XML file is written with the original ‘Document’ object.
Hence, replicating the rollback() behavior.


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