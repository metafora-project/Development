package de.kuei.metafora.gwt.smack.client.documents;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;

import de.kuei.metafora.gwt.smack.client.DateConverter;
import de.kuei.metafora.gwt.smack.client.RightClickHTML;
import de.kuei.metafora.gwt.smack.client.TableLayout;

public class Documents extends TableLayout {

	public Documents() {
		super();
	}

	public void add(String s, Anchor a, String time) {
		DateConverter dc = new DateConverter(time);
		String currentTime = dc.convertTime();
		HTML today = new HTML(currentTime, true);
		HTML message = new RightClickHTML(s, a.getHref());

		box.setWidget(row, column - 2, today);
		box.setWidget(row, column - 1, message);
		box.setWidget(row, column, a);
		row++;
	}

}
