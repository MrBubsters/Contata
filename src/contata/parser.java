package src.contata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class parser {
	
	public static void downloadFileFromURL(String urlString, File destination) {    
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void parseForNodes() {
		try {

			File XmlFile = new File("file.xml");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.parse(XmlFile);
			
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("Cube");
			
			visitChildNodes(nList);

		    } catch (Exception e) {
			e.printStackTrace();
		    }
		 	
	}
	
	private static void visitChildNodes(NodeList nList) {
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
            //System.out.println("Node = " + node.getNodeName() + node.getTextContent());
				if (node.hasAttributes()) {
					NamedNodeMap nodeMap = node.getAttributes();
					for (int j = 0; j < nodeMap.getLength(); j++) {
               Node tempNode = nodeMap.item(j);
               System.out.println(tempNode.getNodeName() + " = " + tempNode.getNodeValue());
               }
               if (node.hasChildNodes()) {
                  visitChildNodes(node.getChildNodes());
               		}
				}
			}
		}
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		File xml = new File("file.xml");
		try {
			if (xml.createNewFile()) { 
			    System.out.println("File created: " + xml.getName()); 
			  } else { 
			    System.out.println("File already exists."); 
			  }
		} catch (IOException e) {
			e.printStackTrace();
		} 
		downloadFileFromURL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml", xml);
	    parseForNodes();
	}

}
