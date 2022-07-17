package top.fpsmaster.irc.packets.impl.serverside;

import top.fpsmaster.irc.packets.Packet;
import top.fpsmaster.irc.server.handler.NettyServerHandler;

public class PacketServerRep extends Packet {
    public String userRank;
    public String serverVersion;
    public String onlineUsersCount;

    public PacketServerRep(String userRank, String serverVersion, String onlineUsersCount, String content) {
        super(Type.LOGINREP, content);
        this.userRank = userRank;
        this.serverVersion = serverVersion;
        this.onlineUsersCount = String.valueOf(NettyServerHandler.users.size());
    }
}
