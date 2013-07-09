package de.kuei.metafora.gwt.smack.shared.event.documents;

public interface DocSavedEvent {
	String getDocId();

	String getDocName();

	long getTimestamp();
}
