package de.kuei.metafora.gwt.smack.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;

public class DocUploadServlet extends HttpServlet {
	// private static final String server = "141.78.99.238";
	public static String server = StartupServlet.couchDbServer;
	private static final int port = 5984; // wegen: http://127.0.0.1:5984/...
	private static final String databaseName = "gwtfilebase"; // Name der
																// Datenbank
	private String docdata = null; // im anderen Filedata genannt
	private String contenttype = null;

	public void doGet(HttpServletRequest req, HttpServletResponse response) {

		if (req.getParameterMap().containsKey("id")) {
			String id = req.getParameter("id");
			System.out.println("file " + id + " requested");

			String[] doc = getDocFromDatabase(id);

			if (doc != null) {
				response.setContentType(doc[0]);
				try {
					PrintWriter writer = response.getWriter();
					writer.write(doc[1]);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				response.setContentType("text/plain");
				try {
					response.getWriter().write("file not found");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else if (docdata != null && contenttype != null) {
			response.setContentType(contenttype);
			try {
				PrintWriter writer = response.getWriter();
				writer.write(new String(docdata));
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			response.setContentType("text/plain");
			try {
				response.getWriter().write("no file aviable");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/plain");

		String responseText = "";

		if (ServletFileUpload.isMultipartContent(request)) {

			boolean update = false;
			if (request.getParameterMap().containsKey("fileid"))
				update = true;

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			System.out.println(responseText += "Upload from "
					+ request.getRemoteAddr() + ", ");
			System.out.println(responseText += "Size: "
					+ request.getContentLength() + " Bytes, ");

			try {
				List<FileItem> items = upload.parseRequest(request);

				String groupId = null, challengeId = null, challengeName = null;
				Vector<String> users = null;

				for (FileItem item : items) {
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						if (name.equals("userName")) {
							users = new Vector<String>();
							if (users.contains("|")) {
								String[] splittedArray = value.split("\\|");

								for (int b = 0; b < splittedArray.length; b++) {
									String userValue = splittedArray[b];
									users.add(userValue);
								}
							} else {
								users.add(value);
							}
						} else if (name.equals("groupId")) {
							groupId = value;
						} else if (name.equals("challengeId")) {
							challengeId = value;
						} else if (name.equals("challengeName")) {
							challengeName = value;
						}
						continue;
					}

					contenttype = item.getContentType();
					System.out.println(responseText += "Filename: "
							+ item.getName() + ", ");
					docdata = item.getString();
					System.out.println(responseText += "Bytes: "
							+ item.getSize());

					if (update) {
						updateDoc(item.getName(), item.getContentType(),
								item.getString(),
								request.getParameter("fileid"));

					} else {
						putDocToDatabase(item.getName(), item.getContentType(),
								item.getString());

						if (users != null && groupId != null
								&& challengeId != null && challengeName != null) {
							DocUploadIndicator
									.getInstance()
									.sendUploadIndicator(users, item.getName(),
											groupId, challengeId, challengeName);
						} else {
							System.err
									.println("DocUploadServlet.doPost: No Indicator could be sent, missing user information");
						}
					}

					break;
				}

			} catch (FileUploadException e1) {
				e1.printStackTrace();
			}
		} else {
			responseText = "there was no document!";
		}

		try {
			response.getWriter().write(responseText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the filetype and data of the Document with the specified ID from the
	 * couchDB. Meant for downloading the files.
	 * 
	 * @param id
	 *            ID of the Document to be got
	 * @return An array of length 2 with the filetype in 0-coordinate and the
	 *         data or null, if there was a problem retrieving the Document from
	 *         the couchDB
	 */
	private String[] getDocFromDatabase(String id) {
		String[] doc = null;

		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB);

		Database db = null;
		db = session.getDatabase(databaseName);
		try {
			Document dc = db.getDocument(id);
			if (dc != null) {
				Set keys = dc.keySet();
				if (keys.contains("filetype") && keys.contains("data")) {
					doc = new String[2];
					doc[0] = dc.getString("filetype");
					doc[1] = dc.getString("data");

					System.out.println("file found: " + doc[1].length()
							+ " Bytes, Type: " + doc[0]);

				} else {
					System.out.println("invalid document!");
				}
			} else {
				System.out.println("document not found!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Creates and saves a new file with the specified name, type and data into
	 * the couchDB
	 * 
	 * @param docname
	 *            Name under which the file is to be saved
	 * @param doctype
	 *            Type of the file to be saved
	 * @param docdata
	 *            Conten of the file to be saved
	 */
	private void putDocToDatabase(String docname, String doctype, String docdata) {
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB);

		Database db = null;
		db = session.getDatabase(databaseName);

		Document doc = new Document();
		doc.put("filename", docname);
		doc.put("filetype", doctype);
		doc.put("data", docdata);
		long time = System.currentTimeMillis();
		doc.put("time", time);

		try {
			db.saveDocument(doc);
			Date date = new Date(time);
			DocIdServiceImpl.onDocumentPut(doc.getId(), docname, date);
			System.out.println("new document created: " + doc.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Updates the Document in the couchDB with the specified ID by setting the
	 * values of filename, filetype and data
	 * 
	 * @param docname
	 *            Name under which the Document ist to be saved
	 * @param doctype
	 *            Type of the Document
	 * @param docdata
	 *            Content of the Document
	 * @param docid
	 *            ID of the Document
	 */
	private void updateDoc(String docname, String doctype, String docdata,
			String docid) {
		Session session = new Session(StartupServlet.couchDbServer, 5984,
				"admin", Passwords.COUCHDB);

		Database db = null;
		db = session.getDatabase(databaseName);

		try {
			Document doc = db.getDocument(docid);
			if (doc != null) {
				doc.put("filename", docname);
				doc.put("filetype", doctype);
				doc.put("data", docdata);
				doc.put("time", System.currentTimeMillis());

				try {
					db.saveDocument(doc);
					System.out.println("new document created: " + doc.getId());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
