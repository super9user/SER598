
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

class SockServer3{
	public static void main (String args[]) throws Exception {
	    ServerSocket serv = null;
	    InputStream in = null;
	    OutputStream out = null;
	    Socket sock = null;
	    Map<Integer, Integer> totals = new HashMap<Integer, Integer>();
	    
		try {
		    serv = new ServerSocket(8888);
		} catch(Exception e) {
		    e.printStackTrace();
		}
		while (serv.isBound() && !serv.isClosed()) {
		    System.out.println("Ready...");
			try {
				sock = serv.accept();
			    in = sock.getInputStream();
			    out = sock.getOutputStream();
			    
			    int clientId = in.read();
			    Integer runningTotal = totals.get(clientId);
				if (runningTotal == null) {
					runningTotal = 0;
				}
//			    char x = (char)in.read();
			    int x = in.read();
			    
//			    Thread.sleep(10000);
			    switch(x){
				    case (char)'r':
				    	// reset input 
				    	System.out.println("Server received reset command.");
				    	runningTotal = 0;
				    	break;
				    	
				    case (char)'t':
				    	// return total 
				    	System.out.println("Server received total command.");
				    	// do nothing with the runningTotal as it is previously set
				    	break;
				    	
				    default:
				    	// integer input
				    	System.out.println("Server received number: " + x);
				    	runningTotal = runningTotal + x;
				    	break;
			    }
			    totals.put(clientId, runningTotal);
			    out.write(runningTotal);
			    out.flush();
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
			    if (out != null)  out.close();
			    if (in != null)   in.close();
			    if (sock != null) sock.close();
			}
		}
	}
}

