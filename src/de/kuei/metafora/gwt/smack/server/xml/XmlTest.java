package de.kuei.metafora.gwt.smack.server.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XmlTest {

	public static void main(String[] args){
		try {
			Document doc = XMLUtils.createDocument();
						
			Element action =  doc.createElement("action");
			action.setAttribute("time", "1302691416192");
			doc.appendChild(action);
			
			Element actiontype =  doc.createElement("actiontype");
			actiontype.setAttribute("classification", "create");
			actiontype.setAttribute("type", "createNode");
			actiontype.setAttribute("logged", "true");
			action.appendChild(actiontype);
			
			Element user =  doc.createElement("user");
			user.setAttribute("id", "John");
			user.setAttribute("ip", "141.78.96.123");
			user.setAttribute("role", "originator");
			action.appendChild(user);
			
			user =  doc.createElement("user");
			user.setAttribute("id", "Anna");
			user.setAttribute("ip", "141.78.96.123");
			user.setAttribute("role", "originator");
			action.appendChild(user);
			
			user =  doc.createElement("user");
			user.setAttribute("id", "Simon");
			user.setAttribute("ip", "141.78.96.123");
			user.setAttribute("role", "originator");
			action.appendChild(user);

			Element object =  doc.createElement("object");
			object.setAttribute("id", "default_node_1");
			object.setAttribute("type", "<unser xml>");
			action.appendChild(object);
			
			String xml = XMLUtils.documentToString(doc, "http://data.metafora-project.de/dtd/commonformat.dtd");
			System.out.println(xml);
			
			Document doc2 = XMLUtils.parseXMLString(xml, false);
			NodeList nodes = doc2.getElementsByTagName("action");
			System.out.println(nodes.getLength());
			if(nodes.getLength() > 0){
				Node node = nodes.item(0);
				System.out.println(node.getAttributes().getLength());
				Node time = node.getAttributes().getNamedItem("time");
				if(time != null)
					System.out.println(time.getTextContent());
			}
			
			System.err.println("doc3:");
			Document doc3 = XMLUtils.parseXMLString(xml, true);
			
			System.err.println("doc4:");
			String xml2 = XMLUtils.documentToString(doc);
			Document doc4 = XMLUtils.parseXMLString(xml2, true);
			
			
			
			Document content = XMLUtils.createDocument();
			
			Element obj = content.createElement("object");
			content.appendChild(obj);
			
			Element node = content.createElement("node");
			node.setAttribute("id", "default_node_1");
			obj.appendChild(node);
			
			
			Element graphics = content.createElement("graphics");
			node.appendChild(graphics);
			
			Element bordercolor = content.createElement("bordercolor");
			bordercolor.setAttribute("value", "#FFFFFF");
			graphics.appendChild(bordercolor);
			
			Element position = content.createElement("position");
			position.setAttribute("x", "123");
			position.setAttribute("y", "456");
			graphics.appendChild(position);
			
			Element text = content.createElement("text");
			text.setAttribute("value", "Hallo Welt!");
			node.appendChild(text);
			
			Element properties = content.createElement("properties");
			node.appendChild(properties);
			
			Element pictureurl = content.createElement("pictureurl");
			pictureurl.setAttribute("value", "visualcards/Agreement01.jpg");
			properties.appendChild(pictureurl);
			
			Element tool = content.createElement("tool");
			tool.setAttribute("value", "http://www.example.com:8080/lasad?autologin=true,...");
			properties.appendChild(tool);
			
			Element categorie = content.createElement("categorie");
			categorie.setAttribute("value", "aCategorie");
			properties.appendChild(categorie);
			
			Element name = content.createElement("name");
			name.setAttribute("value", "aName");
			properties.appendChild(name);
			
			String contentxml = XMLUtils.documentToString(content, "http://data.metafora-project.de/dtd/planningtoolelement.dtd");
			System.out.println(contentxml);
			
			System.err.println("vor parse");
			
			Document content2 = XMLUtils.parseXMLString(contentxml, true);
			
			System.err.println("nach parse");
			
			NodeList list = doc.getElementsByTagName("object");
			System.out.println(list.getLength());
			
			Node o = list.item(0);
			Element elem = (Element)o;
			elem.setAttribute("type", contentxml);
			
			String alles = XMLUtils.documentToString(doc, "http://data.metafora-project.de/dtd/commonformat.dtd");
			System.out.println(alles);
			
			System.err.println("vor parse");
			
			Document allesDoc = XMLUtils.parseXMLString(alles, true);
			
			System.err.println("nach parse");
			
			String innerXml = allesDoc.getElementsByTagName("object").item(0).getAttributes().getNamedItem("type").getNodeValue();
			System.out.println(innerXml);
			
			System.err.println("vor parse");
			
			XMLUtils.parseXMLString(contentxml, true);
			
			System.err.println("nach parse");
			
		} catch (XMLException e) {
			e.printStackTrace();
		}
	}
	
}
