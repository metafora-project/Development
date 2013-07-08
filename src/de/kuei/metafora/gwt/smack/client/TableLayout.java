package de.kuei.metafora.gwt.smack.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

public class TableLayout extends ScrollPanel {
	protected FlexTable box = new FlexTable();
	protected int row = 0;
	protected int column = 3;

	protected TableLayout() {
		box.setCellPadding(2);
		setPixelSize(405, 600);
		add(box);
		box.setStyleName("flextable");
		setVisible(true);
	}
}
