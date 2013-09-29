package de.kuei.metafora.gwt.smack.server;

import java.util.Vector;

import javax.servlet.http.HttpServlet;

import de.kuei.metafora.gwt.smack.server.mysql.ChannelDescription;
import de.kuei.metafora.gwt.smack.server.mysql.MysqlInitConnector;
import de.kuei.metafora.gwt.smack.server.mysql.ServerDescription;
import de.kuei.metafora.gwt.smack.server.xmpp.XMPPListener;
import de.kuei.metafora.xmppbridge.xmpp.NameConnectionMapper;
import de.kuei.metafora.xmppbridge.xmpp.ServerConnection;
import de.kuei.metafora.xmppbridge.xmpp.XmppMUC;
import de.kuei.metafora.xmppbridge.xmpp.XmppMUCManager;

public class StartupServlet extends HttpServlet {

	public static String moduleName = "Workbench";
	public static String sending_tool = "WORKBENCH";
	public static String metafora = "METAFORA";
	public static boolean logged = true;

	public static String tomcatServer = "https://metaforaserver.ku.de";
	public static String couchDbServer = "metaforaserver.ku-eichstaett.de";
	public static String apacheServer = "http://metaforaserver.ku.de";
	public static String xmpp = "metaforaserver.ku.de";
	
	private static XmppMUC logger;
	private static XmppMUC analysis;
	private static XmppMUC command;

	public void init() {
		System.err.println("Workbench StartupServlet init...");

		MysqlInitConnector.getInstance().loadData(moduleName);

		System.err.println("Loading mysql init parameter....");

		sending_tool = MysqlInitConnector.getInstance().getParameter(
				"SENDING_TOOL");

		metafora = MysqlInitConnector.getInstance().getParameter("METAFORA");

		if (MysqlInitConnector.getInstance().getParameter("logged")
				.toLowerCase().equals("false")) {
			logged = false;
		}

		ServerDescription apacheServerDesc = MysqlInitConnector.getInstance()
				.getAServer("apache");

		apacheServer = apacheServerDesc.getServer();

		System.err.println("SENDING_TOOL: " + sending_tool);
		System.err.println("METAFORA: " + metafora);
		System.err.println("logged: " + logged);

		System.err.println("Loading mysql init server data...");

		ServerDescription couchDBServerDesc = MysqlInitConnector.getInstance()
				.getAServer("couchdb");
		ServerDescription tomcatServerDesc = MysqlInitConnector.getInstance()
				.getAServer("tomcat");

		StartupServlet.couchDbServer = couchDBServerDesc.getServer();
		
		
		StartupServlet.tomcatServer = tomcatServerDesc.getServer();

		System.err.println("Config DocIdServiceImpl...");

		// config DocIdServiceImpl
		DocIdServiceImpl.tomcatserver = tomcatServerDesc.getServer();

		DocIdServiceImpl.server = couchDBServerDesc.getServer();
		DocIdServiceImpl.user = couchDBServerDesc.getUser();
		DocIdServiceImpl.password = couchDBServerDesc.getPassword();

		System.err.println("Config VersionsServiceImpl...");

		// config VersionsServiceImpl
		VersionsServiceImpl.tomcatserver = tomcatServerDesc.getServer();

		VersionsServiceImpl.server = couchDBServerDesc.getServer();
		VersionsServiceImpl.user = couchDBServerDesc.getUser();
		VersionsServiceImpl.password = couchDBServerDesc.getPassword();

		System.err.println("Config DocUploadServlet...");

		// config DocUploadServlet
		DocUploadServlet.server = couchDBServerDesc.getServer();
		DocUploadServlet.user = couchDBServerDesc.getUser();
		DocUploadServlet.password = couchDBServerDesc.getPassword();

		System.err.println("Config XMPP...");

		// configure xmpp
		Vector<ServerDescription> xmppServers = MysqlInitConnector
				.getInstance().getServer("xmpp");

		// should be the same server but different accounts
		if (xmppServers.size() > 0)
			xmpp = xmppServers.firstElement().getServer();

		for (ServerDescription xmppServer : xmppServers) {
			System.err.println("XMPP server: " + xmppServer.getServer());
			System.err.println("XMPP user: " + xmppServer.getUser());
			System.err.println("XMPP password: " + xmppServer.getPassword());
			System.err.println("XMPP device: " + xmppServer.getDevice());
			System.err.println("Modul: " + xmppServer.getModul());

			System.err.println("Starting XMPP connection...");

			NameConnectionMapper.getInstance().createConnection(
					xmppServer.getConnectionName(), xmppServer.getServer(),
					xmppServer.getUser(), xmppServer.getPassword(),
					xmppServer.getDevice());

			NameConnectionMapper.getInstance()
					.getConnection(xmppServer.getConnectionName())
					.addPacketListener(new XMPPListener());

			NameConnectionMapper.getInstance()
					.getConnection(xmppServer.getConnectionName()).login();
		}

		Vector<ChannelDescription> channels = MysqlInitConnector.getInstance()
				.getXMPPChannels();

		for (ChannelDescription channeldesc : channels) {
			ServerConnection connection = NameConnectionMapper.getInstance()
					.getConnection(channeldesc.getConnectionName());

			if (connection == null) {
				System.err.println("StartupServlet: Unknown connection: "
						+ channeldesc.getUser());
				continue;
			}

			System.err.println("Joining channel " + channeldesc.getChannel()
					+ " as " + channeldesc.getAlias());

			XmppMUC muc = XmppMUCManager.getInstance().getMultiUserChat(
					channeldesc.getChannel(), channeldesc.getAlias(),
					connection);
			muc.join(0);

			if (channeldesc.getChannel().equals("logger")) {
				System.err.println("StartupServlet: logger configured.");
				logger = muc;
			} else if (channeldesc.getChannel().equals("analysis")) {
				System.err.println("StartupServlet: analysis configured.");
				analysis = muc;
			} else if (channeldesc.getChannel().equals("command")) {
				System.err.println("StartupServlet: command configured.");
				command = muc;
			}
		}

		DocIdServiceImpl.setIdToDoc();
		VersionsServiceImpl.setIdToDoc();
	}

	public static void sendToLogger(String message) {
		if (logger != null) {
			logger.sendMessage(message);
		} else {
			System.err
					.println("Logger channel was not initalized! Message lost:\n"
							+ message);
		}
	}

	public static void sendToCommand(String message) {
		if (command != null) {
			command.sendMessage(message);
		} else {
			System.err
					.println("Command channel was not initalized! Message lost:\n"
							+ message);
		}
	}

	public static void sendToAnalysis(String message) {
		if (analysis != null) {
			analysis.sendMessage(message);
		} else {
			System.err
					.println("Analysis channel was not initalized! Message lost:\n"
							+ message);
		}
	}

	@Override
	public void destroy() {
		Vector<ServerDescription> xmppServers = MysqlInitConnector
				.getInstance().getServer("xmpp");

		for (ServerDescription xmppServer : xmppServers) {
			NameConnectionMapper.getInstance()
					.getConnection(xmppServer.getConnectionName()).disconnect();
		}
	}
}