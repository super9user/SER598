
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PostCoderServlet extends javax.servlet.http.HttpServlet {
	
	XMLWrapper wrapper;
	String contextPath;
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contextPath = System.getProperty("catalina.home");
        String xmlName = getServletContext().getInitParameter("xml_name");
        wrapper = new XMLWrapper(contextPath + xmlName);
    }
	
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	String fName = req.getParameter("fname");
    	String lName = req.getParameter("lname");
    	float exp = 0;
    	if(req.getParameter("experience")!=""){
    		exp = Float.parseFloat(req.getParameter("experience"));
    	}
    	
    	List<String> languages = new ArrayList<String>();
    	if(req.getParameterValues("languages")!=null){
    		for (String language : req.getParameterValues("languages")) {
    			languages.add(language);
    		}
    	}
    	
    	List<String> availabilities = new ArrayList<String>();
    	if(req.getParameterValues("availability")!=null){
    		for (String availability : req.getParameterValues("availability")) {
        		availabilities.add(availability);
    		}
    	}
    	
    	CoderEntry entry = new CoderEntry(fName, lName, exp, languages, availabilities);
    	
    	
    	PrintWriter out = res.getWriter();
    	res.setContentType("text/html");
    	out.println("<html>");
		out.println("<head><title>Task1 - Lab2</title></head>");
		out.println("<body>");
    	try {
    		// Synchronized block
    		synchronized (this) {
    			wrapper.createEntry(entry);
			}
			out.println("<H4>New entry created successfully.</H4>");
		} catch (Exception e) {
			out.println("<H4>Error Encountered.</H4>");
			e.printStackTrace();
		}
    	out.println("<H4>Total Entries: " + wrapper.getTotalEntries() + "</H4>");
    	String referrer = req.getHeader("referer");
    	out.println("<a href=" + referrer + "><< Go Back</a>");
    	out.println("</body></html>");
	}
}
