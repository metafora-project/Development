package de.kuei.metafora.gwt.smack.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kuei.metafora.gwt.smack.client.versions.VersionsService;
import de.kuei.metafora.gwt.smack.shared.ThinVersionStructure;
import de.kuei.metafora.gwt.smack.shared.VersionStructure;

public class VersionsServiceImpl extends RemoteServiceServlet implements
		VersionsService {
	/**
	 * Implementation of the VersionsService-interface to represent the
	 * couchDB-knowledge about versions of planningtool-maps
	 */
	// TODO Put versions to VersionsMan. that are created at Workbench-Runtime?

	// Represents the Documents in the couchDB
	private static List<ThinVersionStructure> idToDoc = null;
	private static final String databaseName = "gwtfilebase";

	// /**
	// * This Method gets all documents with their complete "data" from the
	// * CouchDB thus causing a lot of traffic. A combination of getIdToNames()
	// * and getDocument(String) should therefore be preferred
	// *
	// * @return All DocStructures as created in
	// * {@link #createDocStructure(Document)} with the documents from the
	// * CouchDB
	// *
	// * @see de.kuei.metafora.gwt.smack.client.DocumentService#getFileIds()
	// */
	// public VersionStructure[] getFileIds() {
	//
	// System.out.println("getFileIds was called");
	//
	// Session session = new Session(server, port, "admin", Passwords.COUCHDB);
	//
	// Database db = session.getDatabase(databaseName);
	// ViewResults alldocs = db.getAllDocuments();
	// // Roter Consolenausdruck (n�chste Zeilen)
	// List<Document> docs = alldocs.getResults();
	// System.out.println(docs.size() + " Documents found");
	// VersionStructure[] ids = new VersionStructure[docs.size()];
	//
	// for (int i = 0; i < docs.size(); i++) {
	// Document d = docs.get(i);
	// try {
	// d = db.getDocument(d.getId());
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// ids[i] = createDocStructure(d);
	// }
	// return ids;
	// }

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
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#setAttribute(java.lang
	 *      .String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setAttribute(String docId, String key, String value) {
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB, false, false);
		Database db = session.getDatabase(databaseName);

		try {
			Document d = db.getDocument(docId);
			if (d != null) {
				d.put(key, value);
				db.saveDocument(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets the Revisions of the Document with the specified ID from the couchDB
	 * 
	 * @param fileId
	 *            ID of the Document in the couchDB
	 * @return String-Array with the Revisions or null, if the Document could
	 *         not be found in the couchDB
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#getRevisions()
	 */
	@Override
	public String[] getRevisions(String fileId) {
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB, false, false);
		Database db = session.getDatabase(databaseName);

		try {
			Document d = db.getDocument(fileId);
			if (d != null)
				return d.getRevisions();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets the document IDs of all planningtool-map-versions in the couchDB
	 */
	@Override
	public String[] getDocIds() {
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB, false, false);

		Database db = null;
		db = session.getDatabase(databaseName);

		ViewResults results = db.view("pt-maps/map_ids");

		String[] ids = new String[results.getResults().size()];
		int i = 0;
		for (Document doc : results.getResults()) {
			ids[i] = doc.getId();
			i++;
		}

		return ids;
	}

	/**
	 * This method gets the DocIdServiceImpl-representation of the Documents in
	 * the couchDB calling {@link #setIdToDoc()} if there exists no
	 * representation yet
	 * 
	 * @return An ArrayList-Object as described
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#getIdToNames()
	 * @see #setIdToDoc()
	 */
	@Override
	public List<ThinVersionStructure> getIdToNames() {
		return idToDoc;
	}

	/**
	 * Gets the ThinDocStructures of all Documents in the CouchDB and puts them
	 * into idToDoc
	 */
	public static synchronized void setIdToDoc() {
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB, false, false);
		Database db = session.getDatabase(databaseName);

		// ViewResults results = db.view("pt-maps/nameToVersions");
		ViewResults results = db.view("pt-maps/nameToVersionTime");

		idToDoc = new ArrayList<ThinVersionStructure>();

		for (Document doc : results.getResults()) {

			String id = doc.getId();
			String filename = doc.getString("key");
			String[] values = doc.getString("value").split(";");

			/*
			 * typical result-document: {_id = aa7f5d7634c2606e003cbbd78400721a,
			 * key=" foo", value="0;1277942400000"}
			 * 
			 * values[0] = "0" (version); values[1] = "1277942400000" (time)
			 */
			ThinVersionStructure thinDoc = new ThinVersionStructure(id,
					filename, values[0], values[1], "http://"
							+ StartupServlet.tomcatServer + "/");

			idToDoc.add(thinDoc);
		}
	}

	/**
	 * Gets the whole Document with the specified ID from the CouchDB
	 * 
	 * @param id
	 *            ID of the document
	 * @return The VersionStructure of the document from the CouchDB
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#getDocument(java.lang.String)
	 */
	@Override
	public VersionStructure getDocument(String id) {
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB, false, false);
		Database db = session.getDatabase(databaseName);

		Document doc = null;
		try {
			doc = db.getDocument(id);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return createDocStructure(doc);
	}

	/**
	 * Gets the ip to a id.
	 * 
	 * @param id
	 *            = ID of the document
	 * @return ip of the document
	 */
	/*
	 * @Override public String getToken(String id) { Session session = new
	 * Session(server, port, "admin", Passwords.COUCHDB); Database db =
	 * session.getDatabase(databaseName); String token = "0";
	 * 
	 * Document doc = null; try { doc = db.getDocument(id); } catch (IOException
	 * e) { e.printStackTrace(); }
	 * 
	 * if(doc != null && doc.containsKey("ip")){ token = doc.getString("ip"); }
	 * return token; }
	 */

	/**
	 * Gets the Version number of the document with the specified ID (directly
	 * from the couchDB!) or -1 if this document is not a map or doesn't exist
	 * 
	 * @param id
	 *            ID of the document
	 * @return version number of the document or -1
	 */
	public String getVersion(String id) {
		String version = "-1";
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB, false, false);

		Database db = null;
		db = session.getDatabase(databaseName);

		Document doc = null;
		try {
			doc = db.getDocument(id);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (doc != null && doc.containsKey("version")) {
			version = doc.getString("version");
		}

		return version;
	}

	/**
	 * Transfers the values of "filename", "filetype", "ID", "data" and other
	 * keys from the Document to this DocStructure
	 * 
	 * @param doc
	 *            Document from which the information should be transferred
	 */
	private VersionStructure createDocStructure(Document doc) {
		VersionStructure docStructure = new VersionStructure("http://"
				+ StartupServlet.tomcatServer + "/");

		if (doc.containsKey("filename")) {
			docStructure.setDocname(doc.getString("filename"));
		}

		if (doc.containsKey("version")) {
			docStructure.setVersion(doc.getString("version"));
		}

		if (doc.containsKey("filetype"))
			docStructure.setContenttype(doc.getString("filetype"));

		if (doc.containsKey("time"))
			docStructure.setTime(doc.getString("time"));

		docStructure.setDocId(doc.getId());

		Iterator iter = doc.keys();
		while (iter.hasNext()) {
			Object key = iter.next();
			if (!"_id".equals(key) && !"_rev".equals(key)
					&& !"data".equals(key) && !"filename".equals(key)
					&& !"filetype".equals(key))
				docStructure.setAttribute(key.toString(), doc.get(key)
						.toString());
		}
		return docStructure;
	}

}
