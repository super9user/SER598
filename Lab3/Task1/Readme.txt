README
SER422 Spring 2016 Lab3 => Activity1
Tanmay Patil

Design Decisions:-
- View technology used: JSP
- Data passed to views using: Request object

- Controllers: AuthorController
- Models: Author
- Views: 
author/create.jsp
author/index.jsp
author/delete.jsp
author/_partial.jsp
error.jsp


- ----------------------------------------------
-- DDL Statements for tables
-- ----------------------------------------------

CREATE TABLE "APP"."AUTHORS" ("ID" INTEGER NOT NULL, "LAST_NAME" VARCHAR(256) NOT NULL, "FIRST_NAME" VARCHAR(256) NOT NULL);

-- ----------------------------------------------
-- DDL Statements for keys
-- ----------------------------------------------

-- PRIMARY/UNIQUE
ALTER TABLE "APP"."AUTHORS" ADD CONSTRAINT "SQL140226215953110" PRIMARY KEY ("ID");
