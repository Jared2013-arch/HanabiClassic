package cn.hanabi.irc.packets.impl.serverside;

import cn.hanabi.irc.packets.Packet;

public class PacketServerRep extends Packet {
    public String userRank;
    public String serverVersion;
    public String onlineUsersCount;

    public PacketServerRep(String userRank, String serverVersion, String onlineUsersCount, String content) {
        super(Type.LOGINREP, content);
    }
}
