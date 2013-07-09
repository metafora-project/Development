package de.kuei.metafora.gwt.smack.client.eventServiceListeners.versions;

import com.google.gwt.user.client.ui.Anchor;

import de.kuei.metafora.gwt.smack.client.Workbench;
import de.kuei.metafora.gwt.smack.client.handler.VersionsHandler;
import de.kuei.metafora.gwt.smack.client.versions.VersionsManagement;
import de.kuei.metafora.gwt.smack.shared.event.breakingnews.BreakingNewsEvent;
import de.kuei.metafora.gwt.smack.shared.event.versions.VersionSavedEvent;
import de.novanic.eventservice.client.event.Event;

public class VersionsListenerImpl implements VersionsListener {

	private VersionsManagement vm;

	public VersionsListenerImpl(VersionsManagement vm) {
		this.vm = vm;
	}

	@Override
	public void apply(Event anEvent) {
		if (anEvent instanceof BreakingNewsEvent) {
			onVersionSavedEvent((VersionSavedEvent) anEvent);
		}
	}

	@Override
	public void onVersionSavedEvent(VersionSavedEvent anEvent) {
		Anchor anchor = new Anchor(anEvent.getMapName());
		anchor.addClickHandler(new VersionsHandler(anEvent.getMapName(),
				anEvent.getMapId(), anEvent.getURL(), anEvent
						.getVersionNumber(), Workbench.token));
		vm.add(anchor, "" + anEvent.getTimestamp());
	}

}
