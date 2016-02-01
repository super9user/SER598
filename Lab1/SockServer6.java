
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.*;

class SockServer6 {
	public static void main (String args[]) throws Exception {
	    ServerSocket serv = null;
	    
	    // {ClientId -> Wrapper Object}
	    Map<Integer, XMLWrapper6> map = new HashMap<Integer, XMLWrapper6>();
	    
	    if (args.length != 2) {
			System.out.println("USAGE: java SockServer6 <file_name>.xml <delay>");
			System.exit(1);
		}
	    
	    try {
		    serv = new ServerSocket(8888);
		} catch(Exception e) {
		    e.printStackTrace();
		}

	    String fileName = args[0];
	    int delay = Integer.parseInt(args[1]);
	    
	    //ThreadPool created of size 10
	    ExecutorService executor = Executors.newFixedThreadPool(10);
		
		while (serv.isBound() && !serv.isClosed()) {
		    System.out.println("Ready...");
			try {
				Socket sock = serv.accept();
				InputStream in = sock.getInputStream();
			    OutputStream out = sock.getOutputStream();
			    
			    int clientId = in.read();
			    XMLWrapper6 clientWrapper = map.get(clientId); 
			    if(clientWrapper==null){
			    	clientWrapper = new XMLWrapper6(fileName, delay);
			    	map.put(clientId, clientWrapper);
			    }
			    executor.submit(new ClientThread(clientWrapper, clientId, in, out, sock));
			    
			} catch (Exception e) {
			    e.printStackTrace();
			}
		}
	}
}

class ClientThread extends Thread {
	XMLWrapper6 wrapper;
	int clientId;
	InputStream in;
	OutputStream out;
	Socket sock;
	
	public ClientThread(XMLWrapper6 clientWrapper, int clientId, InputStream in, OutputStream out, Socket sock) {
		this.wrapper = clientWrapper;
		this.clientId = clientId;
		this.in = in;
		this.out = out;
		this.sock = sock;
	}
	
	public void run(){
		System.out.println("Starting Thread for Client: " + clientId);
		try {
			int result = wrapper.performOperation(clientId, in);
			
			DataOutputStream dOut = new DataOutputStream(out);
			dOut.writeInt(result);
			dOut.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if (out != null)  out.close();
			    if (in != null)   in.close();
			    if (sock != null) sock.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println("Exiting Thread for Client: " + clientId);
	}
}

class XMLWrapper6{
	String fileName;
	int delay;
	Document doc;
	
	public XMLWrapper6(String fileName, int delay) {
		this.fileName = fileName;
		this.delay = delay;
		try {
			this.doc = findOrCreateXML(fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// write the content into xml file
	// this method is called from both sync and async blocks
	public void writeXML(String fileName, Document doc) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileName));
		transformer.transform(source, result);
	}
	
	public Document findOrCreateXML(String fileName) throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
	    File f = new File(fileName);
	    Document doc;
	    if(f.exists()){
	    	doc = docBuilder.parse(fileName);
	    }else{
			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("clients_root");
			doc.appendChild(rootElement);
			
			writeXML(fileName, doc);
			System.out.println("New XML file created!");
	    }
	    return doc;
	}
	
	public synchronized int performOperation(int clientId, InputStream in) throws Exception{
	    Element root = doc.getDocumentElement();
	    NodeList clients = doc.getElementsByTagName("client");
	    Element client = null;

		for (int i = 0; i < clients.getLength(); i++) {
			Node clientNode = clients.item(i);
			if(clientNode.getNodeType() == Node.ELEMENT_NODE){
				Element el = (Element)clientNode;
				int elId = Integer.parseInt(el.getElementsByTagName("client_id").item(0).getTextContent());
				if(elId == clientId){
					client = el;
					break;
				}
			}
		}
		
		if(client==null){
			// create new client entry in xml file
			Element newClient = doc.createElement("client");

            Element id = doc.createElement("client_id");
            id.appendChild(doc.createTextNode(Integer.toString(clientId)));
            newClient.appendChild(id);

            Element totalEl = doc.createElement("running_total");
            totalEl.appendChild(doc.createTextNode(Integer.toString(0)));
            newClient.appendChild(totalEl);
            
            root.appendChild(newClient);
            client = newClient;
		}
		
		
		// User Induced Delay
		if(delay>0){
			Thread.sleep(delay);
		}
		
	    
		// runningTotal for the corresponding client
	    Node runningTotal = client.getElementsByTagName("running_total").item(0);
		String val = runningTotal.getTextContent();
		int total = Integer.parseInt(val);

		char x = (char)in.read();
		switch(x){
		    case 'r':
		    	// reset input 
		    	total = 0;
		    	break;
		    case 't':
		    	// return total 
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
		    	total = total + Integer.parseInt(number);
		    	break;
	    }
		
		runningTotal.setTextContent(Integer.toString(total));
		writeXML(fileName, doc);
		
		return total;
	}
}
