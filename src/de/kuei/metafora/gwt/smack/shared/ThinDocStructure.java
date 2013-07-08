package de.kuei.metafora.gwt.smack.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ThinDocStructure implements Serializable, IsSerializable {
	/**
	 * This class represents a document in the couchDB and serves the transport
	 * of information about the corresponding document in the couchDB from the
	 * server to the client
	 */
	private static final long serialVersionUID = -2390885606058308353L;
	private String urlPrefix = "workbench/development/fileupload?id=";

	String docname;
	String url;
	String docId;
	String time;
	
	public ThinDocStructure(){
		
	}
	

	public ThinDocStructure(String server) {
		urlPrefix = server+urlPrefix;
	}

	/**
	 * Creates a ThinDocStructure with this ID, name and time
	 * 
	 * @param docId
	 *            ID of the document
	 * @param docname
	 *            filename of the document
	 * @param time
	 *            time stamp of the document
	 */
	public ThinDocStructure(String docId, String docname, String time, String server) {
		urlPrefix = server+urlPrefix;
		
		this.docId = docId;
		this.docname = docname;
		url = urlPrefix + docId;
		this.time = time;
	}

	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public String getUrl() {
		return url;
	}

	private void setUrl(String id) {
		this.url = urlPrefix + id;
	}

	public String getDocId() {
		return docId;
	}

	/**
	 * Sets the docId of this ThinDocStructure and updates its URL
	 * 
	 * @param docId
	 *            the ID of the document
	 */
	public void setDocId(String docId) {
		this.docId = docId;
		setUrl(docId);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
