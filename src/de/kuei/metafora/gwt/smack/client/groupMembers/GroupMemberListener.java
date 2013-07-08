package de.kuei.metafora.gwt.smack.client.groupMembers;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("groupmemberlistener")
public interface GroupMemberListener extends RemoteService{
	public void setGroupMembers(String group, Vector<String> user);
	public void addGroupMember(String group, String user);
	public String getGroupMembers(String group);
	public void removeGroupMember(String user);
}
