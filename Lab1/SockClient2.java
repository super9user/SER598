
import java.net.*;
import java.io.*;

class SockClient2 {
	public static void main (String args[]) throws Exception {
		Socket sock = null;
		OutputStream out = null;
		InputStream in = null;
		int i1=0;
		
		if (args.length != 1) {
			System.out.println("USAGE: java SockClient2 'r'/ <int>");
			System.exit(1);
		}
		
		try {
			sock = new Socket("localhost", 8888);
			out = sock.getOutputStream();
			in = sock.getInputStream();
			
			char cmd = args[0].charAt(0);
			switch (args[0]) {
				case "r":
					out.write(cmd);
					break;
				default: 
					try {
						i1 = Integer.parseInt(args[0]);
						String str = "i" + i1;
						out.write(str.getBytes());
					} catch (NumberFormatException nfe) {
						System.out.println("Command line args must be 'r' or an integer");
						System.exit(2);
					}
			}
			
			int result = in.read();
			System.out.println("Result is " + result);
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
