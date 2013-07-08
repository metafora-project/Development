package de.kuei.metafora.gwt.smack.client.versions;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kuei.metafora.gwt.smack.client.documents.DocumentService;
import de.kuei.metafora.gwt.smack.shared.ThinVersionStructure;
import de.kuei.metafora.gwt.smack.shared.VersionStructure;

public interface VersionsServiceAsync {

	/**
	 * @see DocumentService#getDocIds()
	 */
	void getDocIds(AsyncCallback<String[]> callback);

	/**
	 * @see DocumentService#getIp()
	 */
	// void getToken(String id, AsyncCallback<String> callback);

	/**
	 * @see DocumentService#getVersion()
	 */
	void getVersion(String id, AsyncCallback<String> callback);

	/**
	 * @see DocumentService#getIdToNames(String)
	 */
	void getIdToNames(AsyncCallback<List<ThinVersionStructure>> callback);

	/**
	 * @see DocumentService#getDocument(String)
	 */
	void getDocument(String id, AsyncCallback<VersionStructure> callback);

	/**
	 * @see DocumentService#setAttribute(String, String, String)
	 */
	void setAttribute(String docId, String key, String value,
			AsyncCallback<Void> callback);

	/**
	 * @see DocumentService#getRevisions(String)
	 */
	void getRevisions(String docId, AsyncCallback<String[]> callback);

}
