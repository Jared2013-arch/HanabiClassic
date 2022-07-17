package cn.hanabi.irc.management;

public class User {
    public String username;
    public String password;

    public RankManager.Ranks rank;

    public String rankInGame;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
