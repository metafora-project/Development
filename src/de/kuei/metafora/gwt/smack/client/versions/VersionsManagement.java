package de.kuei.metafora.gwt.smack.client.versions;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;

import de.kuei.metafora.gwt.smack.client.DateConverter;
import de.kuei.metafora.gwt.smack.client.TableLayout;

public class VersionsManagement extends TableLayout {

	public VersionsManagement() {
		super();

		/*
		 * HTML today = new
		 * HTML("["+DateTimeFormat.getMediumDateTimeFormat().format(new
		 * Date())+"]", true); Anchor a = new Anchor("Task 1");
		 * a.addClickHandler(new VersionsHandler("Task 1", "12345", "", ""));
		 * box.setWidget(row, column-1, today); box.setWidget(row, column, a);
		 */
	}

	public void add(Anchor a, String time) {
		DateConverter dc = new DateConverter(time);
		String currentTime = dc.convertTime();
		HTML today = new HTML(currentTime, true);
		box.setWidget(row, column - 1, today);
		box.setWidget(row, column, a);
		row++;
	}

	/*
	 * public void addComment(int value){ HTML today = new
	 * HTML("["+DateTimeFormat.getMediumDateTimeFormat().format(new Date())+"]",
	 * true);
	 * 
	 * if(value==1){ //nur zu Testzwecken erst einmal Werte f�r die if.
	 * Sp�ter sollte ein Event mit oder String Bedingung sein (vom Browser an
	 * Client -> Meldung, dass anderer Client etwas gemacht hat) Anchor a = new
	 * Anchor("Task 1"); a.addClickHandler(new VersionsHandler("Task 1",
	 * "12345")); box.setWidget(row, column-1, today); box.setWidget(row,
	 * column, a); } if(value==2){ Anchor a = new Anchor("Task 2");
	 * a.addClickHandler(new VersionsHandler("Task 2", "23456"));
	 * box.setWidget(row, column-1, today); box.setWidget(row, column, a); }
	 * row++; }
	 */

}
