package de.kuei.metafora.gwt.smack.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kuei.metafora.gwt.smack.client.documents.DocumentService;
import de.kuei.metafora.gwt.smack.server.xmpp.XMPPListener;
import de.kuei.metafora.gwt.smack.shared.DocStructure;
import de.kuei.metafora.gwt.smack.shared.ThinDocStructure;

public class DocIdServiceImpl extends RemoteServiceServlet implements
		DocumentService {

	public static String tomcatserver = "https://metafora-project.de";
	public static String server = "metafora-project.de";
	public static String user = "admin";
	public static String password = "password";

	private static final int port = 5984;
	private static final String databaseName = "gwtfilebase";

	// Represents the Documents in the couchDB
	private static List<ThinDocStructure> idToDoc = null;

	/**
	 * This method gets all documents with their complete "data" from the
	 * CouchDB thus causing a lot of traffic. A combination of getIdToNames()
	 * and getDocument(String) should therefore be preferred
	 * 
	 * @return All DocStructures as created in
	 *         {@link #createDocStructure(Document)} with the documents from the
	 *         CouchDB
	 * 
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#getFileIds()
	 */
	public DocStructure[] getFileIds() {

		System.out.println("getFileIds was called");

		Session session = new Session(server, port, user, password);

		Database db = session.getDatabase(databaseName);
		ViewResults alldocs = db.getAllDocuments();

		List<Document> docs = alldocs.getResults();
		System.out.println(docs.size() + " Documents found");
		DocStructure[] ids = new DocStructure[docs.size()];

		for (int i = 0; i < docs.size(); i++) {
			Document d = docs.get(i);
			try {
				d = db.getDocument(d.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}

			ids[i] = createDocStructure(d);
		}
		return ids;
	}

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
		Session session = new Session(server, port, user, password);
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
		Session session = new Session(server, port, user, password);
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
	 * This Method only calls {@link #getFileIds()}
	 * 
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#getDocIds()
	 * @see #getFileIds()
	 */
	@Override
	public DocStructure[] getDocIds() {
		return getFileIds();
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
	public List<ThinDocStructure> getIdToNames() {
		return idToDoc;
	}

	/**
	 * Gets the ThinDocStructures of all Documents in the CouchDB
	 * 
	 */
	public static synchronized void setIdToDoc() {
		Session session = new Session(server, port, user, password);
		Database db = session.getDatabase(databaseName);

		// ViewResults results = db.view("development/idToNameTime");
		ViewResults results = db.view("development/nameToTime");

		idToDoc = new ArrayList<ThinDocStructure>();

		if (results != null) {
			for (Document doc : results.getResults()) {
				String id = doc.getId();

				String filename = doc.getString("key");
				String time = doc.getString("value");

				/*
				 * typical result-document: {_id =
				 * aa7f5d7634c2606e003cbbd78400721a, key=" foo.doc",
				 * value="1277942400000"}
				 */
				ThinDocStructure thinDoc = new ThinDocStructure(id, filename,
						time, "http://" + tomcatserver + "/");

				idToDoc.add(thinDoc);
			}
		}
	}

	/**
	 * Gets the Document with the specified ID from the CouchDB
	 * 
	 * @param id
	 *            ID of the document
	 * @return The Document from the CouchDB
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#getDocument(java.lang.String)
	 */
	@Override
	public DocStructure getDocument(String id) {
		Session session = new Session(server, port, user, password);
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
	 * Gets the ThinDocStructure of the document with the specified ID
	 * 
	 * @param id
	 *            ID of the document
	 * @return ThinDocStructure of the document or null if the document could
	 *         not be found
	 */
	public ThinDocStructure getThinDocument(String id) {
		for (ThinDocStructure thinDoc : idToDoc) {
			if (thinDoc.getDocId().equals(id))
				return thinDoc;
		}

		return null;
	}

	/**
	 * Transfers the values of "filename", "filetype", "ID", "data" and other
	 * keys from the Document to this DocStructure
	 * 
	 * @param doc
	 *            Document from which the information should be transferred
	 */
	private DocStructure createDocStructure(Document doc) {
		DocStructure docStructure = new DocStructure("http://" + tomcatserver
				+ "/");

		if (doc.containsKey("filename")) {
			docStructure.setDocname(doc.getString("filename"));
		}

		if (doc.containsKey("filetype"))
			docStructure.setContenttype(doc.getString("filetype"));

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

	/**
	 * This method adds the ThinDocStructure of the Document with these
	 * parameters to idToDoc and updates the Documents-View of the clients
	 * 
	 * @param fileId
	 *            ID of the document
	 * @param filename
	 *            filename of the document
	 * @param time
	 *            time stamp of the document
	 * 
	 * @see de.kuei.metafora.gwt.smack.client.documents.DocumentService#onDocumentPut(java.lang.String)
	 */
	public synchronized static void onDocumentPut(String fileId,
			String filename, Date date) {
		long time = date.getTime();
		idToDoc.add(new ThinDocStructure(fileId, filename, "" + time, "http://"
				+ tomcatserver + "/"));

		// propagate changes to all clients
		try {
			/*
			 * send a message with the ID of the document such as:
			 * "newDoc:12h23j436lkj3kj::foo.doc::1277942400000"
			 */
			XMPPListener.getInstance().newMessage("DocIdServiceImpl",
					fileId + ";" + filename, "DocIdService", date);
		} catch (Exception e) {
			System.err.println("DocIdServiceImpl.onDocumentPut(): "
					+ e.toString());
		}
	}

	@Override
	public String getTomcatServer() {
		return StartupServlet.tomcatServer;
	}
}