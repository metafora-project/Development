package de.kuei.metafora.gwt.smack.shared.event.versions;

import de.novanic.eventservice.client.event.Event;

public class VersionSavedEventImpl implements VersionSavedEvent, Event {

	private static final long serialVersionUID = 8276702274574701071L;

	private String mapId;
	private String mapName;
	private long timestamp;
	private String url;
	private String versionNumber;

	public VersionSavedEventImpl() {

	}

	public VersionSavedEventImpl(String mapId, String mapName, long timestamp,
			String url, String versionNumber) {
		this.mapId = mapId;
		this.mapName = mapName;
		this.timestamp = timestamp;
		this.url = url;
		this.versionNumber = versionNumber;
	}

	@Override
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	@Override
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String getURL() {
		return url;
	}

	public void setTimestamp(String url) {
		this.url = url;
	}

	@Override
	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

}
