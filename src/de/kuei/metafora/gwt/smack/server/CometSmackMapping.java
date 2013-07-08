package de.kuei.metafora.gwt.smack.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.zschech.gwt.comet.server.CometServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.kuei.metafora.gwt.smack.server.user.GroupMembers;
import de.kuei.metafora.gwt.smack.server.xml.XMLException;
import de.kuei.metafora.gwt.smack.server.xml.XMLUtils;
import de.kuei.metafora.xmpp.XMPPMessageTimeListener;

public class CometSmackMapping implements XMPPMessageTimeListener {

	private static CometSmackMapping mapping = null;

	private final static Lock lock = new ReentrantLock();

	public static CometSmackMapping getInstance() {
		try {
			if (mapping == null) {
				mapping = new CometSmackMapping();
			}
			return mapping;
		} catch (Exception e) {
			System.err.println("CometSmackMapping.getInstance(): "
					+ e.toString());
			return mapping;
		}
	}

	private Vector<CometServletResponse> clientChannels;

	public CometSmackMapping() throws Exception {
		try {
			if (mapping != null) {
				throw new Exception(
						"Something went wrong. There is already a mapping class.");
			}
			clientChannels = new Vector<CometServletResponse>();
		} catch (Exception e) {
			System.err.println("CometSmackMapping() constructor: "
					+ e.toString());
		}
	}

	public void registerUser(CometServletResponse response) {
		clientChannels.add(response);
	}

	public void unregisterUser(CometServletResponse response) {
		// This method is called after every write process so it is no
		// good idea to close the xmpp connection if there is no client!
		// TODO: Find a solution with a timeout of a few seconds.
		clientChannels.remove(response);
	}

