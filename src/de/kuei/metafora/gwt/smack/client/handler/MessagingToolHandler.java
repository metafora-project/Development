package de.kuei.metafora.gwt.smack.client.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

import de.kuei.metafora.gwt.smack.client.Workbench;

public class MessagingToolHandler implements ClickHandler {

	private Frame fMessagingTool = null;
	// productive system
	// String url =
	// "http://web.lkldev.ioe.ac.uk/MonitorInterventionMetafora/?receiver=METAFORA_TEST&userType=METAFORA_USER";
	// test system
	private static String metaforaURL = "https://metafora-project.de/home/home/usergroup?url=http%3A%2F%2Fweb.lkldev.ioe.ac.uk%2FMonitorInterventionMetafora%2F%3FuserType%3DMETAFORA_USER";
	private static String metaforaServerURL = "https://metafora-project.de/home/home/usergroup?url=http%3A%2F%2Fweb.lkldev.ioe.ac.uk%2FMonitorInterventionMetafora%2F%3FuserType%3DMETAFORA_USER";

	public MessagingToolHandler() {
		fMessagingTool = new Frame();
		fMessagingTool.setVisible(false);
		RootPanel.get().add(fMessagingTool);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (!fMessagingTool.isVisible()) {
			showFrame();
		} else {
			fMessagingTool.setVisible(false);
			fMessagingTool.setUrl("");
		}
	}

	private void showFrame() {
		String text = "";
		if (Workbench.testServer.equals("true")) {
			text += metaforaURL + "&receiver=METAFORA_TEST";
		} else {
			text += metaforaServerURL + "&receiver=METAFORA";
		}

		fMessagingTool.setUrl(text + "&locale=" + Workbench.locale + "&token="
				+ URL.encode(Workbench.token) + "&user="
				+ URL.encode(Workbench.user) + "&groupId="
				+ URL.encode(Workbench.groupId) + "&challengeId="
				+ URL.encode(Workbench.challengeId) + "&challengeName="
				+ URL.encode(Workbench.challengeName) + "&testServer="
				+ URL.encode(Workbench.testServer) + "&messaging=true");

		fMessagingTool.setHeight(Window.getClientHeight() + "px");
		fMessagingTool.setWidth(Workbench.hPanel2.getElement().getOffsetWidth()
				+ "px");
		fMessagingTool.getElement().getStyle().setZIndex(100);
		fMessagingTool.getElement().getStyle().setBackgroundColor("white");
		RootPanel.get().setWidgetPosition(fMessagingTool,
				Workbench.hPanel2.getAbsoluteLeft(), 0);
		fMessagingTool.setVisible(true);
	}
}
