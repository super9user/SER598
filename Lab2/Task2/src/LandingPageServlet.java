import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LandingPageServlet extends javax.servlet.http.HttpServlet{
	
	XMLWrapper wrapper;
	String contextPath;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		contextPath = System.getProperty("catalina.home");
        String xmlName = getServletContext().getInitParameter("xml_name");
        wrapper = new XMLWrapper(contextPath + "/" + xmlName);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		
		String fName = null;
		String lName = null;
		Cookie[] cookies = req.getCookies();
		
		if(isNewUser(cookies)){
			// redirect to user registration page
			redirectToRegistration(resp);
			
			PrintWriter out = resp.getWriter();
			resp.setContentType("text/html");
	    	out.println("<html>");
	    	out.println("<head><title>Landing Page</title></head>");
	    	out.println("<body>");
	    	out.println("<b>Welcome New User!</b><br/>");
	    	out.println("Redirecting now ...");
	    	out.println("</body>");
	    	out.println("</html>");
		}else{
			// returning user
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("fName")){
					fName = cookie.getValue();
				}else if(cookie.getName().equals("lName")){
					lName = cookie.getValue();
				}
			}
			CoderEntry filter = new CoderEntry();
			filter.setFirstName(fName);
			filter.setLastName(lName);
			List<CoderEntry> list = wrapper.getEntries(filter);
			
			List<CoderEntry> entries;
			if(list.size()>0){
				entries = wrapper.getMatchingEntires(list.get(0), 3);
			}else{
				entries = new ArrayList<CoderEntry>();
			}
			
			PrintWriter out = resp.getWriter();
			resp.setContentType("text/html");
	    	out.println("<html>");
	    	out.println("<head><title>Landing Page</title></head>");
	    	out.println("<body>");
	    	out.println("Welcome <b>" + fName + " " + lName + "</b><br/>");
	    	out.println("Not you? Click <a href='new.html'>here</a>");
	    	out.println("<br> <h4>Top Matching Profiles For You:</h4>");
			for (CoderEntry coderEntry : entries) {
				out.println("<div>");
				out.println(coderEntry.toString());
				out.println("<hr>");
				out.println("</div>");
			}
			out.println("<br/><a href='get_coders'>View All Entries</a>");
	    	out.println("</body>");
	    	out.println("</html>");
		}
	}
	
	// Redirect to registration form
	private void redirectToRegistration(HttpServletResponse resp){
		resp.setStatus(resp.SC_MOVED_TEMPORARILY);
		resp.setHeader("Refresh", "3; URL=new.html");
	}
	
	private Boolean isNewUser(Cookie[] cookies){
		List<String> names = new ArrayList<String>();
		if(cookies==null){
			return true;
		}
		for (Cookie cookie : cookies) {
			names.add(cookie.getName());
		}
		if(names.contains("fName") && names.contains("lName")){
			return false;
		}else{
			return true;
		}
	}

}
