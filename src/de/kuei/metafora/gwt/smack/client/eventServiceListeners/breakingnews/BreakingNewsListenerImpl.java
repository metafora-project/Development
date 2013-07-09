package de.kuei.metafora.gwt.smack.client.eventServiceListeners.breakingnews;

import de.kuei.metafora.gwt.smack.client.Development;
import de.kuei.metafora.gwt.smack.shared.event.breakingnews.BreakingNewsEvent;
import de.novanic.eventservice.client.event.Event;

public class BreakingNewsListenerImpl implements BreakingNewsListener {

	private Development development;

	public BreakingNewsListenerImpl(Development development) {
		this.development = development;
	}

	@Override
	public void apply(Event anEvent) {
		if (anEvent instanceof BreakingNewsEvent) {
			onBreakingNewsEvent((BreakingNewsEvent) anEvent);
		}
	}

	@Override
	public void onBreakingNewsEvent(BreakingNewsEvent anEvent) {
		development.add(anEvent.getDescription(), "" + anEvent.getTimestamp(),
				anEvent.getL2l2Tag(), anEvent.getLandmarkURL());
	}
}
