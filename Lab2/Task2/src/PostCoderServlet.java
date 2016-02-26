
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PostCoderServlet extends javax.servlet.http.HttpServlet {
	
	XMLWrapper wrapper;
	String contextPath;
	List<String> attributeNames;
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contextPath = System.getProperty("catalina.home");
        String xmlName = getServletContext().getInitParameter("xml_name");
        wrapper = new XMLWrapper(contextPath + "/" + xmlName);
        attributeNames = CoderEntry.getAttributeNames();
    }
	
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	
    	// Do NOT create a new session object it is already not present
    	HttpSession session = req.getSession(false);
		String html = ""; 
		Boolean prevClicked = true;
		Boolean cancelClicked = false;
		int page;
		
		if(req.getParameter("cancel") != null){
			cancelClicked = true;
		}else if (req.getParameter("prev") == null) {
	    	// next button was clicked
	    	prevClicked = false;
	    }
	    
	    if(session==null && (req.getParameter("page")==null || req.getParameter("page")=="")){
	    	// session expired. show error message
			page = -1;
	    } else if(session==null){
	    	// first page of form
			// ok to create a new session
			page = 1;
			session = req.getSession(true);
			prevClicked = false;
	    } else if((req.getParameter("page")!=null && req.getParameter("page")!="")){
	    	session.invalidate();
	    	page = 1;
			session = req.getSession(true);
			prevClicked = false;
	    }else{
	    	page = (int) session.getAttribute("page");
	    }
	    
	    if(session!=null){
		    
		    if(prevClicked && !cancelClicked){
				page -= 1;
			}else{
				page += 1;
			}
			session.setAttribute("page", page);
			
			if(page >= 6){
				if (!cancelClicked) {
			    	// submit button was clicked
					CoderEntry entry = extractEntry(session);
					
					synchronized (this) {
						try{
							wrapper.createEntry(entry);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					// add to cookies
		    		Cookie fName = new Cookie("fName", entry.getFirstName());
		    		Cookie lName = new Cookie("lName", entry.getLastName());
		    		
		    		// Set expiry date after 1 Hr
		    		fName.setMaxAge(60*60); 
		    		lName.setMaxAge(60*60);

		    	    // Add both the cookies in the response header.
					res.addCookie(fName);
					res.addCookie(lName);
					
			    }
				// invalidate session
		    	session.invalidate();
		    	redirectToLandingPage(res);
		    	return;
		    }
			
			Enumeration<String> e = req.getParameterNames();
		    while(e.hasMoreElements()){
		    	String attr = e.nextElement();
		    	if(attributeNames.contains(attr)){
		    		if(attr.equalsIgnoreCase("languages") || attr.equalsIgnoreCase("availability")){
		    			List<String> list = new ArrayList<String>();
		    	    	if(req.getParameterValues(attr)!=null){
		    	    		for (String str : req.getParameterValues(attr)) {
		    	    			list.add(str);
		    	    		}
		    	    	}
		    	    	session.setAttribute(attr, list);
		    		}else{
		    			session.setAttribute(attr, req.getParameter(attr));
		    		}
		    	}
		    }
		    CoderEntry entry = extractEntry(session);
			html = getHtmlForPage(page, entry);
	    }else{
	    	html = getHtmlForPage(page, new CoderEntry());
	    }
	    
    	
    	PrintWriter out = res.getWriter();
    	res.setContentType("text/html");
    	out.println("<html>");
		out.println("<head><title>Registration Form</title></head>");
		out.println("<body>");
    	out.println(html);
    	out.println("</body></html>");
    	
	}
    
    
    private String getHtmlForPage(int page, CoderEntry entry){
    	String html = "";
    	List<String> langs = entry.getLanguages();
    	List<String> avails = entry.getAvailability();
    	
    	switch (page) {
    		
    		case 1:
    			html += "<h3>New Coder Registration Form</h3>" +
    				"<form action='post_coder' method='post'>" +
		    			"First name: <input type='text' name='firstName' value='"+ entry.getFirstName() +"'><br>" +
		    			"Last name: <input type='text' name='lastName' value='"+ entry.getLastName() +"'><br>" +
		    			"<br><input type='submit' value='Next >>'>" + 
		    		"</form>";
    			break;
    		
			case 2:
				html += "<h3>New Coder Registration Form</h3>" +
					"<form action='post_coder' method='post'>" +
						"Experience (in years): <input type='number' name='experience' value='"+ (entry.getExperience() > 0 ? Math.round(entry.getExperience()) + "" : "0") +"'><br><br>" +
						"<br><input type='submit' name='prev' value='<< Prev'> <input type='submit' name='next' value='Next >>'>" +
					"</form>";
				break;
				
			case 3:
				html += "<h3>New Coder Registration Form</h3>" +
					"<form action='post_coder' method='post'>" +
						"Languages:<br>" +
						"<input type='checkbox' name='languages' value='java' "+ (langs.contains("java") ? "checked" : "") +">Java" +
						"<input type='checkbox' name='languages' value='python' "+ (langs.contains("python") ? "checked" : "") +">Python" +
						"<input type='checkbox' name='languages' value='ruby' "+ (langs.contains("ruby") ? "checked" : "") +">Ruby" +
			  			"<input type='checkbox' name='languages' value='c' "+ (langs.contains("c") ? "checked" : "") +">C" +
			  			"<input type='checkbox' name='languages' value='c++' "+ (langs.contains("c++") ? "checked" : "") +">C++<br>" +
						"<br><input type='submit' name='prev' value='<< Prev'> <input type='submit' name='next' value='Next >>'>" +
					"</form>";
				break;
				
			case 4:
				html += "<h3>New Coder Registration Form</h3>" +
				"<form action='post_coder' method='post'>" +
					"Availability:<br>" +
					"<input type='checkbox' name='availability' value='monday' "+ (avails.contains("monday") ? "checked" : "") +">Monday" +
					"<input type='checkbox' name='availability' value='tuesday' "+ (avails.contains("tuesday") ? "checked" : "") +">Tuesday" +
					"<input type='checkbox' name='availability' value='wednesday' "+ (avails.contains("wednesday") ? "checked" : "") +">Wednesday" +
		  			"<input type='checkbox' name='availability' value='thursday' "+ (avails.contains("thursday") ? "checked" : "") +">Thursday" +
		  			"<input type='checkbox' name='availability' value='friday' "+ (avails.contains("friday") ? "checked" : "") +">Friday" +
		  			"<input type='checkbox' name='availability' value='saturday' "+ (avails.contains("saturday") ? "checked" : "") +">Saturday" +
		  			"<input type='checkbox' name='availability' value='sunday' "+ (avails.contains("sunday") ? "checked" : "") +">Sunday" +
					"<br><br><input type='submit' name='prev' value='<< Prev'> <input type='submit' name='next' value='Next >>'>" +
				"</form>";
				break;
				
			case 5:
				html += "<h3>Preview:</h3>" +
					entry.toString() + 
					"<br/><br/>" +
					"<form action='post_coder' method='post'>" +
						"<input type='submit' name='prev' value='<< Prev' />" +
						"<input type='submit' name='cancel' value='Cancel' />" + 
						"<input type='submit' name='submit' value='Submit' />" +
					"</form>";
				break;
	
			default:
				html += "Oops! Looks like your session has expired! <br/>";
    			html += "<a href='new.html'>Start Over</a>";
    			break;
		}
    	
    	return html;
    }
    
    private CoderEntry extractEntry(HttpSession session){
    	CoderEntry entry = new CoderEntry();
    	
    	for (String attr : attributeNames) {
    		if(session.getAttribute(attr)!=null && session.getAttribute(attr)!=""){
    			switch (attr) {
					case "firstName":
						entry.setFirstName((String) session.getAttribute(attr));
						break;
					
					case "lastName":
						entry.setLastName((String) session.getAttribute(attr));
						break;
										
					case "experience":
						entry.setExperience(Float.parseFloat(session.getAttribute(attr).toString()));
						break;
										
					case "languages":
						List<String> l1 = (List<String>) session.getAttribute(attr);
						entry.setLanguages(l1);
						break;
						
					case "availability":
						List<String> l2 = (List<String>) session.getAttribute(attr);
						entry.setAvailability(l2);
						break;
				}
    		}
		}
    	return entry;
    }
    
 // Redirect to landing page
 	private void redirectToLandingPage(HttpServletResponse resp){
 		resp.setStatus(resp.SC_MOVED_TEMPORARILY);
 		resp.setHeader("Location", "home");
 	}
    
}
