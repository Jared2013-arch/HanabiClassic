package cn.Hanabi.irc.packets.impl.clientside;

import top.fpsmaster.irc.packets.Packet;

public class PacketHeartBeat extends Packet {
    public PacketHeartBeat(String content) {
        super(Type.HEARTBEAT, content);
    }
}
