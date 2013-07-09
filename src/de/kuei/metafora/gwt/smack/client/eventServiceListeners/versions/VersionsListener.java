package de.kuei.metafora.gwt.smack.client.eventServiceListeners.versions;

import de.kuei.metafora.gwt.smack.shared.event.versions.VersionSavedEvent;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public interface VersionsListener extends RemoteEventListener {

	void onVersionSavedEvent(VersionSavedEvent anEvent);

}
