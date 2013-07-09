package de.kuei.metafora.gwt.smack.shared.event.versions;

public interface VersionSavedEvent {
	String getMapId();

	String getMapName();

	long getTimestamp();

	String getURL();

	String getVersionNumber();
}
