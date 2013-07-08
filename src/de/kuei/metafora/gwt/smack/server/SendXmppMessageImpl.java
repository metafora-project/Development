package de.kuei.metafora.gwt.smack.server;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kuei.metafora.gwt.smack.client.SendXmppMessage;
import de.kuei.metafora.gwt.smack.server.xml.XMLUtils;
import de.kuei.metafora.xmpp.XMPPBridge;

public class SendXmppMessageImpl extends RemoteServiceServlet implements
		SendXmppMessage {

	@Override
	public String sendXmppMessage(String message) {
		try {
			XMPPBridge.getConnection("workbenchcommand").sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "blubb";
	}

	@Override
	public String sendOpenCommandMessage(Vector<String> users, String groupId, String challengeId,
			String challengeName, String name, String id, String link,
			String version, String ip) {
		try {
			// TODO: IP/token/username

			Document doc = XMLUtils.createDocument();

			Element action = doc.createElement("action");
			action.setAttribute("time", System.currentTimeMillis() + "");
			doc.appendChild(action);

			Element actiontype = doc.createElement("actiontype");
			actiontype.setAttribute("type", "DISPLAY_STATE_URL");
			actiontype.setAttribute("classification", "other");
			if (StartupServlet.productive) {
				actiontype.setAttribute("logged", "true");
			} else {
				actiontype.setAttribute("logged", "false");
			}
			action.appendChild(actiontype);

			if (!users.isEmpty()) {
				for (String u : users) {
					Element lUsers = doc.createElement("user");
					lUsers.setAttribute("id", u);
					lUsers.setAttribute("ip", ip);
					lUsers.setAttribute("role", "controller");
					action.appendChild(lUsers);
				}
			}

			Element object = doc.createElement("object");
			object.setAttribute("id", "0");
			object.setAttribute("type", "ELEMENT");
			action.appendChild(object);

			Element properties = doc.createElement("properties");
			object.appendChild(properties);

			Element property = doc.createElement("property");
			properties.appendChild(property);
			property.setAttribute("name", "NAME");
			property.setAttribute("value", name);

			// TODO: finish
			Element property2 = doc.createElement("property");
			properties.appendChild(property2);
			property2.setAttribute("name", "ID");
			property2.setAttribute("value", id);

			Element property3 = doc.createElement("property");
			properties.appendChild(property3);
			property3.setAttribute("name", "LINK");
			property3.setAttribute("value", link);

			Element property4 = doc.createElement("property");
			properties.appendChild(property4);
			property4.setAttribute("name", "VERSION");
			property4.setAttribute("value", version);

			Element content = doc.createElement("content");
			object.appendChild(content);

			if(StartupServlet.productive){
				Element property6 = doc.createElement("property");
				content.appendChild(property6);
				property6.setAttribute("name", "SENDING_TOOL");
				property6.setAttribute("value", "WORKBENCH");
				
				property6 = doc.createElement("property");
				content.appendChild(property6);
				property6.setAttribute("name", "RECEIVING_TOOL");
				property6.setAttribute("value", "METAFORA");
			} else{
				Element property6 = doc.createElement("property");
				content.appendChild(property6);
				property6.setAttribute("name", "SENDING_TOOL");
				property6.setAttribute("value", "WORKBENCH_TEST");
				
				property6 = doc.createElement("property");
				content.appendChild(property6);
				property6.setAttribute("name", "RECEIVING_TOOL");
				property6.setAttribute("value", "METAFORA_TEST");
			}
			
			Element property7 = doc.createElement("property");
			content.appendChild(property7);
			property7.setAttribute("name", "GROUP_ID");
			property7.setAttribute("value", groupId);

			property7 = doc.createElement("property");
			content.appendChild(property7);
			property7.setAttribute("name", "CHALLENGE_ID");
			property7.setAttribute("value", challengeId);

			property7 = doc.createElement("property");
			content.appendChild(property7);
			property7.setAttribute("name", "CHALLENGE_NAME");
			property7.setAttribute("value", challengeName);

			String xmlXMPPMessage = XMLUtils.documentToString(doc,
					"http://metafora.ku-eichstaett.de/dtd/commonformat.dtd");

			// send command for Home to command channel
			XMPPBridge.getConnection("workbenchcommand").sendMessage(
					xmlXMPPMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
