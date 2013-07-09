package de.kuei.metafora.gwt.smack.shared.event.breakingnews;

import de.novanic.eventservice.client.event.Event;

public class BreakingNewsEventImpl implements BreakingNewsEvent, Event {

	private static final long serialVersionUID = -8784611557193776002L;

	private String description;
	private String l2l2Tag;
	private String landmarkURL;
	private long timestamp;

	public BreakingNewsEventImpl() {

	}

	public BreakingNewsEventImpl(String description, String l2l2Tag,
			String landmarkURL, long timestamp) {
		this.description = description;
		this.l2l2Tag = l2l2Tag;
		this.landmarkURL = landmarkURL;
		this.timestamp = timestamp;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getL2l2Tag() {
		return l2l2Tag;
	}

	public void setl2l2Tag(String l2l2Tag) {
		this.l2l2Tag = l2l2Tag;
	}

	@Override
	public String getLandmarkURL() {
		return landmarkURL;
	}

	public void setLandmarkURL(String landmarkURL) {
		this.landmarkURL = landmarkURL;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
