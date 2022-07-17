package top.fpsmaster.irc.packets;


public class Packet {
    public String content;
    public Type type;

    public Packet(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Packet(Type type) {
        this.type = type;
    }

    public enum Type {
        GET, RETURN,
        LOGIN, LOGINREP,
        EXIT,
        MESSAGE,
        HEARTBEAT,
        COMMAND, HEARTBEATREP,
        REGISTER,REGISTERREP,
    }
}
