
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCodersServlet extends javax.servlet.http.HttpServlet {
	
	XMLWrapper wrapper;
	String contextPath;
	List<String> attributeNames;
	List<String> possibleLanguages = new ArrayList<String>();
	List<String> possibleAvailabilities = new ArrayList<String>();
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        contextPath = System.getProperty("catalina.home");
        String xmlName = getServletContext().getInitParameter("xml_name");
        wrapper = new XMLWrapper(contextPath + "/" + xmlName);
        attributeNames = CoderEntry.getAttributeNames();
        
        possibleLanguages.add("java");
        possibleLanguages.add("python");
        possibleLanguages.add("ruby");
        possibleLanguages.add("c");
        possibleLanguages.add("c++");
        
        possibleAvailabilities.add("monday");
        possibleAvailabilities.add("tuesday");
        possibleAvailabilities.add("wednesday");
        possibleAvailabilities.add("thursday");
        possibleAvailabilities.add("friday");
        possibleAvailabilities.add("saturday");
        possibleAvailabilities.add("sunday");
        
    }
	
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	
    	String userAgent = req.getHeader("User-Agent");
    	// Disable caching
    	res.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
    	
    	Enumeration<String> a = req.getParameterNames();
    	while(a.hasMoreElements()){
    		if(!attributeNames.contains(a.nextElement())){
    			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
    		    return;
    		}
    	}
    	
    	String fName = null;
    	if(req.getParameter("firstName")!=null && req.getParameter("firstName")!=""){
    		fName = req.getParameter("firstName");
    	}
    	String lName = null;
    	if(req.getParameter("lastName")!=null && req.getParameter("lastName")!=""){
    		lName = req.getParameter("lastName");
    	}
    	float exp = -1;
    	if(req.getParameter("experience")!=null && req.getParameter("experience")!=""){
    		
    		try{
    			exp = Float.parseFloat(req.getParameter("experience"));
    		}catch(Exception e){
    			e.printStackTrace();
    			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
    		    return;
    		}
    	}
    	
    	List<String> languages = new ArrayList<String>();
    	if(req.getParameterValues("languages")!=null){
    		for (String language : req.getParameterValues("languages")) {
    			if(possibleLanguages.contains(language)){
    				languages.add(language);
    			}else{
    				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        		    return;
    			}
    			
    		}
    	}
    	
    	List<String> availabilities = new ArrayList<String>();
    	if(req.getParameterValues("availability")!=null){
    		for (String availability : req.getParameterValues("availability")) {
    			if(possibleAvailabilities.contains(availability)){
    				availabilities.add(availability);
    			}else{
    				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        		    return;
    			}
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
    			"First name: <input type='text' name='firstName' value='"+ (filterEntry.getFirstName()==null ? "" : filterEntry.getFirstName()) +"'>" +
    			"Last name: <input type='text' name='lastName' value='"+ (filterEntry.getLastName()==null ? "" : filterEntry.getLastName()) +"'>" +
    			"Experience (in years): <input type='number' name='experience' value='"+ (filterEntry.getExperience()<0 ? "" : filterEntry.getExperience()) +"'><br><br>" +
    			"Languages:<br>";
    	
    	for (String l : possibleLanguages) {
    		str +=	"<input type='checkbox' name='languages' value='" + l + "' "+ (langs.contains(l) ? "checked" : "") +">"+ Character.toUpperCase(l.charAt(0)) + l.substring(1);
		}
    	str += "<br><br>";
    	str +=	"Availability:<br>";
    	for (String a : possibleAvailabilities) {
    		str +=	"<input type='checkbox' name='availability' value='" + a + "' "+ (avails.contains(a) ? "checked" : "") +">" + Character.toUpperCase(a.charAt(0)) + a.substring(1);
		}
    	str += "<br><br><input type='submit' value='Filter Results'></form>";
		return str;
    }
}
