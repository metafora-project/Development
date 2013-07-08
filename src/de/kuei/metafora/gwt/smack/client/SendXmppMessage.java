package de.kuei.metafora.gwt.smack.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("xmpp")
public interface SendXmppMessage extends RemoteService {

	public String sendXmppMessage(String Message);

	public String sendOpenCommandMessage(Vector<String> users, String groupId, String challengeId, String challengeName, String name, String id, String link,
			String version, String ip);
}
