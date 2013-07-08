package de.kuei.metafora.gwt.smack.shared;

import java.util.HashMap;

public class VersionStructure extends ThinVersionStructure {

	private static final long serialVersionUID = -2390885606058308354L;

	private String contenttype;
	private HashMap<String, String> attributes = new HashMap<String, String>();

	public VersionStructure(){
		
	}
	
	public VersionStructure(String server) {
		super(server);
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

}
