package de.kuei.metafora.gwt.smack.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

class LasadCommand implements Command {

	private SendXmppMessageAsync xmpp = GWT.create(SendXmppMessage.class);  
	
	private String url;
	private RightClickHTML html;
	
	public LasadCommand(String url, RightClickHTML html) {
		this.url = url;
		this.html = html;
	}

	@Override
	public void execute() {
		html.hidePopup();
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Action time=\""
				+ System.currentTimeMillis()
				+ "\">  <ActionType type=\"CREATE_ELEMENT\" classification=\"USER_INTERACTION\" succeeded=\"UNKNOWN\" />  <users>    <User id=\"METAFORA\" role=\"controller\" />  </users>  <objects>    <Object id=\"0\" type=\"element\">      <properties>        <Property name=\"MAP_ID\" value=\"1\" />        <Property name=\"ELEMENT_TYPE\" value=\"Image\" />        <Property name=\"IMAGE_URL\" value=\""
				+ url
				+ "\"/>      </properties>    </Object>  </objects></Action>";
		try {
			xmpp.sendXmppMessage(xml, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class LasadItem extends MenuItem {
	public LasadItem(String url, RightClickHTML html) {
		super("create referrable object", true, new LasadCommand(url, html));
	}
}


public class RightClickHTML extends HTML implements ContextMenuHandler {

	private final PopupPanel popup;
	private final MenuBar popupMenu;

	public RightClickHTML(String s, String url) {
		super(s);

		popupMenu = new MenuBar();

		popupMenu.addItem(new LasadItem(url, this));

		popup = new PopupPanel(true);
		popup.add(popupMenu);
		
		addDomHandler(this, ContextMenuEvent.getType());
	}

	@Override
	public void onContextMenu(ContextMenuEvent event) {
		event.stopPropagation();
		event.preventDefault();
		showPopupMenu(event.getNativeEvent());
	}

	public void showPopupMenu(NativeEvent event) {
		popup.setPopupPosition(event.getClientX(), event.getClientY());
		popup.show();
	}

	public void hidePopup() {
		popup.hide();
	}
}
