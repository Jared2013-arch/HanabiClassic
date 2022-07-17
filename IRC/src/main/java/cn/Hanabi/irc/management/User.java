package cn.Hanabi.irc.management;

import top.fpsmaster.irc.server.database.DBHelper;
import top.fpsmaster.irc.utils.MD5Utils;

import static top.fpsmaster.irc.server.ServerMain.debug;

public class User {
    public String username;
    public String password;

    public RankManager.Ranks rank;

    public String rankInGame;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String login() {
        password = MD5Utils.getMD5(password);
        if (debug) {
            System.out.println("[DEBUG]User login: " + username + " " + password);
        }
        String res = DBHelper.login(username, password);
        if (res.equals("Admin")) {
            rank = RankManager.Ranks.Admin;
            rankInGame = "§c[DEV]§r";
        } else if (res.equals("Beta")) {
            rank = RankManager.Ranks.Beta;
            rankInGame = "§b[Beta]§r";
        } else if (res.equals("Moderator")) {
            rank = RankManager.Ranks.Moderator;
            rankInGame = "§d[Moderator]§r";
        } else if (res.equals("User")) {
            rank = RankManager.Ranks.User;
            rankInGame = "§7";
        }
        return res;
    }

}
