package de.kuei.metafora.gwt.smack.shared.event.documents;

import de.novanic.eventservice.client.event.Event;

public class DocSavedEventImpl implements DocSavedEvent, Event {

	private static final long serialVersionUID = 8276702274574701071L;

	private String docId;
	private String docName;
	private long timestamp;

	public DocSavedEventImpl() {

	}

	public DocSavedEventImpl(String docId, String mapName, long timestamp,
			String versionNumber) {
		this.docId = docId;
		this.docName = mapName;
		this.timestamp = timestamp;
	}

	@Override
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	@Override
	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