	@Override
	public void newMessage(String user, String message, String chat, Date time) {
		String msg = "";

		try {
			message = message.replaceAll("\n", "");
		} catch (Exception e) {
			System.err.println(e.toString());
		}

		System.err.println("New message: " + message);

		if (message == null)
			return;

		System.err.println("Workbench.CometSmackMapping.newMessage(): "
				+ message);

		if (chat.contains("analysis")) {
			if (message
					.matches(".*[<][ ]*description[ ]*[>].*[<][ ]*[/]description[ ]*[>].*")) {
				try {
					Document doc = XMLUtils.parseXMLString(message, false);
					if (doc != null) {
						NodeList actionList = doc
								.getElementsByTagName("actiontype");
						Node actiontype = actionList.item(0);
						String type = actiontype.getAttributes()
								.getNamedItem("type").getTextContent();
						if (type.toLowerCase().equals("landmark")) {
							NodeList nl = doc
									.getElementsByTagName("description");
							for (int i = 0; i < nl.getLength(); i++) {
								Element e = (Element) nl.item(i);
								NodeList descChilds = e.getChildNodes();
								for (int j = 0; j < descChilds.getLength(); j++) {
									Node node = descChilds.item(j);
									msg += node.getTextContent();
								}
								if (i < nl.getLength() - 1) {
									msg += "\n";
								}
							}

							NodeList properties = doc
									.getElementsByTagName("property");
							String landmarkType = null;
							for (int k = 0; k < properties.getLength(); k++) {
								Node propertyNode = properties.item(k);
								String name = propertyNode.getAttributes()
										.getNamedItem("name").getTextContent();
								if (name.toLowerCase().equals("l2l2_tag")) {
									landmarkType = propertyNode.getAttributes()
											.getNamedItem("value")
											.getTextContent();
								}
							}

							msg += "##" + landmarkType;

							String l2l2Url = StartupServlet.apacheserver
									+ "/images/l2l2/generic.jpg";
							if (landmarkType != null) {
								try {
									String urlText = StartupServlet.apacheserver
											+ "/images/l2l2/"
											+ landmarkType.toLowerCase()
											+ ".jpg";
									URL url = new URL(urlText);
									HttpURLConnection con = (HttpURLConnection) url
											.openConnection();
									con.setRequestMethod("HEAD");
									if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
										l2l2Url = urlText;
									} else {
										System.err.println("Image " + urlText
												+ " not found. Status code: "
												+ con.getResponseCode());
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							msg += "##" + l2l2Url;
						}
					}
				} catch (Exception e) {
					System.err.println("Workbench.newMessage() from analysis: "
							+ e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		} else if (chat.contains("command")) {
			if (message
					.matches(".*[<][ ]*[Aa][Cc][Tt][Ii][Oo][Nn][ ]*.*[<][/][ ]*[Aa][Cc][Tt][Ii][Oo][Nn][ ]*[>].*")) {
				try {
					Document doc = XMLUtils.parseXMLString(message, false);
					NodeList action = doc.getElementsByTagName("action");
					Node actionTime = action.item(0);
					String atime = actionTime.getAttributes()
							.getNamedItem("time").getTextContent();
					long aLong = Long.valueOf(atime).longValue();
					if (aLong < System.currentTimeMillis()) {
						return;
					}

					String t;
					if (doc != null) {
						NodeList actionL = doc
								.getElementsByTagName("actiontype");
						Node actiont = actionL.item(0);
						t = actiont.getAttributes().getNamedItem("type")
								.getTextContent();

						if (t.toLowerCase().equals("login")
								|| t.toLowerCase().equals("logout")
								|| t.toLowerCase().equals("set_teamname")) {
							String userName = null;

							NodeList users = doc.getElementsByTagName("user");
							for (int i = 0; i < users.getLength(); i++) {
								Node userNode = users.item(i);
								String role = userNode.getAttributes()
										.getNamedItem("role").getTextContent();
								if (role.toLowerCase().equals("originator")) {
									userName = userNode.getAttributes()
											.getNamedItem("id")
											.getTextContent();
									break;
								}
							}

							String groupName = null;
							NodeList properties = doc
									.getElementsByTagName("property");
							for (int i = 0; i < properties.getLength(); i++) {
								Node property = properties.item(i);
								String pname = property.getAttributes()
										.getNamedItem("name").getTextContent();
								if (pname.toLowerCase().equals("group_id")) {
									groupName = property.getAttributes()
											.getNamedItem("value")
											.getTextContent();
									break;
								}
							}

							if (t.toLowerCase().equals("login")) {
								if (userName != null) {
									GroupMembers.getInstance().addGroupMember(
											groupName, userName);

								} else {
									return;
								}
							} else if (t.toLowerCase().equals("logout")) {
								if (userName != null) {
									GroupMembers.getInstance()
											.removeGroupMember(userName);
								} else {
									return;
								}
							} else if (t.toLowerCase().equals("set_teamname")) {
								if (groupName != null) {
									GroupMembers.getInstance()
											.removeGroupMember(userName);
									GroupMembers.getInstance().addGroupMember(
											groupName, userName);
								} else {
									return;
								}
							}
						} else {
							return;
						}
					}
				} catch (XMLException e) {
					System.err.println("Workbench.newMessage(): "
							+ e.getMessage());
					return;
				}
			}
		}

		System.err.println("Workbench: CometSmackMapping.newMessage(): msg = "
				+ msg);

		if (msg != null && msg.length() != 0) {

			lock.lock();

			Vector<CometServletResponse> invalidCometConnections = new Vector<CometServletResponse>();

			for (int i = 0; i < clientChannels.size(); i++) {
				try {
					clientChannels.get(i).write(
							chat + "::" + time.getTime() + "::" + msg);
					System.err
							.println("DEBUG: CometSmackMapping finally sent: "
									+ chat + "::" + time.getTime() + "::" + msg);
				} catch (IOException e) {
					System.err
							.println("Workbench: CometSmackMapping.newMessage(): Comet Error: "
									+ e.getMessage());
					invalidCometConnections.add(clientChannels.get(i));
				}
			}

			for (CometServletResponse response : invalidCometConnections) {
				clientChannels.remove(response);
			}

			lock.unlock();
		}
	}
}
