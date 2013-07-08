package de.kuei.metafora.gwt.smack.server.user;

import java.util.Vector;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import de.kuei.metafora.gwt.smack.client.groupMembers.GroupMemberListener;

public class GroupMemberListenerImpl extends RemoteServiceServlet implements GroupMemberListener{

	@Override
	public void setGroupMembers(String group, Vector<String> user) {
		GroupMembers.getInstance().addGroupMembers(group, user);
	}

	@Override
	public String getGroupMembers(String group) {
		return GroupMembers.getInstance().getGroupMembers(group);
	}

	@Override
	public void removeGroupMember(String user) {
		GroupMembers.getInstance().removeGroupMember(user);
	}

	@Override
	public void addGroupMember(String group, String user) {
		GroupMembers.getInstance().addGroupMember(group, user);
	}

}
