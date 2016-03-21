package edu.asupoly.ser422.Helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.xml.sax.InputSource;

public class XmlWrapper {
	String fileName;
	String rowName;
	List<String> schematics;
	
	public XmlWrapper(String fileName, String rowName, List<String> schematics) {
		this.fileName = fileName;
		this.rowName = rowName;
		this.schematics = schematics;
		try {
			getDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void rollback(Document doc){
		try {
			writeToXml(fileName, doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void commit(Document doc) throws Exception {
		try {
			writeToXml(fileName, doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// write the content into xml file
	private void writeToXml(String fileName, Document doc) throws Exception{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileName));
		transformer.transform(source, result);
	}
	
	public Document getDocument(){
		Document doc = null;
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    File f = new File(fileName);
		    
		    if(f.exists()){
		        doc = docBuilder.parse(fileName);
		    }else{
				doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("root");
				doc.appendChild(rootElement);
				
				writeToXml(fileName, doc);
				System.out.println("New XML file created!");
		    }
		}catch(Exception e){
			System.out.println("EEEERRRRRR");
			e.printStackTrace();
		}
		
	    return doc;
	}
	
	public Document createEntry(Document doc, Map<String, String> values){
		Element root = doc.getDocumentElement();
		Element newEntry = doc.createElement(rowName);
		for (String attr : schematics) {
			if(values.get(attr)!=null){
				Element attrEl = doc.createElement(attr);
				attrEl.appendChild(doc.createTextNode(values.get(attr)));
		        newEntry.appendChild(attrEl);
			}
		}
        root.appendChild(newEntry);
        
        return doc;
	}
	
	public Document updateEntry(Document doc, Map<String, String> newValues, String idKey, String idValue){
		
		Element root = doc.getDocumentElement();
		NodeList allNodes = root.getElementsByTagName(rowName);
		
		for (int i = 0; i < allNodes.getLength(); i++) {
			Node node = allNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element el = (Element)node;
				String attr = el.getElementsByTagName(idKey).item(0).getTextContent();
				if(idValue.equalsIgnoreCase(attr)){
					// element found
					for (String sAttr : schematics) {
						Element idElement = (Element) el.getElementsByTagName(sAttr).item(0);
						idElement.setTextContent(newValues.get(sAttr));
					}
					return doc;
				}
			}
		}
		return null;
	}
	
	public Map<String, String> findEntry(Document doc, String key, String value){
		
		Element root = doc.getDocumentElement();
		NodeList allNodes = root.getElementsByTagName(rowName);
		Map<String, String> map = new HashMap<String, String>();
		
		
		for (int i = 0; i < allNodes.getLength(); i++) {
			Node node = allNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element el = (Element)node;
				String attr = el.getElementsByTagName(key).item(0).getTextContent();
				if(value.equalsIgnoreCase(attr)){
					for (String sAttr : schematics) {
						String val = el.getElementsByTagName(sAttr).item(0).getTextContent();
						map.put(sAttr, val);
					}
					return map;
				}
			}
		}
		return null;
	}
	
	public Document deleteEntry(Document doc, String key, String value){
		
		Element root = doc.getDocumentElement();
		NodeList allNodes = root.getElementsByTagName(rowName);
		
		for (int i = 0; i < allNodes.getLength(); i++) {
			Node node = allNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element el = (Element)node;
				String attr = el.getElementsByTagName(key).item(0).getTextContent();
				if(value.equalsIgnoreCase(attr)){
					node.getParentNode().removeChild(node);
					return doc;
				}
			}
		}
		return null;
	}
	
	public List<Map<String, String>> getAllEntries(Document doc){
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Element root = doc.getDocumentElement();
		NodeList allNodes = root.getElementsByTagName(rowName);
		for (int i = 0; i < allNodes.getLength(); i++) {
			Node node = allNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element el = (Element)node;
				
				Map<String, String> map = new HashMap<String, String>();
				for (String attr : schematics) {
					String val = el.getElementsByTagName(attr).item(0).getTextContent();
					map.put(attr, val);
				}
				list.add(map);
							}
		}
		return list;
	}
	
	public List<Map<String, String>> filterEntries(Document doc, String key, String value){
		
		Element root = doc.getDocumentElement();
		NodeList allNodes = root.getElementsByTagName(rowName);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		for (int i = 0; i < allNodes.getLength(); i++) {
			Node node = allNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element el = (Element)node;
				String attr = el.getElementsByTagName(key).item(0).getTextContent();
				if(attr.contains(value)){
					Map<String, String> map = new HashMap<String, String>();
					for (String sAttr : schematics) {
						String val = el.getElementsByTagName(sAttr).item(0).getTextContent();
						map.put(sAttr, val);
					}
					list.add(map);
				}
			}
		}
		return list;
	}
	
}
