package de.kuei.metafora.gwt.smack.server;

import java.util.Vector;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.kuei.metafora.gwt.smack.server.xml.XMLException;
import de.kuei.metafora.gwt.smack.server.xml.XMLUtils;
import de.kuei.metafora.xmpp.XMPPBridge;

public class DocUploadIndicator {

	private static DocUploadIndicator instance = null;

	public static DocUploadIndicator getInstance() {
		if (instance == null) {
			instance = new DocUploadIndicator();
		}

		return instance;
	}

	public void sendUploadIndicator(Vector<String> users, String docname,
			String groupId, String challengeId, String challengeName) {
		try {
			// send indicator to analysis channel
			Document doc = XMLUtils.createDocument();
			Element action = doc.createElement("action");
			action.setAttribute("time", System.currentTimeMillis() + "");
			doc.appendChild(action);

			Element actiontype = doc.createElement("actiontype");
			actiontype.setAttribute("type", "INDICATOR");
			actiontype.setAttribute("classification", "other");
			if (StartupServlet.productive) {
				actiontype.setAttribute("logged", "true");
			} else {
				actiontype.setAttribute("logged", "false");
			}
			action.appendChild(actiontype);

			for (String u : users) {
				Element userElement = doc.createElement("user");
				userElement.setAttribute("id", u);
				userElement.setAttribute("role", "originator");
				action.appendChild(userElement);
			}

			Element object = doc.createElement("object");
			object.setAttribute("id", users.get(0) + "'s upload");
			object.setAttribute("type", "upload");
			action.appendChild(object);

			Element properties = doc.createElement("properties");
			object.appendChild(properties);

			Element property;
			property = doc.createElement("property");
			properties.appendChild(property);
			property.setAttribute("name", "SENDING_TOOL");
			if (StartupServlet.productive) {
				property.setAttribute("value", "WORKBENCH");
			} else {
				property.setAttribute("value", "WORKBENCH_TEST");
			}

			property = doc.createElement("property");
			properties.appendChild(property);
			property.setAttribute("name", "GROUP_ID");
			property.setAttribute("value", groupId);

			property = doc.createElement("property");
			properties.appendChild(property);
			property.setAttribute("name", "CHALLENGE_ID");
			property.setAttribute("value", challengeId);

			property = doc.createElement("property");
			properties.appendChild(property);
			property.setAttribute("name", "CHALLENGE_NAME");
			property.setAttribute("value", challengeName);

			Element content = doc.createElement("content");
			action.appendChild(content);

			Element description = doc.createElement("description");
			CDATASection cdata = doc.createCDATASection(users.get(0)
					+ " uploaded a document!");
			description.appendChild(cdata);
			content.appendChild(description);

			Element contentProperties = doc.createElement("properties");
			content.appendChild(contentProperties);

			Element property2 = doc.createElement("property");
			contentProperties.appendChild(property2);
			property2.setAttribute("name", "INDICATOR_TYPE");
			property2.setAttribute("value", "ACTIVITY");

			property2 = doc.createElement("property");
			contentProperties.appendChild(property2);
			property2.setAttribute("name", "SENDING_TOOL");
			if (StartupServlet.productive) {
				property2.setAttribute("value", "WORKBENCH");
			} else {
				property2.setAttribute("value", "WORKBENCH_TEST");
			}

			property2 = doc.createElement("property");
			contentProperties.appendChild(property2);
			property2.setAttribute("name", "ACTIVITY_TYPE");
			property2.setAttribute("value", users.get(0) + " uploaded " + docname);

			String xmlXMPPMessage = XMLUtils.documentToString(doc,
					"http://metafora.ku-eichstaett.de/dtd/commonformat.dtd");

			XMPPBridge.getConnection("workbenchanalysis").sendMessage(
					xmlXMPPMessage);

		} catch (XMLException exc) {
			System.err.println("DocUpload: " + exc.getMessage());
			exc.printStackTrace();
		}
	}
}
