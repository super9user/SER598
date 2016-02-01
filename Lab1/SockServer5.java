
import java.net.*;
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

class SockServer5 {
	public static void main (String args[]) throws Exception {
	    ServerSocket serv = null;
	    InputStream in = null;
	    OutputStream out = null;
	    Socket sock = null;
	    
	    if (args.length != 1) {
			System.out.println("USAGE: java SockServer5 <file_name>.xml");
			System.exit(1);
		}
	    
	    try {
		    serv = new ServerSocket(8888);
		} catch(Exception e) {
		    e.printStackTrace();
		}

	    String fileName = args[0];
	    XMLWrapper5 wrapper = new XMLWrapper5(fileName);
		
		while (serv.isBound() && !serv.isClosed()) {
		    System.out.println("Ready...");
			try {
				sock = serv.accept();
			    in = sock.getInputStream();
			    out = sock.getOutputStream();
			    
			    int clientId = in.read();
			    // use the XMLWrapper object to perform the operations and write the result to the XML file
			    int result = wrapper.performOperation(clientId, in);
			    
			    System.out.println("Result is: " + result);
			    
			    DataOutputStream dOut = new DataOutputStream(out);
				dOut.writeInt(result);
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


class XMLWrapper5{
	String fileName;
	Document doc;
	
	public XMLWrapper5(String fileName) {
		this.fileName = fileName;
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

	    // Find Xml Element associated with clientId
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
		
		
	    
		// runningTotal for the corresponding client
	    Node runningTotal = client.getElementsByTagName("running_total").item(0);
		String val = runningTotal.getTextContent();
		int total = Integer.parseInt(val);

		char x = (char)in.read();
		switch(x){
		    case 'r':
		    	// reset input 
		    	System.out.println("Server received reset command.");
		    	total = 0;
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
		    	total = total + Integer.parseInt(number);
		    	
		    	break;
	    }
		
		runningTotal.setTextContent(Integer.toString(total));
		writeXML(fileName, doc);
		
		return total;
	}
}
