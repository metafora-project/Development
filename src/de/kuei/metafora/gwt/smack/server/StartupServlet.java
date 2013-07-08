package de.kuei.metafora.gwt.smack.server;

import javax.servlet.http.HttpServlet;

import de.kuei.metafora.xmpp.XMPPBridge;
import de.kuei.metafora.xmpp.XMPPBridgeCurrent;

public class StartupServlet extends HttpServlet {

	private static final boolean test = true;
	// this flag also change the database server
	public static final boolean productive = false;

	public static String couchDbServer = "metafora.ku-eichstaett.de";
	public static String tomcatServer = "metafora.ku-eichstaett.de:8443";
	public static String apacheserver = "http://metafora.ku.de";

	public void init() {

		// change couchdb if productive
		if (productive) {
			couchDbServer = "metaforaserver.ku-eichstaett.de";
			tomcatServer = "metaforaserver.ku-eichstaett.de:8080";
			apacheserver = "http://metaforaserver.ku.de";
		}

		String commandDevice = "workbenchCommandListener";
		String commandAlias = "WorkbenchCommand";
		String loggerDevice = "workbenchLoggerListener";
		String loggerAlias = "WorkbenchLogger";
		String analysisDevice = "workbenchAnalysisListener";
		String analysisAlias = "WorkbenchAnalysis";

		if (productive) {
			commandDevice += "Productive";
			commandAlias += "Productive";
			loggerDevice += "Productive";
			loggerAlias += "Productive";
			analysisDevice += "Productive";
			analysisAlias += "Productive";
		}

		if (test) {
			commandDevice += "Test";
			commandAlias += "Test";
			loggerDevice += "Test";
			loggerAlias += "Test";
			analysisDevice += "Test";
			analysisAlias += "Test";
		}

		if (productive) {
			XMPPBridgeCurrent.setServer("metaforaserver.ku.de");
			XMPPBridge.createConnection("workbenchanalysis",
					"workbenchAnalysis", Passwords.PWORKBENCHANALYSYS,
					"analysis@conference.metaforaserver.ku.de", analysisAlias,
					analysisDevice);

			XMPPBridge.getConnection("workbenchanalysis").connectToChat();
			XMPPBridge.getConnection("workbenchanalysis").registerTimeListener(
					CometSmackMapping.getInstance());

			XMPPBridge.createConnection("workbenchcommand", "workbenchCommand",
					Passwords.PWORKBENCHCOMMAND,
					"command@conference.metaforaserver.ku.de", commandAlias,
					commandDevice);

			XMPPBridge.getConnection("workbenchcommand").connectToChat();
			XMPPBridge.getConnection("workbenchcommand").registerTimeListener(
					CometSmackMapping.getInstance());

			XMPPBridge.createConnection("workbenchlogger", "workbenchLogger",
					Passwords.PWORKBENCHLOGGER,
					"logger@conference.metaforaserver.ku.de", loggerAlias,
					loggerDevice);

			XMPPBridge.getConnection("workbenchlogger").connectToChat();
		} else {
			XMPPBridge.createConnection("workbenchanalysis",
					"workbenchAnalysis", Passwords.WORKBENCHANALYSYS,
					"analysis@conference.metafora.ku-eichstaett.de",
					analysisAlias, analysisDevice);

			XMPPBridge.getConnection("workbenchanalysis").connectToChat();
			XMPPBridge.getConnection("workbenchanalysis").registerTimeListener(
					CometSmackMapping.getInstance());

			XMPPBridge.createConnection("workbenchcommand", "workbenchCommand",
					Passwords.WORKBENCHCOMMAND,
					"command@conference.metafora.ku-eichstaett.de",
					commandAlias, commandDevice);

			XMPPBridge.getConnection("workbenchcommand").connectToChat();
			XMPPBridge.getConnection("workbenchcommand").registerTimeListener(
					CometSmackMapping.getInstance());

			XMPPBridge.createConnection("workbenchlogger", "workbenchLogger",
					Passwords.WORKBENCHLOGGER,
					"logger@conference.metafora.ku-eichstaett.de", loggerAlias,
					loggerDevice);

			XMPPBridge.getConnection("workbenchlogger").connectToChat();
		}

		DocIdServiceImpl.setIdToDoc();
		VersionsServiceImpl.setIdToDoc();
	}
}