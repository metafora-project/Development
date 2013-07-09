package de.kuei.metafora.gwt.smack.client.eventServiceListeners.documents;

import de.kuei.metafora.gwt.smack.shared.event.documents.DocSavedEvent;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public interface DocumentsListener extends RemoteEventListener {

	void onDocSavedEvent(DocSavedEvent anEvent);

}
