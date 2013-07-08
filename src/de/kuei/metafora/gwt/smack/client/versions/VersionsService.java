package de.kuei.metafora.gwt.smack.client.versions;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kuei.metafora.gwt.smack.shared.ThinVersionStructure;
import de.kuei.metafora.gwt.smack.shared.VersionStructure;

@RemoteServiceRelativePath("versions")
public interface VersionsService extends RemoteService {

	/**
	 * This Method gets all documents with their complete "data" from the
	 * CouchDB thus causing a lot of traffic. A combination of getIdToNames()
	 * and getDocument(String) should therefore be preferred
	 * 
	 * @return DocStructures of all the documents in the couchDB
	 */
	public String[] getDocIds();

	/**
	 * Returns the ip of a document.
	 * 
	 * @param id
	 * @return ip
	 */
	// public String getToken(String id);

	/**
	 * Returns the versionnumber of a document.
	 * 
	 * @param id
	 * @return versionnumber
	 */
	public String getVersion(String id);

	/**
	 * This method gets the servers representation of the Documents in the
	 * couchDB
	 * 
	 * @return An ArrayList-Object as described
	 */
	public List<ThinVersionStructure> getIdToNames();

	/**
	 * Gets the Document with the specified ID from the CouchDB
	 * 
	 * @param id
	 *            ID of the document
	 * @return The Document from the CouchDB
	 */
	public VersionStructure getDocument(String id);

	/**
	 * Sets the attribute with this key of the Document with the given docId to
	 * this value
	 * 
	 * @param docId
	 *            ID of the Document in the couchDB
	 * @param key
	 *            key of the attribute
	 * @param value
	 *            value to which the attribute is to be set
	 */
	public void setAttribute(String docId, String key, String value);

	/**
	 * gets the Revisions of the Document with the specified ID from the couchDB
	 * 
	 * @param fileId
	 *            ID of the Document in the couchDB
	 * @return String-Array with the Revisions or null, if the Document could
	 *         not be found in the couchDB
	 */
	public String[] getRevisions(String docId);
}
