
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

class SockServer4 {
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
			    char x = (char)in.read();
			    
//			    Thread.sleep(10000);
			    switch(x){
				    case 'r':
				    	// reset input 
				    	System.out.println("Server received reset command.");
				    	runningTotal = 0;
				    	break;
				    case 't':
				    	// return total 
				    	System.out.println("Server received total command.");
				    	// do nothing with the runningTotal as it is previously set
				    	break;
				    case 'i':
				    	// integer input
				    	int digits = in.available();
				    	String number = "";
				    	int initNumber = in.read();
				    	int index;
				    	if(Character.getNumericValue(initNumber)<0){
				    		index = 2;
				    		in.read();
				    	}else{
				    		index = 1;
				    	}
				    	number += Character.getNumericValue(initNumber);
				    	for (int i = index; i < digits; i++) {
				    		number += Character.getNumericValue(in.read());
						}
				    	System.out.println("Server received number: " + number);
				    	runningTotal = runningTotal + Integer.parseInt(number);
				    	break;
			    }
			    totals.put(clientId, runningTotal);
			    DataOutputStream dOut = new DataOutputStream(out);
				dOut.writeInt(runningTotal);
				dOut.flush();
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

