package cn.hanabi.utils.client;


import java.util.ArrayList;

public class FriendManager {
	private static final ArrayList friends = new ArrayList();

	public static ArrayList getFriends() {
		return friends;
	}

	public static boolean isFriend(String player) {
		for (Object friend : friends) {
			String friendlist = (String) friend;
			if (friendlist.equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}

}
