package de.kuei.metafora.gwt.smack.client.versions;

import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;

import de.kuei.metafora.gwt.smack.client.handler.VersionsHandler;
import de.kuei.metafora.gwt.smack.shared.ThinVersionStructure;

public class VersionsCallBack implements
		AsyncCallback<List<ThinVersionStructure>> {

	/**
	 * This class represents the AsyncCallback for getting the List of
	 * ThinDocStructures from the server on client-startup
	 */
	private VersionsManagement vm;

	public VersionsCallBack(VersionsManagement _vm) {
		this.vm = _vm;
	}

	@Override
	public void onFailure(Throwable caught) {
		Window.alert("versionscallback " + caught.getMessage());
	}

	@Override
	public void onSuccess(List<ThinVersionStructure> result) {

		for (ThinVersionStructure doc : result) {
			// Create the entry in the Documents-section of the Workbench
			Anchor anchor = new Anchor(doc.getDocname() + ", version: "
					+ doc.getVersion());

			anchor.setHref(doc.getUrl());

			// TODO: add user to anchor
			anchor.addClickHandler(new VersionsHandler(doc.getDocname(), doc
					.getDocId(), doc.getUrl(), doc.getVersion()));
			String time = doc.getTime();
			vm.add(anchor, time);
		}
	}
}
