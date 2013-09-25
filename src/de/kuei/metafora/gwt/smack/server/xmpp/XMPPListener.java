package de.kuei.metafora.gwt.smack.server.xmpp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.packet.DelayInfo;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.kuei.metafora.gwt.smack.server.StartupServlet;
import de.kuei.metafora.gwt.smack.server.user.GroupMembers;
import de.kuei.metafora.gwt.smack.server.xml.XMLException;
import de.kuei.metafora.gwt.smack.server.xml.XMLUtils;
import de.kuei.metafora.gwt.smack.shared.event.breakingnews.BreakingNewsEventImpl;
import de.kuei.metafora.gwt.smack.shared.eventservice.EventServiceDomains;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.registry.EventRegistryFactory;

public class XMPPListener implements PacketListener {

	private static XMPPListener instance = null;

	public static XMPPListener getInstance() {
		if (instance == null) {
			instance = new XMPPListener();
		}
		return instance;
	}

	public void newMessage(String user, String message, String chat, Date time) {
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

		if (chat.toLowerCase().contains("analysis")) {
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

							String l2l2Tag = null;
							String iconURL = StartupServlet.apacheServer
									+ "/images/l2l2/generic.jpg";
							String description = "";
							long timestamp = time.getTime();

							NodeList nl = doc
									.getElementsByTagName("description");
							for (int i = 0; i < nl.getLength(); i++) {
								Element e = (Element) nl.item(i);
								NodeList descChilds = e.getChildNodes();
								for (int j = 0; j < descChilds.getLength(); j++) {
									Node node = descChilds.item(j);
									description += node.getTextContent();
								}
								if (i < nl.getLength() - 1) {
									description += "\n";
								}
							}

							NodeList properties = doc
									.getElementsByTagName("property");

							for (int k = 0; k < properties.getLength(); k++) {
								Node propertyNode = properties.item(k);
								String name = propertyNode.getAttributes()
										.getNamedItem("name").getTextContent();
								if (name.toLowerCase().equals("l2l2_tag")) {
									l2l2Tag = propertyNode.getAttributes()
											.getNamedItem("value")
											.getTextContent();
								}

								if (l2l2Tag != null) {
									/*
									 * test whether a specific icon exists for
									 * this L2L2-category and update iconURL
									 */
									try {
										String urlText = StartupServlet.apacheServer
												+ "/images/l2l2/"
												+ l2l2Tag.toLowerCase()
												+ ".jpg";
										URL url = new URL(urlText);
										HttpURLConnection con = (HttpURLConnection) url
												.openConnection();
										con.setRequestMethod("HEAD");
										if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
											iconURL = urlText;
										} else {
											System.err
													.println("Image "
															+ urlText
															+ " not found. Status code: "
															+ con.getResponseCode());
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

							EventRegistryFactory
									.getInstance()
									.getEventRegistry()
									.addEvent(
											DomainFactory
													.getDomain(EventServiceDomains.BREAKINGNEWSDOMAIN),
											new BreakingNewsEventImpl(
													description, l2l2Tag,
													iconURL, timestamp));
							System.err
									.println("Workbench: CometSmackMapping.newMessage(): new Event of type "
											+ l2l2Tag);

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
	}

	@Override
	public void processPacket(Packet packet) {
		try {
			handlePacket(packet);
		} catch (Exception e) {
			System.err
					.println("PlanningTool: XMPPListener: Handle packet exception! "
							+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void handlePacket(Packet packet) {
		if (packet instanceof Message) {
			Message msg = (Message) packet;

			if (msg.getBody() == null) {
				return;
			}

			String from = msg.getFrom();
			String name = "";
			String chat = from;

			int splitPos = from.indexOf('/');
			if (splitPos > 0) {
				name = from.substring(splitPos + 1, from.length());
				chat = from.substring(0, splitPos);
			}

			Date time = new Date();

			Collection<PacketExtension> extensions = packet.getExtensions();
			for (PacketExtension e : extensions) {
				if (e instanceof DelayInfo) {
					DelayInfo d = (DelayInfo) e;
					time = d.getStamp();
					break;
				} else if (e instanceof DelayInformation) {
					DelayInformation d = (DelayInformation) e;
					time = d.getStamp();
					break;
				}
			}

			newMessage(name, msg.getBody(), chat, time);
		}
	}
}
