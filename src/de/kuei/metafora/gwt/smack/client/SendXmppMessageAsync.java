package de.kuei.metafora.gwt.smack.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SendXmppMessageAsync {

	void sendXmppMessage(String Message, AsyncCallback<String> callback);

	void sendOpenCommandMessage(Vector<String> users, String groupId, String challengeId, String challengeName, String name, String id, String link,
			String version, String ip, AsyncCallback<String> callback);
}
