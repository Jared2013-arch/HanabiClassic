package top.fpsmaster.irc.packets.impl.serverside;

import top.fpsmaster.irc.packets.Packet;

public class PacketRegisterRep extends Packet {
    public PacketRegisterRep(String result) {
        super(Type.REGISTERREP, result);
    }
}
