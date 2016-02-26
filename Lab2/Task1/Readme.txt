README
SER422 Spring 2016 Lab2
Task1

Ant Usage
Go to the directory where build.xml is located
Change “tomcat.home” variable in build.properties.
Run command “ant deploy”.
Note: This build will get deployed to port1 (8080) only.


Design Decisions
Web form created with additional field: experience (integer).
POST request handled by: PostCoderServlet.java
GET request handled by: GetCodersServlet.java
Back Button handled using “referer” header of request object.
Thread safety achieved by synchronizing all writes to XML.
Design of XML:  <root>
	<coder>
     		<first_name> Abc </first_name>
     		<last_name> Abc </last_name>
     		<experience> 20 </experience>
     		<languages><language>python</language></languages>
     		<availabilities><availability>monday</availability></availabilities>
	</coder>
</root>

