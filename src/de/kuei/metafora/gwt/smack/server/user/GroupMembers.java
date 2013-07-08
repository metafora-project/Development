package de.kuei.metafora.gwt.smack.server.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GroupMembers {
	private static Map<String, Vector<String>> usersToGroup;
	private static GroupMembers instance = null;

	public static GroupMembers getInstance() {
		if (instance == null) {
			instance = new GroupMembers();
			if (usersToGroup == null) {
				usersToGroup = Collections
						.synchronizedMap(new HashMap<String, Vector<String>>());
			}
		}
		return instance;
	}

	public void addGroupMembers(String group, Vector<String> user) {
		try {
			if (usersToGroup.containsKey(group)) {
				for (String key : usersToGroup.keySet()) {
					if(key.equals(group)){
						Vector<String> groupMembers = usersToGroup.get(key);
						for (String u : user) {
							if (!groupMembers.contains(u)){
								usersToGroup.get(key).add(u);
							}
						}
					}
				}
			} else {
				usersToGroup.put(group, user);
			}
		} catch (Exception e) {
			System.err.println("GroupMembers.addGroupMembers(): "
					+ e.toString());
		}
	}

	public void addGroupMember(String group, String user) {
		try {
			if (usersToGroup.containsKey(group)) {
				for (String key : usersToGroup.keySet()) {
					if (usersToGroup.get(key).equals(group)) {
						Vector<String> groupMembers = usersToGroup.get(key);
						if (!groupMembers.contains(user)) {
							usersToGroup.get(key).add(user);
						}
					}
				}
			} else {
				Vector<String> users = new Vector<String>();
				users.add(user);
				usersToGroup.put(group, users);
			}
		} catch (Exception e) {
			System.err
					.println("GroupMembers.addGroupMember(): " + e.toString());
		}
	}

	public void removeGroupMember(String user) {
		try {
			for (String key : usersToGroup.keySet()) {
				if (usersToGroup.get(key).contains(user)) {
					usersToGroup.get(key).remove(user);
				}
			}
		} catch (Exception e) {
			System.err.println("GroupMembers.removeGroupMember(): "
					+ e.toString());
		}
	}

	public String getGroupMembers(String group) {
		try {
			String users = "";
			for (int i = 0; i < usersToGroup.get(group).size(); i++) {
				if (users.equals("")) {
					users = usersToGroup.get(group).get(i);
				} else {
					users += "|" + usersToGroup.get(group).get(i);
				}
			}
			return users;
		} catch (Exception e) {
			System.err.println("GroupMembers.getGroupMembers(): "
					+ e.toString());
			return null;
		}
	}
}
