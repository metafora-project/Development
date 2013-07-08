package de.kuei.metafora.gwt.smack.client;

import com.google.gwt.i18n.client.Messages;

/**
 * Interface for i18n (internationalization)
 * @author Kerstin Pfahler
 * @methods returns a string, whose value depends on the requested language
 * 
 * add "&locale=en" to the end of the URL -> words appear in English
 * add "&locale=he" to URL -> words appear in Hebrew
 * add "&locale=gr" to URL -> words appear in Greek
 * add "&locale=de" to URL -> words appear in German
 * i18n default value = English
 */
public interface Languages extends Messages{
	String Workbench();
	String GeneralOverview();
	String Development();
	String Documents();
	String Send();
	String Browse();
	String TeamMembers();
	String VersionsManagement();
	String MessagingTool();
}
