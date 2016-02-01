
import java.net.*;
import java.io.*;

class SockServer2 {
	public static void main (String args[]) throws Exception {
	    ServerSocket serv = null;
	    InputStream in = null;
	    OutputStream out = null;
	    Socket sock = null;
	    int runningTotal = 0;
	    
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
			    char x = (char)in.read();
			    
//			    Thread.sleep(10000);
			    switch(x){
				    case 'r':
				    	// reset input 
				    	System.out.println("Server received reset command");
				    	runningTotal = 0;
				    	break;
				    case 'i':
				    	// integer input
				    	int digits = in.available();
				    	String number = "";
				    	for (int i = 0; i < digits; i++) {
							number += Character.getNumericValue(in.read());
						}
				    	System.out.println("Server received number: " + number);
				    	runningTotal = runningTotal + Integer.parseInt(number);
				    	break;
			    }
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

