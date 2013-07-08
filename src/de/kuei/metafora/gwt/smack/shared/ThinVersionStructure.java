package de.kuei.metafora.gwt.smack.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ThinVersionStructure implements Serializable, IsSerializable {

	private static final long serialVersionUID = -2390885606058308353L;
	private String urlPrefix = "workbench/development/fileupload?id=";

	String docname;
	String url;
	String docId;
	String time;
	// String token;
	String version;

	public ThinVersionStructure(){
		
	}
	
	public ThinVersionStructure(String server) {
		urlPrefix = server+urlPrefix;

	}

	/*
	 * public ThinVersionStructure(String docId, String docname) { this.docId =
	 * docId; this.docname = docname; url =
	 * "http://metafora.ku-eichstaett.de:8081/workbench/development/fileupload?id="
	 * + docId; }
	 */

	public ThinVersionStructure(String docId, String docname, String version,
			String time, String server) {
		urlPrefix = server+urlPrefix;
		
		this.docId = docId;
		this.docname = docname;
		url = urlPrefix + docId;
		this.version = version;
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

	/*
	 * public String getToken(){ return token; }
	 */

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
