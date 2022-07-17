package cn.Hanabi.irc.packets.impl.clientside;

import top.fpsmaster.irc.management.User;
import top.fpsmaster.irc.packets.Packet;

public class PacketLogin extends Packet {
    public User user;
    public PacketLogin(String username, String password) {
        super(Packet.Type.LOGIN);
        user = new User(username,password);
    }
}
