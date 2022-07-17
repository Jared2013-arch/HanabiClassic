package top.fpsmaster.irc.packets.impl;

import top.fpsmaster.irc.packets.Packet;

public class PacketMessage extends Packet {
    public PacketMessage(String content) {
        super(Type.MESSAGE, content);
    }
}
