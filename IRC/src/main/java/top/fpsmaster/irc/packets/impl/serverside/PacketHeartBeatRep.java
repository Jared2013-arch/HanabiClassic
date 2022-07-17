package top.fpsmaster.irc.packets.impl.serverside;

import top.fpsmaster.irc.packets.Packet;

public class PacketHeartBeatRep extends Packet {
    public PacketHeartBeatRep( String content) {
        super(Type.HEARTBEATREP, content);
    }
}
