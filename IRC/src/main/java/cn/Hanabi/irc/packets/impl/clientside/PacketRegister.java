package cn.Hanabi.irc.packets.impl.clientside;

import top.fpsmaster.irc.packets.Packet;

public class PacketRegister extends Packet {
    public String username, password, key;

    public PacketRegister(String username, String password, String key) {
        super(Type.REGISTER);
        this.username = username;
        this.password = password;
        this.key = key;
    }
}
