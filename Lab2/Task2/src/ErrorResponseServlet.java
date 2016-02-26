import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorResponseServlet extends javax.servlet.http.HttpServlet{
	
	Map<Integer, String> messages = new HashMap<Integer, String>();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		messages.put(400, "The server did not understand the request. Please check the query parameters.");
		messages.put(403, "Access is forbidden to the requested page");
		messages.put(404, "The server can not find the requested page.");
		messages.put(405, "The method specified in the request is not allowed.");
		messages.put(500, "The request was not completed. The server met an unexpected condition.");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) req.getAttribute("javax.servlet.error.servlet_name");
		String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");
		
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
    	out.println("<html>");
    	out.println("<head><title>Error Response</title></head>");
    	out.println("<body>");
    	out.println("Error has occured.");
    	out.println("<br />");
    	
    	if(statusCode != null){
    		out.println("Status Code: " + statusCode + " - " + messages.get(statusCode));
    		out.println("<br />");
    	}
		if(throwable != null){
			out.println("Exception: " + throwable.getMessage());
			out.println("<br />");
		}
		
		if(servletName != null){
			out.println("Servlet Name: " + servletName);
			out.println("<br />");
		}
		
		if(requestUri != null){
			out.println("Relative URL: " + requestUri);
			out.println("<br />");
		}
		
    	out.println("</body>");
    	out.println("</html>");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
