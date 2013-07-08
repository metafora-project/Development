package de.kuei.metafora.gwt.smack.client.groupMembers;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GroupMemberListenerAsync {
	void setGroupMembers(String group, Vector<String> user, AsyncCallback<Void> callback);
	void addGroupMember(String group, String user, AsyncCallback<Void> callback);
	void getGroupMembers(String group, AsyncCallback<String> callback);
	void removeGroupMember(String user, AsyncCallback<Void> callback);
}
