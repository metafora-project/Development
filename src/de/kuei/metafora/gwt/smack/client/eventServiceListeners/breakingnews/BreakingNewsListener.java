package de.kuei.metafora.gwt.smack.client.eventServiceListeners.breakingnews;

import de.kuei.metafora.gwt.smack.shared.event.breakingnews.BreakingNewsEvent;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

public interface BreakingNewsListener extends RemoteEventListener {
	
	void onBreakingNewsEvent(BreakingNewsEvent anEvent);
	
}
