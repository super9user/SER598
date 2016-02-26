README
SER422 Spring 2016 Lab2 => Task2

1) ANT USAGE:-
- Go to the directory where build.xml is located
- Change “tomcat.home” variable in build.properties.
- Run command “ant deploy”.
- Note: This build will get deployed to port1 & port2.

2) CONFIGURATIONS:-
a) HTTPD
- Set “stickysession=JSESSIONID”
- BalanceMember “route” IDs should match “jvmRoute” value inside server.xml file of corresponding Tomcat instance.

b) Tomcat
- CATALINA_HOME variable should be SAME for all instances (the parent directory of all the instances).
- CATALINA_BASE variable should be DIFFERENT for all instances (the unique directory for the particular instance).


3) Design Decisions:-
Part1:
- User is remembered using “Cookies”.
- Cookie expiration = 1hour.
- If new user, then he will be redirected to the multi-page registration form.

Part2:
- Multi-page form uses “HttpSession”.
- Session timeout = 15mins.
- 1 Servlet (PostCodersServlet.java) manages the multi-page form.
- On submit, new cookies will be created.
- Session invalidated on: 1) End of workflow 2) User leaves in middle of workflow.

Part3:
- Error Template displayed using ErrorResponseServlet.java
- 400 (wrong query parameters) can be tested using “get_coders”.
- 404 (page not found) can be tested by writing wrong url.
- 405 (method not allowed) can be tested by sending GET request to “post_coder”.
- Other error codes handled are: 500, 505, 403 and generic “java.lang.Throwable” errors.

General:
- The XML database is relative to the CATALINA_HOME path.
- Hence it is assumed that standard practice for multiple tomcat instances will be followed and CATALINA_HOME variable will be same for all the instances.

4) Servlets List:
- Landing Page = LandingPageServlet.java
- Get Coders = GetCodersServlet.java
- Post Coders = PostCodersServlet.java
- Error Response Page = ErrorResponseServlet.java
