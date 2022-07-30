package cn.hanabi.irc.packets.impl.clientside;

import cn.hanabi.irc.management.User;
import cn.hanabi.irc.packets.Packet;

public class PacketLogin extends Packet {
    public User user;
    public String version;

    public PacketLogin(String username, String password, String hwid) {
        super(Packet.Type.LOGIN);
        user = new User(username, password, hwid);
    }
}
