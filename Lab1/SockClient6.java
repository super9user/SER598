
import java.net.*;
import java.io.*;

class SockClient6 {
	public static void main (String args[]) throws Exception {
		Socket sock = null;
		OutputStream out = null;
		InputStream in = null;
		int i1=0;
		
		if (args.length != 2) {
			System.out.println("USAGE: java SockClient6 <client_id> 'r'(reset)/'t'(total)/<int>");
			System.exit(1);
		}
		
		try {
			sock = new Socket("localhost", 8888);
			out = sock.getOutputStream();
			in = sock.getInputStream();
			
			char cmd = args[1].charAt(0);
			try {
				switch (args[1]) {
					case "r":
						out.write(Integer.parseInt(args[0]));
						out.write(cmd);
						break;
						
					case "t":
						out.write(Integer.parseInt(args[0]));
						out.write(cmd);
						break;
						
					default:
						out.write(Integer.parseInt(args[0]));
						i1 = Integer.parseInt(args[1]);
						String str = "i" + i1;
						out.write(str.getBytes());
						
				}
			} catch (NumberFormatException nfe) {
				System.out.println("USAGE: java SockClient <client_id> 'r'(reset)/'t'(total)/<int>");
				System.exit(2);
			}
			DataInputStream dIn = new DataInputStream(in);
	    	System.out.println("Result is " + dIn.readInt());
	    	
		} catch(ConnectException e){
			System.out.println("Please start the server first.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)  out.close();
			if (in != null)   in.close();
			if (sock != null) sock.close();
		}
	}
}
