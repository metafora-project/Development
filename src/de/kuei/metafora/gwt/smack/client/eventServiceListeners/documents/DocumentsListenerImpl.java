package de.kuei.metafora.gwt.smack.client.eventServiceListeners.documents;

import com.google.gwt.user.client.ui.Anchor;

import de.kuei.metafora.gwt.smack.client.documents.Documents;
import de.kuei.metafora.gwt.smack.shared.event.breakingnews.BreakingNewsEvent;
import de.kuei.metafora.gwt.smack.shared.event.documents.DocSavedEvent;
import de.novanic.eventservice.client.event.Event;

public class DocumentsListenerImpl implements DocumentsListener {

	private Documents documents;

	public DocumentsListenerImpl(Documents documents) {
		this.documents = documents;
	}

	@Override
	public void apply(Event anEvent) {
		if (anEvent instanceof BreakingNewsEvent) {
			onDocSavedEvent((DocSavedEvent) anEvent);
		}
	}

	@Override
	public void onDocSavedEvent(DocSavedEvent anEvent) {
		Anchor a = new Anchor(anEvent.getDocId());
		a.setTarget("_parent");
		documents.add(anEvent.getDocName(), a, "" + anEvent.getTimestamp());
	}

}
