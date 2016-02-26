
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XMLWrapper{
	String fileName;
	
	public XMLWrapper(String fileName) {
		this.fileName = fileName;
		try {
			findOrCreateXML();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// write the content into xml file
	public void writeXML(String fileName, Document doc) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileName));
		transformer.transform(source, result);
	}
	
	public Document findOrCreateXML(){
		Document doc = null;
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    File f = new File(fileName);
		    
		    if(f.exists()){
		    	doc = docBuilder.parse(fileName);
		    }else{
				doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("coders_root");
				doc.appendChild(rootElement);
				
				writeXML(fileName, doc);
				System.out.println("New XML file created!");
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	    return doc;
	}
	
	public int getTotalEntries(){
		Document doc = findOrCreateXML();
		Element root = doc.getDocumentElement();
		return root.getChildNodes().getLength();
	}
	
	public List<CoderEntry> getEntries(CoderEntry filterEntry){
		Document doc = findOrCreateXML();
		List<CoderEntry> list = new ArrayList<CoderEntry>();
		Element root = doc.getDocumentElement();
		NodeList allNodes = root.getElementsByTagName("coder");
		for (int i = 0; i < allNodes.getLength(); i++) {
			Node node = allNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element el = (Element)node;
				
				String fName = el.getElementsByTagName("first_name").item(0).getTextContent();
				String lName = el.getElementsByTagName("last_name").item(0).getTextContent();
				float exp = Float.parseFloat(el.getElementsByTagName("experience").item(0).getTextContent());
				
				NodeList languages = el.getElementsByTagName("languages");
				Element eLanguages = (Element)languages.item(0);
				NodeList language = eLanguages.getElementsByTagName("language");
				List<String> listLanguages = new ArrayList<String>();
				for (int j = 0; j < language.getLength(); j++) {
					Element eLanguage = (Element)language.item(j);
					listLanguages.add(eLanguage.getTextContent());
				}

				NodeList availabilities = el.getElementsByTagName("availabilities");
				Element eAvailabilities = (Element)availabilities.item(0);
				NodeList availability = eAvailabilities.getElementsByTagName("availability");
				List<String> listAvailabilities = new ArrayList<String>();
				for (int j = 0; j < availability.getLength(); j++) {
					Element eAvailability = (Element)availability.item(j);
					listAvailabilities.add(eAvailability.getTextContent());
				}
				
				//Filtering activities
				if(filterEntry.getFirstName()!=null && !fName.equalsIgnoreCase(filterEntry.getFirstName())){
					continue;
				}
				if(filterEntry.getLastName()!=null && !lName.equalsIgnoreCase(filterEntry.getLastName())){
					continue;
				}
				if(filterEntry.getExperience()>=0 && exp!=filterEntry.getExperience()){
					continue;
				}
				
				if(filterEntry.getLanguages().size()>0){
					if(listLanguages.size()==0){
						continue;
					}else{
						Boolean flag = true;
						for (String string : listLanguages) {
							if(filterEntry.getLanguages().contains(string)){
								flag = false;
								break;
							}
						}
						if(flag){
							continue;
						}
					}
				}
				
				if(filterEntry.getAvailability().size()>0){
					if(listAvailabilities.size()==0){
						continue;
					}else{
						Boolean flag = true;
						for (String string : listAvailabilities) {
							if(filterEntry.getAvailability().contains(string)){
								flag = false;
								break;
							}
						}
						if(flag){
							continue;
						}
					}
				}
				
				list.add(new CoderEntry(fName, lName, exp, listLanguages, listAvailabilities));
			}
		}
		return list;
	}
	
	public void createEntry(CoderEntry entry) throws Exception{
		Document doc = findOrCreateXML();
		Element root = doc.getDocumentElement();
	    
		// create new client entry in xml file
		Element newCoder = doc.createElement("coder");
		
        Element firstName = doc.createElement("first_name");
        firstName.appendChild(doc.createTextNode(entry.getFirstName()));
        newCoder.appendChild(firstName);
        
        Element lastName = doc.createElement("last_name");
        lastName.appendChild(doc.createTextNode(entry.getLastName()));
        newCoder.appendChild(lastName);
        
        Element experience = doc.createElement("experience");
        experience.appendChild(doc.createTextNode(entry.getExperience() + ""));
        newCoder.appendChild(experience);
        
        Element languages = doc.createElement("languages");
        for(String l: entry.getLanguages()){
        	Element language = doc.createElement("language");
        	language.appendChild(doc.createTextNode(l));
        	languages.appendChild(language);
        }
        newCoder.appendChild(languages);
        
        Element availabilities = doc.createElement("availabilities");
        for(String l: entry.getAvailability()){
        	Element availability = doc.createElement("availability");
        	availability.appendChild(doc.createTextNode(l));
        	availabilities.appendChild(availability);
        }
        newCoder.appendChild(availabilities);
        
        root.appendChild(newCoder);
        writeXML(fileName, doc);
	}
}
