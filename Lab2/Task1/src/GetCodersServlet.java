
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCodersServlet extends javax.servlet.http.HttpServlet {
	
	XMLWrapper wrapper;
	String contextPath;
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contextPath = System.getProperty("catalina.home");
        String xmlName = getServletContext().getInitParameter("xml_name");
        wrapper = new XMLWrapper(contextPath + xmlName);
        System.out.println("--------------- 111111 " + contextPath);
        System.out.println("--------------- 222222 " + System.getProperty("catalina.base"));
    }
	
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	String userAgent = req.getHeader("User-Agent");
    	// Disable caching
    	res.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
    	
    	String fName = null;
    	if(req.getParameter("fname")!=null && req.getParameter("fname")!=""){
    		fName = req.getParameter("fname");
    	}
    	String lName = null;
    	if(req.getParameter("lname")!=null && req.getParameter("lname")!=""){
    		lName = req.getParameter("lname");
    	}
    	float exp = -1;
    	if(req.getParameter("experience")!=null && req.getParameter("experience")!=""){
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
    	CoderEntry filterEntry = new CoderEntry(fName, lName, exp, languages, availabilities);
    	
    	PrintWriter out = res.getWriter();
    	res.setContentType("text/html");
    	out.println("<html>");
		out.println("<head><title>Task1 - Lab2</title></head>");
		if(userAgent.contains("Chrome")){
			out.println("<body style='background-color:pink;'>");
		}else{
			out.println("<body>");
		}
		out.println(getFilterHtml(filterEntry));
		out.println("<br> <h3>RESULTS</h3>");
		List<CoderEntry> entries = wrapper.getEntries(filterEntry);
		for (CoderEntry coderEntry : entries) {
			out.println("<div>");
			out.println(coderEntry.toString());
			out.println("<hr>");
			out.println("</div>");
		}
    	out.println("</body></html>");
	}
    
    private String getFilterHtml(CoderEntry filterEntry){
    	String str = "";
    	List<String> langs = filterEntry.getLanguages();
    	List<String> avails = filterEntry.getAvailability();
    	str += "<form action='get_coders' method='get'>" +
    			"First name: <input type='text' name='fname' value='"+ (filterEntry.getFirstName()==null ? "" : filterEntry.getFirstName()) +"'>" +
    			"Last name: <input type='text' name='lname' value='"+ (filterEntry.getLastName()==null ? "" : filterEntry.getLastName()) +"'>" +
    			"Experience (in years): <input type='number' name='experience' value='"+ (filterEntry.getExperience()<0 ? "" : filterEntry.getExperience()) +"'><br><br>" +
    			"Languages:<br>" +
    			
    			"<input type='checkbox' name='languages' value='java' "+ (langs.contains("java") ? "checked" : "") +">Java" +
    			"<input type='checkbox' name='languages' value='python' "+ (langs.contains("python") ? "checked" : "") +">Python" +
    			"<input type='checkbox' name='languages' value='ruby' "+ (langs.contains("ruby") ? "checked" : "") +">Ruby" +
    			"<input type='checkbox' name='languages' value='c' "+ (langs.contains("c") ? "checked" : "") +">C" +
    			"<input type='checkbox' name='languages' value='c++' "+ (langs.contains("c++") ? "checked" : "") +">C++<br><br>" +
    			"" +
    			"	Availability:<br>" +
    			"<input type='checkbox' name='availability' value='monday' "+ (avails.contains("monday") ? "checked" : "") +">Monday" +
    			"<input type='checkbox' name='availability' value='tuesday' "+ (avails.contains("tuesday") ? "checked" : "") +">Tuesday" +
    			"<input type='checkbox' name='availability' value='wednesday' "+ (avails.contains("wednesday") ? "checked" : "") +">Wednesday" +
    			"<input type='checkbox' name='availability' value='thursday' "+ (avails.contains("thursday") ? "checked" : "") +">Thursday" +
    			"<input type='checkbox' name='availability' value='friday' "+ (avails.contains("friday") ? "checked" : "") +">Friday" +
    			"<input type='checkbox' name='availability' value='saturday' "+ (avails.contains("saturday") ? "checked" : "") +">Saturday" +
    			"<input type='checkbox' name='availability' value='sunday' "+ (avails.contains("sunday") ? "checked" : "") +">Sunday" +
    			"<br>" +
    			"<br><input type='submit' value='Filter Results'>" +
    		"</form>";
		return str;
    }
}
