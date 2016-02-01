
import java.net.*;
import java.io.*;

class SockClient3 {
	public static void main (String args[]) throws Exception {
		Socket sock = null;
		OutputStream out = null;
		InputStream in = null;
		int i1=0;
		
		if (args.length != 2) {
			System.out.println("USAGE: java SockClient3 <client_id> 'r'(reset)/'t'(total)/<int>");
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
//						String str = "i" + i1;
//						out.write(str.getBytes());
						out.write(i1);
						
				}
			} catch (NumberFormatException nfe) {
				System.out.println("USAGE: java SockClient <client_id> 'r'(reset)/'t'(total)/<int>");
				System.exit(2);
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
