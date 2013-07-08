package de.kuei.metafora.gwt.smack.client.documents;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;

import de.kuei.metafora.gwt.smack.shared.ThinDocStructure;

public class DocumentCallBack implements AsyncCallback<List<ThinDocStructure>> {

	/**
	 * This class represents the AsyncCallback for getting the List of
	 * ThinDocStructures from the server on client-startup
	 */
	private Documents doctable;

	public DocumentCallBack(Documents doctable) {
		this.doctable = doctable;
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(List<ThinDocStructure> result) {
		for (ThinDocStructure doc : result) {
			// Create the entry in the Documents-section of the Workbench

			String time = doc.getTime();
			Anchor anchor = new Anchor(doc.getDocId());
			anchor.setTarget("_parent");
			doctable.add(doc.getDocname(), anchor, time);
		}
	}
}
