package de.kuei.metafora.gwt.smack.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class Development extends TableLayout {
	final static Languages language = GWT.create(Languages.class);

	public Development() {
		super();
	}

	public void add(String s, String time, String title, String landmarkURL) {
		DateConverter dc = new DateConverter(time);
		Image img = new Image(landmarkURL);
		img.setWidth("30px");
		img.setTitle(title);
		String currentTime = dc.convertTime();
		Label today = new Label(currentTime, false);
		Label message = new Label(s);
		FlexCellFormatter cellFormatter = box.getFlexCellFormatter();
		box.insertRow(row);
		box.setWidget(row, column - 3, today);
		box.setWidget(row, column - 2, img);
		cellFormatter.setAlignment(row, column - 2,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		box.setWidget(row, column - 1, message);

		HTMLTable.RowFormatter rf = box.getRowFormatter();
		for (int i = 1; i < box.getRowCount(); ++i) {
			if ((i % 2) != 0) {
				rf.getElement(i).getStyle().setBackgroundColor("#ecf3fe");
			} else {
				rf.getElement(i).getStyle().setBackgroundColor("white");
			}
		}
	}
}
