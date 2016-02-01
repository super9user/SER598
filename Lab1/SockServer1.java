
import java.net.*;
import java.io.*;

class SockServer1 {
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
			
			    int x = in.read();
			    System.out.println("Server received " + x);
//			    Thread.sleep(10000);
			    runningTotal = runningTotal + x;
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

