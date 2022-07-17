package cn.Hanabi.irc.packets.impl.clientside;

import top.fpsmaster.irc.packets.Packet;

public class PacketGet extends Packet {
    public String id;

    public PacketGet(String id, String content) {
        super(Type.GET, content);
        this.id = id;
    }
}
