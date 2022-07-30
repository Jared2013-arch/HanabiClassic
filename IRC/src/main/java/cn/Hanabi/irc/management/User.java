package cn.hanabi.irc.management;

import cn.hanabi.irc.server.ServerMain;
import cn.hanabi.irc.server.database.DBHelper;
import cn.hanabi.irc.server.utils.LogUtil;
import cn.hanabi.irc.utils.MD5Utils;

public class User {
    public String username;
    public String password;
    public String hwid;

    public RankManager.Ranks rank;

    public String rankInGame;

    public User(String username, String password, String hwid) {
        this.username = username;
        this.password = password;
        this.hwid = hwid;
    }

    public String login() {
        password = MD5Utils.getMD5(password);
        String res = DBHelper.login(username, password, hwid);
        LogUtil.info(username + " tried to login:(" + res + ") Password:" + password + " HWID:" + hwid);
        if (res.equals("Admin")) {
            rank = RankManager.Ranks.Admin;
            rankInGame = "§4[DEV]§r";
        } else if (res.equals("Beta")) {
            rank = RankManager.Ranks.Beta;
            rankInGame = "§b[Beta]§r";
        } else if (res.equals("Moderator")) {
            rank = RankManager.Ranks.Moderator;
            rankInGame = "§d[Moderator]§r";
        } else if (res.equals("User")) {
            rank = RankManager.Ranks.User;
            rankInGame = "§7";
        }else if(res.equals("Backer")){
            rank = RankManager.Ranks.Backer;
            rankInGame = "§c[Backer]§r";
        }
        return res;
    }

}
