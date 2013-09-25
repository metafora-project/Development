package de.kuei.metafora.gwt.smack.client;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.kuei.metafora.gwt.smack.client.documents.DocumentCallBack;
import de.kuei.metafora.gwt.smack.client.documents.DocumentService;
import de.kuei.metafora.gwt.smack.client.documents.DocumentServiceAsync;
import de.kuei.metafora.gwt.smack.client.documents.Documents;
import de.kuei.metafora.gwt.smack.client.eventServiceListeners.breakingnews.BreakingNewsListener;
import de.kuei.metafora.gwt.smack.client.eventServiceListeners.breakingnews.BreakingNewsListenerImpl;
import de.kuei.metafora.gwt.smack.client.eventServiceListeners.documents.DocumentsListener;
import de.kuei.metafora.gwt.smack.client.eventServiceListeners.documents.DocumentsListenerImpl;
import de.kuei.metafora.gwt.smack.client.eventServiceListeners.versions.VersionsListener;
import de.kuei.metafora.gwt.smack.client.eventServiceListeners.versions.VersionsListenerImpl;
import de.kuei.metafora.gwt.smack.client.groupMembers.GroupMemberListener;
import de.kuei.metafora.gwt.smack.client.groupMembers.GroupMemberListenerAsync;
import de.kuei.metafora.gwt.smack.client.handler.MessagingToolHandler;
import de.kuei.metafora.gwt.smack.client.versions.VersionsCallBack;
import de.kuei.metafora.gwt.smack.client.versions.VersionsManagement;
import de.kuei.metafora.gwt.smack.client.versions.VersionsService;
import de.kuei.metafora.gwt.smack.client.versions.VersionsServiceAsync;
import de.kuei.metafora.gwt.smack.shared.eventservice.EventServiceDomains;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Workbench extends HorizontalPanel implements EntryPoint {

	// object needed to implement i18n through Languages interface
	final static Languages language = GWT.create(Languages.class);
	private static GroupMemberListenerAsync groupMemberListener = GWT
			.create(GroupMemberListener.class);
	public static String token = null;
	public static String groupId = null;
	public static String challengeId = null;
	public static String challengeName = null;
	public static String testServer = null;
	public static String locale = "en";
	public static String user = null;
	public static Vector<String> users = null;

	public static HorizontalPanel hPanel1, hPanel2, hPanel3;
	public static VerticalPanel vPanel1, vPanel2;
	private Development development;

	private Documents doctable;
	private VersionsManagement vm;

	private final FormPanel enterF = new FormPanel();
	private HorizontalPanel uploadPanel = new HorizontalPanel();
	private FileUpload search = new FileUpload();
	private Button send;

	private DocumentServiceAsync dsa = GWT.create(DocumentService.class);
	private VersionsServiceAsync versionsService = GWT
			.create(VersionsService.class);

	private RemoteEventService remoteEventService;

	private static Domain breakingNewsDomain = DomainFactory
			.getDomain(EventServiceDomains.BREAKINGNEWSDOMAIN);
	private static Domain documentsDomain = DomainFactory
			.getDomain(EventServiceDomains.DOCUMENTSDOMAIN);
	private static Domain versionsDomain = DomainFactory
			.getDomain(EventServiceDomains.VERSIONSDOMAIN);

	private BreakingNewsListener breakingNewsListener;
	private DocumentsListener documentsListener;
	private VersionsListener versionsListener;

	public void onModuleLoad() {
		hPanel1 = new HorizontalPanel();
		hPanel2 = new HorizontalPanel();
		hPanel3 = new HorizontalPanel();
		vPanel1 = new VerticalPanel();
		vPanel2 = new VerticalPanel();
		hPanel1.setVisible(true);
		hPanel2.setVisible(true);
		hPanel3.setVisible(true);
		vPanel1.setSize("95%", "95%");
		hPanel1.setHeight((Window.getClientHeight() - 20) + "px");
		hPanel1.setWidth(((Window.getClientWidth() / 2) - 55) + "px");
		hPanel2.setHeight((Window.getClientHeight() - 20) + "px");
		hPanel2.setWidth(((Window.getClientWidth() / 2) - 55) + "px");
		hPanel3.setHeight(Window.getClientHeight() + "px");
		hPanel3.setWidth("35px");
		vPanel1.setVisible(true);
		vPanel1.setHeight(((Window.getClientHeight() / 2) - 20) + "px");
		vPanel1.setWidth(((Window.getClientWidth() / 2) - 55) + "px");
		vPanel2.setVisible(true);
		vPanel2.setHeight(((Window.getClientHeight() / 2) - 20) + "px");
		vPanel2.setWidth(((Window.getClientWidth() / 2) - 55) + "px");
		hPanel1.add(vPanel1);
		hPanel2.add(vPanel2);
		hPanel1.setHorizontalAlignment(ALIGN_LEFT);
		hPanel2.setHorizontalAlignment(ALIGN_LEFT);
		hPanel3.setVerticalAlignment(ALIGN_TOP);

		Workbench.users = new Vector<String>();
		Workbench.token = UrlDecoder.getParameter("token");
		Workbench.groupId = UrlDecoder.getParameter("groupId");
		Workbench.challengeId = UrlDecoder.getParameter("challengeId");
		Workbench.challengeName = UrlDecoder.getParameter("challengeName");
		Workbench.testServer = UrlDecoder.getParameter("testServer");
		Workbench.locale = UrlDecoder.getParameter("locale");
		Workbench.user = UrlDecoder.getParameter("user");

		Workbench.users.add(user);
		Map<String, List<String>> parameters = Window.Location
				.getParameterMap();
		Set<String> keySet = parameters.keySet();
		for (String key : keySet) {
			if (key.startsWith("otherUser")) {
				List<String> values = parameters.get(key);
				for (String u : values) {
					Workbench.users.add(u);
				}
			}
		}

		groupMemberListener.setGroupMembers(Workbench.groupId, Workbench.users,
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void result) {
					}
				});

		Label dev = new Label(language.Development());
		dev.setStyleName("heading");
		vPanel1.add(dev);
		development = new Development();
		vPanel1.add(development);

		Label dc = new Label(language.Documents());
		dc.setStyleName("heading");
		vPanel2.add(dc);

		if (testServer.equals("true")) {
			enterF.setAction("https://metafora.ku-eichstaett.de/workbench/development/fileupload");
		} else {
			enterF.setAction("https://metaforaserver.ku.de/workbench/development/fileupload");
		}

		enterF.setEncoding(FormPanel.ENCODING_MULTIPART);
		enterF.setMethod(FormPanel.METHOD_POST);

		String user = null;
		for (String u : users) {
			if (user == null)
				user = u;
			else
				user += "|" + u;
		}

		TextBox username = new TextBox();
		username.setVisible(false);
		if (user != null)
			username.setText(user);
		else
			username.setText("undefined");
		username.setName("userName");
		uploadPanel.add(username);

		TextBox groupId = new TextBox();
		groupId.setVisible(false);
		groupId.setText(Workbench.groupId);
		groupId.setName("groupId");
		uploadPanel.add(groupId);

		TextBox challengeId = new TextBox();
		challengeId.setVisible(false);
		challengeId.setText(Workbench.challengeId);
		challengeId.setName("challengeId");
		uploadPanel.add(challengeId);

		TextBox challengeName = new TextBox();
		challengeName.setVisible(false);
		challengeName.setText(Workbench.challengeName);
		challengeName.setName("challengeName");
		uploadPanel.add(challengeName);

		search.setName("fileUp");
		search.setStyleName("button");
		search.setTitle(language.Browse());
		uploadPanel.add(search);

		send = new Button(language.Send(), new ClickHandler() {

			public void onClick(ClickEvent event) {
				enterF.submit();
			}
		});
		send.setStyleName("button");
		uploadPanel.add(send);
		enterF.add(uploadPanel);
		vPanel2.add(enterF);
		doctable = new Documents();
		// doctable.getElement().getStyle().setHeight(350, Unit.PX);
		doctable.setWidth(((Window.getClientWidth() / 2) - 100) + "px");
		dsa.getIdToNames(new DocumentCallBack(doctable));
		vPanel2.add(doctable);

		Label verm = new Label(language.VersionsManagement());
		verm.setStyleName("heading");
		vPanel2.add(verm);
		vm = new VersionsManagement();
		// vm.getElement().getStyle().setHeight(350, Unit.PX);
		vm.setWidth(((Window.getClientWidth() / 2) - 100) + "px");
		versionsService.getIdToNames(new VersionsCallBack(vm));
		vPanel2.add(vm);

		Button messagingToolButton = new Button();
		messagingToolButton.setText(language.MessagingTool());
		messagingToolButton.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		// messagingToolButton.getElement().addClassName("rotate");
		messagingToolButton.addClickHandler(new MessagingToolHandler());
		hPanel3.add(messagingToolButton);

		this.add(hPanel1);
		this.add(hPanel2);
		this.add(hPanel3);

		setVisible(true);

		RootPanel rootLayout = RootPanel.get();
		rootLayout.add(this);
		Window.enableScrolling(true);

		remoteEventService = RemoteEventServiceFactory.getInstance()
				.getRemoteEventService();
		breakingNewsListener = new BreakingNewsListenerImpl(development);
		documentsListener = new DocumentsListenerImpl(doctable);
		versionsListener = new VersionsListenerImpl(vm);

		remoteEventService.addListener(breakingNewsDomain,
				breakingNewsListener, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error registering the BreakingNewsListener:\n"
								+ caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
					}
				});

		remoteEventService.addListener(documentsDomain, documentsListener,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error registering the DocumentsListener:\n"
								+ caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
					}
				});

		remoteEventService.addListener(versionsDomain, versionsListener,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error registering the VersionsListener:\n"
								+ caught.getMessage());
					}

					@Override
					public void onSuccess(Void result) {
					}
				});
	}

}
