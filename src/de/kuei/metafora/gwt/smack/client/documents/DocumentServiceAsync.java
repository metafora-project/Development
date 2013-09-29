package de.kuei.metafora.gwt.smack.client.documents;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kuei.metafora.gwt.smack.shared.DocStructure;
import de.kuei.metafora.gwt.smack.shared.ThinDocStructure;

public interface DocumentServiceAsync {

	/**
	 * @see DocumentService#getDocIds()
	 */
	void getDocIds(AsyncCallback<DocStructure[]> callback);

	/**
	 * @see DocumentService#getRevisions(String)
	 */
	void getRevisions(String docId, AsyncCallback<String[]> callback);

	/**
	 * @see DocumentService#setAttribute(String, String, String)
	 */
	void setAttribute(String docId, String key, String value,
			AsyncCallback<Void> callback);

	/**
	 * @see DocumentService#getIdToNames(String)
	 */
	void getIdToNames(AsyncCallback<List<ThinDocStructure>> callback);

	/**
	 * @see DocumentService#getDocument(String)
	 */
	void getDocument(String id, AsyncCallback<DocStructure> callback);

	/**
	 * @see DocumentService#getThinDocument(String)
	 */
	void getThinDocument(String id, AsyncCallback<ThinDocStructure> callback);

	void getTomcatServer(AsyncCallback<String> callback);
}
