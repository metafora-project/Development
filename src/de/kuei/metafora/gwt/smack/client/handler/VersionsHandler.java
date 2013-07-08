package de.kuei.metafora.gwt.smack.client.handler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kuei.metafora.gwt.smack.client.SendXmppMessage;
import de.kuei.metafora.gwt.smack.client.SendXmppMessageAsync;
import de.kuei.metafora.gwt.smack.client.Workbench;

public class VersionsHandler implements ClickHandler {

	private final SendXmppMessageAsync sendXmppMessage = GWT
			.create(SendXmppMessage.class);
	String name;
	String id;
	String version;
	String link;
	String token;

	public VersionsHandler(String name, String id, String link, String version,
			String token) {
		this.name = name;
		this.id = id;
		this.version = version;
		this.link = link;
		this.token = token;
	}

	public VersionsHandler(String name, String id, String link, String version) {
		this.name = name;
		this.id = id;
		this.version = version;
		this.link = link;
	}

	@Override
	public void onClick(ClickEvent event) {
		sendXmppMessage.sendOpenCommandMessage(Workbench.users,
				Workbench.groupId, Workbench.challengeId,
				Workbench.challengeName, name, id, link, version, token,
				new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});

	}
}
