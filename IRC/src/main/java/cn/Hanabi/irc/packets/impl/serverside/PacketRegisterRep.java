package cn.hanabi.irc.packets.impl.serverside;

import cn.hanabi.irc.packets.Packet;

public class PacketRegisterRep extends Packet {
    public PacketRegisterRep(String result) {
        super(Type.REGISTERREP, result);
    }
}
